package calebxzhou.rdi.ui

import calebxzhou.rdi.exception.RGeneralException
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.*

class RegScreen(prevScreen: RScreen) : ROkCancelScreen(prevScreen, "注册"){

    lateinit var nameBox : REditBox
    lateinit var qqBox : REditBox
    lateinit var pwdBox : REditBox
    lateinit var cpwdBox : REditBox
    override fun init() {
        nameBox= REditBox("昵称", width/2-50, 40, 100).also { registerWidget(it) }
        qqBox= REditBox("QQ号", width/2-50, 80, 100).also { registerWidget(it) }
        pwdBox=  RPasswordEditBox("RDI密码", width/2-50, 120, 100).also { registerWidget(it) }
        cpwdBox=  RPasswordEditBox("确认密码", width/2-50, 160, 100).also { registerWidget(it) }
        super.init()
    }
    override fun onSubmit() {
        val name = nameBox.value
        val qq = qqBox.value
        val pwd = pwdBox.value
        val cpwd = cpwdBox.value
        if(name.isBlank()||qq.isBlank()||pwd.isBlank())
            throw RGeneralException("输入框不能为空")
        if(pwd != cpwd)
            throw RGeneralException("密码与确认密码不一致")
        if(!qq.isNumber() || qq.length !in 5 .. 10)
            throw RGeneralException("QQ号格式错误")
        if(name.length>16)
            throw RGeneralException("名称过长，不允许超过16字符")
        if(pwd.length !in 6..16)
            throw RGeneralException("密码长度必须是6到16位")
        //开始注册
        IhqClient.post("register", listOf(
            "pwd" to pwd,
            "qq" to qq,
            "name" to name,
        )){
            popupInfo("注册成功")
            LocalStorage += "usr" to qq
            LocalStorage += "pwd" to pwd
            onClose()
        }


    }

}