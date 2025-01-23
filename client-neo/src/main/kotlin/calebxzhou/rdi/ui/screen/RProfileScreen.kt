package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ihq.HttpMethod
import calebxzhou.rdi.logger
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.model.RServer
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.component.RFormScreenSubmitHandler
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.formScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.ui.general.alertOs
import calebxzhou.rdi.ui.general.confirm
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.multiplayer.resolver.ServerAddress
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class RProfileScreen(
    val account: RAccount,
    val server: RServer
    ): RScreen("我的信息") {
    override fun init() {
        gridLayout (this, hAlign = HAlign.CENTER){
            button("start", text = "开始") {
                RSoundPlayer.stopAll()
                connect()
            }
            button("basic_info", text = "修改信息") {
                alert("开发中，周末前上线")
            }
            button("clothes", text = "皮肤") {
                mc go picServerSkinScreen
            }
            button("smp", text = "团队") {
                alert("开发中，2月前上线")
            }
        }
        super.init()
    }

    override fun onClose() {
       confirm ("确定要登出吗？"){
           RServer.now?.disconnect()
           RAccount.now?.logout()
           mc.goHome()
       }
    }
    private fun connect(){
        mcMainThread {
            ConnectScreen.startConnecting(
                this@RProfileScreen, mc, ServerAddress(server.ip, server.gamePort), server.mcData, false
            )
        }
    }
    override fun tick() {
        if (mc.pressingEnter) {
            connect()
        }
        super.tick()
    }
    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        PlayerFaceRenderer.draw(guiGraphics, account.skinLocation, width / 2 - 10, height / 5, 20)
        drawTextAtCenter(guiGraphics, "昵称：${account.name}", height / 2 - 20)
        drawTextAtCenter(guiGraphics, "RDID：${account.id}", height / 2)
        drawTextAtCenter(guiGraphics, "QQ：${account.qq}", height / 2 + 40)
        mc.player?.let {
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics, 200, 200, 30, mouseX.toFloat(), mouseY.toFloat(),
                it
            )
        }
    }
    private val picServerSkinScreen
        get() = formScreen(this, "设定皮肤披风") {
            bottomLayoutBuilder = {
                button("mojang",text="从正版账号导入") {
                    mc go mojangSkinScreen
                }
                button("blessing_skin",text="从皮肤站导入") {
                    mc go blessingSkinScreen
                }

            }
            text("skin", "皮肤链接", 256, defaultValue = account.cloth?.skin, nullable = true)
            text("cape", "披风链接", 256, defaultValue = account.cloth?.cape, nullable = true)
            checkbox("slim", "Alex瘦版皮肤")
            submit = {
                setPicServerCloth(it)

            }
        }
    private val mojangSkinScreen
        get() = formScreen(this, "导入正版皮肤披风") {
            text("name", "正版玩家名", 16)
            checkbox("skin", "导入皮肤")
            checkbox("cape", "导入披风")
            submit = {
                    setMojangSkinCape(it)

            }
        }
    private val blessingSkinScreen
        get() = formScreen(this, "导入皮肤站皮肤披风") {
            text("skin", "皮肤链接", 256, nullable = true)
            text("cape", "披风链接", 256, nullable = true)
            submit = {
                    setBlessingServerSkinCape(it)

            }
        }
    //验证服饰链接（有效url，并且能够获取png图片）
    private fun validateClothUrl(url: String, handler: RFormScreenSubmitHandler): Boolean {
        if (!url.isValidHttpUrl()) {
            alertOs("链接必须http开头",  )
            return false
        }

        val response = HttpClients.createDefault().execute(HttpGet(url))
        val entity = response.entity
        //内容类型
        val contentType = ContentType.getOrDefault(entity)

        if (contentType != ContentType.IMAGE_PNG) {
            alertOs("不是PNG图片",  )
            return false
        }

        EntityUtils.consume(entity)
        return true
    }
    //设定服饰
    private fun setCloth(cloth: RAccount.Cloth){
        val params = arrayListOf<Pair<String, String>>()
        params += "isSlim" to cloth.isSlim.toString()
        params += "skin" to cloth.skin.toString()
        params += "cape" to cloth.cape.toString()
        server.hqSend(HttpMethod.POST,"skin", params) {
            alert("换皮成功 重新登录看新皮肤")
        }
    }
    //从图床导入服饰
    private fun setPicServerCloth(handler: RFormScreenSubmitHandler) {
        val skin = handler.formData["skin"] ?: ""
        val cape = handler.formData["cape"] ?: ""
        val isSlim = handler.formData["slim"] == "true"
        if (skin.isNotBlank()) {
            if (!validateClothUrl(skin, handler)) {
                handler.finish()
                return
            }
        }

        if (cape.isNotBlank()) {
            if (!validateClothUrl(cape, handler)) {
                handler.finish()
                return
            }
        }
        setCloth(RAccount.Cloth(isSlim, skin, cape))
    }

    //blessing skin皮肤站,通过链接获取皮肤hash从而得到图片
    private fun setBlessingServerSkinCape(handler: RFormScreenSubmitHandler) {
        var skin = handler.formData["skin"] ?: ""
        var cape = handler.formData["cape"] ?: ""
        val isSlim = handler.formData["slim"] == "true"
        if (skin.isNotBlank()) {
            if (!skin.isValidHttpUrl()) {
                alertOs("皮肤链接格式错误",  )
                return
            }
            if (!skin.contains("/skinlib/show/")) {
                alertOs("仅支持blessing skin架构的皮肤站",  )
                return
            }
            val url = skin.extractDomain() + "texture/" + skin.substringAfterLast('/')
            val response = HttpClients.createDefault().execute(HttpGet(url))
            val entity = response.entity
            val statusCode = response.statusLine.statusCode

            if (statusCode !in 200..299) {
                alertOs("获取皮肤失败：$statusCode\n",  )
                logger.error("$statusCode ${EntityUtils.toString(entity)}")
                return
            }

            val hash = Json.parseToJsonElement(EntityUtils.toString(entity).also { logger.info(it) })
                .jsonObject["hash"]?.jsonPrimitive?.content
                ?: let {
                    alertOs("无法获取皮肤hash数据",  )
                    return
                }

            skin = skin.extractDomain() + "textures/" + hash
        }

        if (cape.isNotBlank()) {
            if (!cape.isValidHttpUrl()) {
                alertOs("披风链接格式错误",  )
                return
            }
            if (!cape.contains("/skinlib/show/")) {
                alertOs("仅支持blessing skin架构的皮肤站",  )
                return
            }
            val url = cape.extractDomain() + "texture/" + cape.substringAfterLast('/')
            val response = HttpClients.createDefault().execute(HttpGet(url))
            val entity = response.entity
            val statusCode = response.statusLine.statusCode

            if (statusCode !in 200..299) {
                alertOs("获取披风失败：$statusCode\n",  )
                logger.error("$statusCode ${EntityUtils.toString(entity)}")
                return
            }

            val hash = Json.parseToJsonElement(
                EntityUtils.toString(entity).also { logger.info(it) }).jsonObject["hash"]?.jsonPrimitive?.content ?: let {
                alertOs("无法获取披风hash数据",  )
                return
            }
            cape = cape.extractDomain() + "textures/" + hash
        }


        setCloth(RAccount.Cloth(isSlim, skin, cape))
    }

    private fun setMojangSkinCape(handler: RFormScreenSubmitHandler) {
        val name = handler.formData["name"]
        val importSkin = handler.formData["skin"] == "true"
        val importCape = handler.formData["cape"] == "true"
        if (!importSkin && !importCape) {
            alert("请选择皮肤或披风",  )
            handler.finish()
            return
        }
        val httpClient = HttpClients.createDefault()

// First request
        val httpPost = HttpPost("https://api.mojang.com/profiles/minecraft")
        httpPost.entity = StringEntity("[\"${name}\"]", ContentType.APPLICATION_JSON)
        val response = httpClient.execute(httpPost)
        val entity = response.entity
        val statusCode = response.statusLine.statusCode

        if (statusCode in 200..299) {
            val body1 = EntityUtils.toString(entity).also { logger.info(it) }
            val id = Json.parseToJsonElement(body1)
                .jsonArray.getOrNull(0)?.jsonObject?.get("id")?.jsonPrimitive?.content
                ?: let {
                    alertOs("找不到对应玩家",)
                    handler.finish()
                    return
                }

            // Second request
            val httpGet = HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/$id")
            val response2 = httpClient.execute(httpGet)
            val entity2 = response2.entity
            val statusCode2 = response2.statusLine.statusCode

            if (statusCode2 in 200..299) {
                toastOk("读取皮肤成功 正在设置")
                val body2 = EntityUtils.toString(entity2).also { logger.info(it) }
                val propValue =
                    Json.parseToJsonElement(body2).jsonObject["properties"]?.jsonArray?.get(0)?.jsonObject?.get("value")?.jsonPrimitive?.content
                        ?: ""
                val texture =
                    Json.parseToJsonElement(propValue.decodeBase64().also { logger.info(it) }).jsonObject["textures"]
                val skinURL =
                    texture?.jsonObject?.get("SKIN")?.jsonObject?.get(
                        "url"
                    )?.jsonPrimitive?.content
                val isSlim =
                    texture?.jsonObject?.get("SKIN")?.jsonObject?.get(
                        "metadata"
                    )?.jsonObject?.get("model")?.jsonPrimitive?.content == "slim"
                val capeURL =
                    texture?.jsonObject?.get("CAPE")?.jsonObject?.get(
                        "url"
                    )?.jsonPrimitive?.content

                setCloth(RAccount.Cloth(isSlim, skinURL ?: "", capeURL ?: ""))
            } else {
                alertOs("获取皮肤失败")
                logger.error(response2.statusLine)
            }
        } else {
            alertOs("找不到对应玩家")
            logger.error(response.statusLine)
        }
    }

}