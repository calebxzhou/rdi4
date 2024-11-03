package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.text.richText
import calebxzhou.rdi.ui.component.RScreen
import net.minecraft.client.gui.GuiGraphics

class AboutScreen : RScreen("关于") {
    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        richText(10,20){
            icon("server")
            text("服务器硬件：wuhudsm66")
            ret()
            icon("plugin")
            text("Mod建议：ForiLuSa 普通人 Caragan J4ckTh3R1pper")
            ret()
            icon("test")
            text("测试：wuhudsm66 狗查 Caragan")
            ret()
            icon("qq")
            text("QQ群：1095925708")
            ret()
            icon("github")
            text("GitHub：calebxzhou/rdi4")

        }.render(guiGraphics)
    }
}