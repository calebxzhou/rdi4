package calebxzhou.rdi.ui

import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.ROkCancelScreen

class REditInfoScreen :ROkCancelScreen(RTitleScreen(),"修改个人信息"){
    lateinit var nameBox : REditBox
    lateinit var uuidBox : REditBox
    lateinit var skinBox : REditBox
    lateinit var capeBox : REditBox
    override fun onSubmit() {

    }
}