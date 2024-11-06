package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.hardware.*
import calebxzhou.rdi.text.richText
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.clickBrowse
import calebxzhou.rdi.util.clickCopy
import calebxzhou.rdi.util.toMcText
import net.minecraft.client.gui.GuiGraphics

class AboutScreen : RScreen("关于") {
    val rt = richText(10,20){
        icon("server")
        text("服务器硬件：wuhudsm66")
        ret()
        icon("plugin")
        text("Mod建议：ForiLuSa 谋士兔 Caragan J4ckTh3R1pper Juliiee")
        ret()
        icon("test")
        text("测试：wuhudsm66 狗查 Caragan Joon Alive")
        ret()
        icon("qq")
        text("QQ群：1095925708")
        ret()
        icon("github")
        text("GitHub：calebxzhou/rdi4")
        ret()
        icon("os")
        text("OS：${os}")
        ret()
        icon("cpu")
        text("CPU：${cpu}")
        ret()
        icon("gpu")
        text("GPU：${gpu}")
        ret()
        icon("ram")
        text("RAM: ${mem}")
        ret()
        icon("disp")
        text("显示:")
        ret()
        disp.forEach {
            text(it)
            ret()
        }
        fill()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        rt.render(guiGraphics)
    }
}