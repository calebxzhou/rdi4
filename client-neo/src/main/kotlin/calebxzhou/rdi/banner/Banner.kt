package calebxzhou.rdi.banner

import calebxzhou.rdi.banner.Banner.textNow
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

object Banner {
    var textNow: Component? = null

    fun renderGui(guiGraphics: GuiGraphics) {
        textNow?.let { textNow ->
            guiGraphics.matrixOp {
                it.translate(0.0, 44.0, 100.0)
                guiGraphics.fill(0, 0, mcUIWidth, 20, 0x66000000.toInt())
                guiGraphics.drawCenteredString(mcFont, textNow, mcUIWidth / 2, 6, WHITE)
            }
        }
    }

    fun reset() {
        textNow = null
    }
    //在其他UI界面上层显示
    fun renderScreen(guiGraphics: GuiGraphics) {
        renderGui(guiGraphics)
    }
}