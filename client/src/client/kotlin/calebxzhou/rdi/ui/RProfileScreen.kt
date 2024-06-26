package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.log
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.ROptionScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.ui.general.confirm
import calebxzhou.rdi.ui.layout.RGridLayout
import calebxzhou.rdi.util.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.InventoryScreen

class RProfileScreen(val account: RAccount) : RScreen("个人信息管理") {
    private val mojangSkinScreen
        get() = formScreen(this, "导入正版皮肤披风") {
            text("name", "正版玩家名", 16)
            checkbox("skin", "导入皮肤")
            checkbox("cape", "导入披风")
            submit {
                bgTask {
                    setMojangSkinCape(it)
                }
            }
        }.build()
    private val picServerSkinScreen
        get() = formScreen(this, "导入图床皮肤披风") {
            text("skin", "皮肤链接", 256, defaultValue = account.skin, nullable = true)
            text("cape", "披风", 256, defaultValue = account.cape, nullable = true)
            submit {
                bgTask {
                    setPicServerSkinCape(it)
                }
            }
        }.build()
    private suspend fun validateSkinCapeUrl(url:String,handler: RFormScreenSubmitHandler): Boolean{
        if (!url.isValidHttpUrl()) {
            alertErr("链接必须http开头",handler.screen)
            return false
        }
        val response = HttpClient().get(url)
        if (response.contentType() != ContentType.Image.PNG) {
            alertErr("不是PNG图片", handler.screen)
            return false
        }
        return true
    }
    private suspend fun setPicServerSkinCape(handler: RFormScreenSubmitHandler) {
        val skin = handler.formData["skin"]?:""
        val cape = handler.formData["cape"]?:""
        if (skin.isNotBlank()) {
            RAccount.now?.skin = skin
            if(!validateSkinCapeUrl(skin,handler)){
                handler.finish()
                return
            }
        }

        if (cape.isNotBlank()) {
            RAccount.now?.cape = cape
            if(!validateSkinCapeUrl(cape,handler)){
                handler.finish()
                return
            }
        }

        IhqClient.put("profile", listOf("skin" to skin,"cape" to cape)){
            showToast("成功修改皮肤披风")
            mc goScreen RProfileScreen(RAccount.now!!)
        }

    }

    private suspend fun setMojangSkinCape(handler: RFormScreenSubmitHandler) {
        val name = handler.formData["name"]
        val importSkin = handler.formData["skin"] == "true"
        val importCape = handler.formData["cape"] == "true"
        if (!importSkin && !importCape) {
            alertErr("请选择皮肤或披风", handler.screen)
            handler.finish()
            return
        }
        val response = HttpClient().post("https://api.mojang.com/profiles/minecraft") {
            setBody("[\"${name}\"]")
            contentType(ContentType.Application.Json)
        }
        if (response.status.isSuccess()) {
            val body1 = response.bodyAsText().also { log.info(it) }
            val id =
                Json.parseToJsonElement(body1).jsonArray.getOrNull(0)?.jsonObject?.get("id")?.jsonPrimitive?.content
                    ?: let {
                        alertErr("找不到对应玩家", it)
                        handler.finish()
                        return
                    }
            val response2 = HttpClient().get("https://sessionserver.mojang.com/session/minecraft/profile/$id")
            if (response2.status.isSuccess()) {
                toastOk("读取皮肤成功 正在设置")
                val body2 = response2.bodyAsText().also { log.info(it) }
                val propValue =
                    Json.parseToJsonElement(body2).jsonObject["properties"]?.jsonArray?.get(0)?.jsonObject?.get("value")?.jsonPrimitive?.content
                        ?: ""
                val texture =
                    Json.parseToJsonElement(propValue.decodeBase64().also { log.info(it) }).jsonObject["textures"]
                val skinURL =
                    texture?.jsonObject?.get("SKIN")?.jsonObject?.get(
                        "url"
                    )?.jsonPrimitive?.content
                val capeURL =
                    texture?.jsonObject?.get("CAPE")?.jsonObject?.get(
                        "url"
                    )?.jsonPrimitive?.content
                val params = mutableListOf<Pair<String, String>>()
                if (skinURL != null && importSkin) {
                    params += "skin" to skinURL
                    RAccount.now?.skin = skinURL
                }
                if (capeURL != null && importCape) {
                    params += "cape" to capeURL
                    RAccount.now?.cape = capeURL
                }
                IhqClient.put("profile", params) {
                    showToast("成功修改皮肤披风")
                    mc goScreen RProfileScreen(RAccount.now!!)
                }
            } else {
                alertErr("获取皮肤失败")
                log.error(response2.status)
            }
        } else {
            alertErr("找不到对应玩家")
            log.error(response.status)
        }
    }

    override fun init() {
        RGridLayout().apply {
            row(
                6,
                RButton("改QQ") {
                    mc goScreen formScreen(this@RProfileScreen, "换QQ") {
                        text("qq", "新QQ", 10, defaultValue = account.qq)
                        submit {
                            val qq = it.formData["qq"]!!
                            IhqClient.put("profile", listOf("qq" to qq)) {
                                showToast("成功修改qq为${qq}")
                                RAccount.now?.qq = qq
                                LocalStorage["usr"] = qq
                                mc goScreen RProfileScreen(RAccount.now!!)
                            }
                        }
                    }.build()
                },
                RButton("改密码") {
                    mc goScreen formScreen(this@RProfileScreen, "修改密码") {
                        pwd("opwd", "旧密码")
                        pwd("pwd", "新密码")
                        pwd("cpwd", "确认密码")
                        submit {
                            val opwd = it.formData["opwd"]!!
                            if (account.pwd != opwd) {
                                alertErr("旧密码错误")
                                return@submit
                            }
                            val pwd = it.formData["pwd"]!!
                            val cpwd = it.formData["cpwd"]!!
                            if (pwd != cpwd) {
                                alertErr("确认密码与密码不一致")
                                return@submit
                            }
                            IhqClient.put("profile", listOf("pwd" to pwd)) {
                                showToast("成功修改密码为${pwd}")
                                LocalStorage += "pwd" to pwd
                                RAccount.now?.pwd = pwd
                                LocalStorage["pwd"] = pwd
                                mc goScreen RProfileScreen(RAccount.now!!)
                            }
                        }
                    }.build()
                },
                RButton("皮肤披风") {
                    mc goScreen ROptionScreen(
                        this@RProfileScreen,
                        "从正版玩家导入" to { mc goScreen mojangSkinScreen },
                        "从皮肤站导入" to {},
                        "从图床导入" to {mc goScreen picServerSkinScreen}
                    )
                },
                RButton("改昵称") {
                    mc goScreen formScreen(this@RProfileScreen, "修改昵称") {
                        text("name", "昵称", 16, defaultValue = account.name)
                        submit {
                            val name = it.formData["name"]!!
                            IhqClient.put("profile", listOf("name" to name)) {
                                showToast("成功修改昵称为${name}")
                                RAccount.now?.name = name
                                mc goScreen RProfileScreen(RAccount.now!!)
                            }
                        }
                    }.build()
                },
                RButton("登出") {
                    confirm("真的要退出账号${account.name}吗？", this@RProfileScreen) {
                        RAccount.now = null
                        toastOk("成功退出登录！")
                        mc goScreen RTitleScreen()
                    }
                },
            )
        }.visitWidgets { registerWidget(it) }
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