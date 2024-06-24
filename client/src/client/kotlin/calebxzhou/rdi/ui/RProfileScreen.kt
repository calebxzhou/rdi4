package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.ROptionScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.ui.layout.RGridLayout
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.client.gui.screens.inventory.InventoryScreen

class RProfileScreen(val account: RAccount) : RScreen("个人信息管理") {


    override fun init() {
        RGridLayout().apply {
            row(
                5,
                RButton("改QQ") {
                    mc goScreen formScreen(this@RProfileScreen,"换QQ"){
                        text("qq","新QQ",10, defaultValue = account.qq)
                        submit { screen, inputs ->
                            val qq = inputs["qq"]!!
                            IhqClient.put("profile", listOf("qq" to qq)){
                                showToast("成功修改qq为${qq}")
                                RAccount.now?.qq = qq
                                LocalStorage["usr"] = qq
                                mc goScreen RProfileScreen(RAccount.now!!)
                            }
                        }
                    }.build()
                },
                RButton("改密码") {
                    mc goScreen formScreen(this@RProfileScreen,"修改密码"){
                        pwd("opwd","旧密码")
                        pwd("pwd","新密码")
                        pwd("cpwd","确认密码")
                        submit {screen, inputs ->
                            val opwd = inputs["opwd"]!!
                            if(account.pwd != opwd){
                                alertErr("旧密码错误")
                                return@submit
                            }
                            val pwd = inputs["pwd"]!!
                            val cpwd = inputs["cpwd"]!!
                            if(pwd != cpwd){
                                alertErr("确认密码与密码不一致")
                                return@submit
                            }
                            IhqClient.put("profile", listOf("pwd" to pwd)){
                                showToast("成功修改密码为${pwd}")
                                LocalStorage+= "pwd" to pwd
                                RAccount.now?.pwd = pwd
                                LocalStorage["pwd"] = pwd
                                mc goScreen RProfileScreen(RAccount.now!!)
                            }
                        }
                    }.build()
                },
                RButton("改皮肤") {
                    mc goScreen ROptionScreen(
                        this@RProfileScreen,
                        "从正版玩家导入" to {},
                        "从皮肤站导入" to {},
                        "从图床导入" to {}
                    )
                },
                RButton("改披风") {
                    mc goScreen ROptionScreen(
                        this@RProfileScreen,
                        "从正版玩家导入" to {},
                        "从皮肤站导入" to {},
                        "从图床导入" to {}
                    )
                },
                RButton("改昵称"){
                    mc goScreen formScreen(this@RProfileScreen,"修改昵称"){
                        text("name","昵称",16, defaultValue = account.name)
                        submit {screen, inputs ->
                            val name = inputs["name"]!!
                            IhqClient.put("profile", listOf("name" to name)){
                                showToast("成功修改昵称为${name}")
                                RAccount.now?.name = name
                                mc goScreen RProfileScreen(RAccount.now!!)
                            }
                        }
                    }.build()
                }
            )
        }.visitWidgets { registerWidget(it) }
        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        PlayerFaceRenderer.draw(guiGraphics, account.skinLocation, width / 2 - 10, height / 5, 20)
        drawTextAtCenter(guiGraphics, "昵称：${account.name}", height / 2-20)
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