package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.util.dialogErr
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.showToast

class RChangeNameScreen(prevScreen: RScreen) : ROkCancelScreen(prevScreen,"修改游戏昵称") {
    lateinit var nameBox: REditBox
    override fun init() {
        nameBox = REditBox("昵称",width/2-50,height/2,100).also { registerWidget(it) }
        nameBox.value = RAccount.now?.name
        super.init()
    }
    override fun onSubmit() {
        val name = nameBox.value
        if(name.isNullOrBlank()){
            dialogErr("游戏昵称不能为空")
            return
        }
        if(name.length>24){
            dialogErr("游戏昵称过长")
            return
        }

        IhqClient.put("profile", listOf("name" to name)){
            showToast("成功修改昵称为${name}")
            RAccount.now?.name = name
            mc goScreen ProfileScreen(RAccount.now!!)
        }
    }
}