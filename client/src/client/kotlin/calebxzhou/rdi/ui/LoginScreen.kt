package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.ihq.net.protocol.general.OkCPacket
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.util.*
import io.ktor.client.statement.*

class LoginScreen(prev: RScreen) : ROkCancelScreen(prev, "登录"){
    lateinit var qqBox: REditBox
    lateinit var pwdBox: RPasswordEditBox

    override fun onSubmit() {

        IhqClient.get("login", listOf(
            "usr" to qqBox.value,
            "pwd" to pwdBox.value
        )){
            val account = serdesJson.decodeFromString<RAccount>(it.bodyAsText())
            RAccount.now = account
            popupInfo("登录成功，欢迎${account.name}！")
            mc goScreen IslandScreen()
        }


    }

    override fun init() {
        qqBox = REditBox("QQ号", width / 2 - 50, 80, 100).also { registerWidget(it) }
        pwdBox = RPasswordEditBox("RDI密码", width / 2 - 50, 120, 100).also { registerWidget(it) }
        qqBox.value = LocalStorage["qq"]
        pwdBox.value = LocalStorage["pwd"]
        super.init()
    }



    fun onResponse(packet: OkCPacket) {
        showToast(packet.msg)

        //mc goScreen  IslandScreen()
    }

}