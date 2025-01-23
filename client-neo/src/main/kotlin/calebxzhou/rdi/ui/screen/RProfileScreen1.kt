package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.account.ChangeClothSPacket
import calebxzhou.rdi.ihq.protocol.account.ChangeNameSPacket
import calebxzhou.rdi.ihq.protocol.account.ChangePwdSPacket
import calebxzhou.rdi.ihq.protocol.account.ChangeQQSPacket
import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import calebxzhou.rdi.logger
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.*
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class RProfileScreen1(val account: RAccount) : RScreen("个人信息") {
    val skinResponseHandler: (ChangeClothSPacket, ResponseCPacket) -> Unit =
        { cloth: ChangeClothSPacket, packet: ResponseCPacket ->
            if (!packet.ok) {
                alertOs(packet.data)
            } else {
                toastOk("换皮成功")
                RAccount.now?.cloth = cloth.cloth
                mc.screen?.onClose()
            }
        }
    private val mojangSkinScreen
        get() = formScreen(this, "导入正版皮肤披风") {
            text("name", "正版玩家名", 16)
            checkbox("skin", "导入皮肤")
            checkbox("cape", "导入披风")
            submit = {
                bgTask {
                    setMojangSkinCape(it)
                }
            }
        }
    private val picServerSkinScreen
        get() = formScreen(this, "导入图床皮肤披风") {
            text("skin", "皮肤链接", 256, defaultValue = account.cloth?.skin, nullable = true)
            text("cape", "披风链接", 256, defaultValue = account.cloth?.cape, nullable = true)
            checkbox("slim", "Alex瘦版皮肤")
            submit = {
                bgTask {
                    setPicServerCloth(it)
                }
            }
        }
    private val blessingSkinScreen
        get() = formScreen(this, "导入皮肤站皮肤披风") {
            text("skin", "皮肤链接", 256, nullable = true)
            text("cape", "披风链接", 256, nullable = true)
            submit = {
                bgTask {
                    setBlessingServerSkinCape(it)
                }
            }
        }

    //验证服饰链接（有效url，并且能够获取png图片）
    private fun validateClothUrl(url: String, handler: RFormScreenSubmitHandler): Boolean {
        if (!url.isValidHttpUrl()) {
            alertOs("链接必须http开头")
            return false
        }

        val response = HttpClients.createDefault().execute(HttpGet(url))
        val entity = response.entity
        //内容类型
        val contentType = ContentType.getOrDefault(entity)

        if (contentType != ContentType.IMAGE_PNG) {
            alertOs("不是PNG图片")
            return false
        }

        EntityUtils.consume(entity)
        return true
    }

    //从图床导入服饰
    private suspend fun setPicServerCloth(handler: RFormScreenSubmitHandler) {
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
        val packet = ChangeClothSPacket(RAccount.Cloth(isSlim, skin, cape))
        IhqClient.send(packet) { skinResponseHandler(packet, it) }

    }

    //blessing skin皮肤站,通过链接获取皮肤hash从而得到图片
    private suspend fun setBlessingServerSkinCape(handler: RFormScreenSubmitHandler) {
        var skin = handler.formData["skin"] ?: ""
        var cape = handler.formData["cape"] ?: ""
        val isSlim = handler.formData["slim"] == "true"
        if (skin.isNotBlank()) {
            if (!skin.isValidHttpUrl()) {
                alertOs("皮肤链接格式错误")
                return
            }
            if (!skin.contains("/skinlib/show/")) {
                alertOs("仅支持blessing skin架构的皮肤站")
                return
            }
            val url = skin.extractDomain() + "texture/" + skin.substringAfterLast('/')
            val response = HttpClients.createDefault().execute(HttpGet(url))
            val entity = response.entity
            val statusCode = response.statusLine.statusCode

            if (statusCode !in 200..299) {
                alertOs("获取皮肤失败：$statusCode\n")
                logger.error("$statusCode ${EntityUtils.toString(entity)}")
                return
            }

            val hash = Json.parseToJsonElement(EntityUtils.toString(entity).also { logger.info(it) })
                .jsonObject["hash"]?.jsonPrimitive?.content
                ?: let {
                    alertOs("无法获取皮肤hash数据")
                    return
                }

            skin = skin.extractDomain() + "textures/" + hash
        }

        if (cape.isNotBlank()) {
            if (!cape.isValidHttpUrl()) {
                alertOs("披风链接格式错误")
                return
            }
            if (!cape.contains("/skinlib/show/")) {
                alertOs("仅支持blessing skin架构的皮肤站")
                return
            }
            val url = cape.extractDomain() + "texture/" + cape.substringAfterLast('/')
            val response = HttpClients.createDefault().execute(HttpGet(url))
            val entity = response.entity
            val statusCode = response.statusLine.statusCode

            if (statusCode !in 200..299) {
                alertOs("获取披风失败：$statusCode\n")
                logger.error("$statusCode ${EntityUtils.toString(entity)}")
                return
            }

            val hash = Json.parseToJsonElement(
                EntityUtils.toString(entity).also { logger.info(it) }).jsonObject["hash"]?.jsonPrimitive?.content
                ?: let {
                    alertOs("无法获取披风hash数据")
                    return
                }
            cape = cape.extractDomain() + "textures/" + hash
        }


        val packet = ChangeClothSPacket(RAccount.Cloth(isSlim, skin, cape))
        IhqClient.send(packet) { skinResponseHandler(packet, it) }

    }

    private suspend fun setMojangSkinCape(handler: RFormScreenSubmitHandler) {
        val name = handler.formData["name"]
        val importSkin = handler.formData["skin"] == "true"
        val importCape = handler.formData["cape"] == "true"
        if (!importSkin && !importCape) {
            alertOs("请选择皮肤或披风")
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
                    alertOs("找不到对应玩家")
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
                val params = mutableListOf<Pair<String, String>>()
                if (skinURL != null && importSkin) {
                    params += "skin" to skinURL

                }
                if (capeURL != null && importCape) {
                    params += "cape" to capeURL

                }
                val packet = ChangeClothSPacket(RAccount.Cloth(isSlim, skinURL ?: "", capeURL ?: ""))
                IhqClient.send(packet) { skinResponseHandler(packet, it) }
            } else {
                alertOs("获取皮肤失败")
                logger.error(response2.statusLine)
            }
        } else {
            alertOs("找不到对应玩家")
            logger.error(response.statusLine)
        }
    }

    override fun init() {
        gridLayout(this, hAlign = HAlign.CENTER) {
            button("basic_info", text = "修改信息") {

            }
            button("改QQ") {
                mc go formScreen(this@RProfileScreen1, "换QQ") {
                    text("qq", "新QQ", 10, defaultValue = account.qq)
                    submit = {
                        val qq = it.formData["qq"]!!
                        IhqClient.send(ChangeQQSPacket(qq)) { resp ->
                            if (resp.ok) {
                                showToast("成功修改qq为${qq}")
                                RAccount.now?.qq = qq
                                LocalStorage["usr"] = qq
                                mc go RProfileScreen1(RAccount.now!!)
                            } else {
                                alertOs(resp.data)
                            }
                        }
                    }
                }
            }
            button("改密码") {
                mc go formScreen(this@RProfileScreen1, "修改密码") {
                    pwd("opwd", "旧密码")
                    pwd("pwd", "新密码")
                    pwd("cpwd", "确认密码")
                    submit = {
                        val opwd = it.formData["opwd"]!!
                        if (account.pwd != opwd) {
                            alertOs("旧密码错误")
                        } else {

                            val pwd = it.formData["pwd"]!!
                            val cpwd = it.formData["cpwd"]!!
                            if (pwd != cpwd) {
                                alertOs("确认密码与密码不一致")
                            } else {
                                IhqClient.send(ChangePwdSPacket(pwd)) { resp ->
                                    if (resp.ok) {
                                        showToast("成功修改密码为${pwd}")
                                        LocalStorage += "pwd" to pwd
                                        RAccount.now?.pwd = pwd
                                        LocalStorage["pwd"] = pwd
                                        mc go RProfileScreen1(RAccount.now!!)
                                    } else {
                                        alertOs(resp.data)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            button("皮肤披风") {
                mc go optionScreen(this@RProfileScreen1) {
                    "从正版玩家导入" to mojangSkinScreen
                    "从皮肤站导入" to blessingSkinScreen
                    "从图床导入" to picServerSkinScreen
                }

            }
            button("改昵称") {
                mc go formScreen(this@RProfileScreen1, "修改昵称") {
                    text("name", "昵称", 16, defaultValue = account.name)
                    submit = {
                        val name = it.formData["name"]!!
                        IhqClient.send(ChangeNameSPacket(name)) { resp ->
                            if (resp.ok) {
                                showToast("成功修改昵称为${name}")
                                RAccount.now?.name = name
                                mc go RProfileScreen1(RAccount.now!!)
                            } else {
                                alertOs(resp.data)
                            }
                        }
                    }
                }
            }
            button("登出") {
                //confirm("真的要退出账号${account.name}吗？", this@RProfileScreen) {
                RAccount.now = null
                toastOk("成功退出登录！")
                mc go RTitleScreen()
                // }
            }
        }

        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        PlayerFaceRenderer.draw(guiGraphics, account.skinLocation, width / 2 - 10, height / 5, 20)
        drawTextAtCenter(guiGraphics, "昵称：${account.name}", height / 2 - 20)
        drawTextAtCenter(guiGraphics, "RDID：${account.id}", height / 2)
        drawTextAtCenter(guiGraphics, "UUID：${account.uuid}", height / 2 + 20)
        drawTextAtCenter(guiGraphics, "QQ：${account.qq}", height / 2 + 40)
        mc.player?.let {
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics, 200, 200, 30, mouseX.toFloat(), mouseY.toFloat(),
                it
            )
        }
    }
}