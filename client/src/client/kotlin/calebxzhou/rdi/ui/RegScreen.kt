package calebxzhou.rdi.ui

import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.ROkCancelScreen
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.screens.Screen

class RegScreen: ROkCancelScreen(RTitleScreen(), "注册"){
    lateinit var nameBox : REditBox
    lateinit var qqBox : REditBox
    lateinit var pwdBox : REditBox
    lateinit var cpwdBox : REditBox
    override fun init() {
        nameBox= REditBox("昵称", width/2-50, 40, 100).also { registerWidget(it) }
        qqBox= REditBox("QQ号", width/2-50, 80, 100).also { registerWidget(it) }
        pwdBox=  REditBox("密码", width/2-50, 120, 100).also { registerWidget(it) }
        cpwdBox=  REditBox("确认密码", width/2-50, 160, 100).also { registerWidget(it) }
        super.init()
    }
    override fun onSubmit() {
        TODO("Not yet implemented")
    }

}