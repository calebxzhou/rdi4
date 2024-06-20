package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.util.*

class RChangePwdScreen(prevScreen: RScreen) : ROkCancelScreen(prevScreen,"修改密码") {
    lateinit var pwdBox: RPasswordEditBox
    override fun init() {
        pwdBox = RPasswordEditBox("密码",width/2-50,height/2,100).also { registerWidget(it) }
        pwdBox.value = RAccount.now?.pwd
        super.init()
    }
    override fun onSubmit() {
        val pwd = pwdBox.value
        if(pwd.isNullOrBlank()){
            dialogErr("密码不能为空")
            return
        }
        if(pwd.length>=16){
            dialogErr("密码过长 16位以内")
            return
        }

        IhqClient.put("profile", listOf("pwd" to pwd)){
            showToast("成功修改密码为${pwd}")
            LocalStorage+= "pwd" to pwd
            RAccount.now?.pwd = pwd
            mc goScreen ProfileScreen(RAccount.now!!)
        }
    }
}