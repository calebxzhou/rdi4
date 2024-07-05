package calebxzhou.rdi.ui

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.util.*

class RSkinCapeScreen(prevScreen: RScreen) : ROkCancelScreen(prevScreen,"皮肤披风设定") {
    lateinit var skinBox: REditBox
    lateinit var capeBox: REditBox
    override fun init() {
        skinBox = REditBox("皮肤",width/2-200,height/3,400).also { registerWidget(it) }
        capeBox = REditBox("披风",width/2-200,height/2,400).also { registerWidget(it) }
        skinBox.value = RAccount.now?.skin
        capeBox.value = RAccount.now?.cape
        super.init()
    }
    override fun onSubmit() {
        val skin = skinBox.value
        val cape = capeBox.value
        if(!skin.endsWith(".png")||!cape.endsWith(".png")||!skin.isValidHttpUrl()||!cape.isValidHttpUrl()){
            alertErr("皮肤/披风格式错误，你需要填写图床链接\nhttp开头，.png结尾")
            return
        }

    }
}