package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.Const
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.model.RServer
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.ui.general.confirm
import calebxzhou.rdi.ui.general.dialog
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcMainThread
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.server.packs.PackType.SERVER_DATA

class RProfileScreen(
    val account: RAccount,
    val server: RServer
    ): RScreen("我的信息") {
    override fun init() {
        gridLayout (this, hAlign = HAlign.CENTER){
            iconButton("start", text = "开始") {
                RSoundPlayer.stopAll()
                mcMainThread {
                    ConnectScreen.startConnecting(
                        this@RProfileScreen, mc, ServerAddress(server.ip, server.gamePort), server.mcData, false
                    )
                }
            }
            iconButton("basic_info", text = "修改信息") {

            }
            iconButton("clothes", text = "衣柜") {

            }
            iconButton("smp", text = "团队") {
                alert("开发中，2月前上线")
            }
        }
        super.init()
    }

    override fun onClose() {
       confirm ("确定要登出吗？"){
           RServer.now?.disconnect()
           RAccount.now?.logout()
       }
    }
}