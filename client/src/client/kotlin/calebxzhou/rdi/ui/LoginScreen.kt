package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.mixin.client.AMc
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.service.AccountService
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.util.*
import io.ktor.client.statement.*

class LoginScreen(private val prev: RScreen) : ROkCancelScreen(prev, "登录") {
    lateinit var qqBox: REditBox
    lateinit var pwdBox: RPasswordEditBox

    override fun onSubmit() {
        val usr = qqBox.value
        val pwd = pwdBox.value
        if (usr.isNullOrBlank() || pwd.isNullOrBlank()) {
            dialogErr("用户名或密码不可为空")
            return
        }
        IhqClient.get(
            "login", listOf(
                "usr" to usr,
                "pwd" to pwd
            )
        ) {
            val account = serdesJson.decodeFromString<RAccount>(it.bodyAsText())
            LocalStorage += "usr" to usr
            LocalStorage += "pwd" to pwd
            RAccount.now = account
            showToast("登录成功")
            mc goScreen RTitleScreen()
        }

    }

    override fun init() {
        qqBox = REditBox("QQ号/昵称", width / 2 - 50, 80, 100).also { registerWidget(it) }
        pwdBox = RPasswordEditBox("RDI密码", width / 2 - 50, 120, 100).also { registerWidget(it) }
        qqBox.value = LocalStorage["usr"]
        pwdBox.value = LocalStorage["pwd"]
        super.init()
    }


}