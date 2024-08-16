package calebxzhou.rdi.banner

import calebxzhou.rdi.util.WHITE
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcUIHeight
import calebxzhou.rdi.util.mcUIWidth
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

object Banner {
    var textNow: Component? = null

    fun render(guiGraphics: GuiGraphics) {
        textNow?.let { textNow ->
            guiGraphics.fill(0, 44, mcUIWidth, 64, 0x66000000.toInt())
            guiGraphics.drawCenteredString(mcFont, textNow, mcUIWidth / 2, 50, WHITE)
        }
    }
}