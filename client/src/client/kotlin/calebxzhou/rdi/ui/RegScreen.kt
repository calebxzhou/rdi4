package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.exception.RAccountException
import calebxzhou.rdi.net.RegisterC2SPacket
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.ROkCancelScreen
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.util.isNumber
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.Connection

class RegScreen: ROkCancelScreen(RTitleScreen(), "注册"){
    lateinit var nameBox : REditBox
    lateinit var qqBox : REditBox
    lateinit var pwdBox : REditBox
    lateinit var cpwdBox : REditBox
    override fun init() {
        nameBox= REditBox("昵称", width/2-50, 40, 100).also { registerWidget(it) }
        qqBox= REditBox("QQ号", width/2-50, 80, 100).also { registerWidget(it) }
        pwdBox=  RPasswordEditBox("密码", width/2-50, 120, 100).also { registerWidget(it) }
        cpwdBox=  RPasswordEditBox("确认密码", width/2-50, 160, 100).also { registerWidget(it) }
        super.init()
    }
    override fun onSubmit() {
        val name = nameBox.value
        val qq = qqBox.value
        val pwd = pwdBox.value
        val cpwd = cpwdBox.value
        if(name.isBlank()||qq.isBlank()||pwd.isBlank())
            throw RAccountException("输入框不能为空")
        if(pwd != cpwd)
            throw RAccountException("密码与确认密码不一致")
        if(!qq.isNumber() || qq.length !in 5 .. 10)
            throw RAccountException("QQ号格式错误")
        if(name.length>16)
            throw RAccountException("名称过长，不允许超过16字符")
        if(pwd.length !in 6..16)
            throw RAccountException("密码长度必须是6到16位")
        //todo check
        val conn = Connection.connectToServer(Const.SERVER_INET_ADDR,true)
        conn.send(RegisterC2SPacket(nameBox.value,qqBox.value,pwdBox.value))

    }

}