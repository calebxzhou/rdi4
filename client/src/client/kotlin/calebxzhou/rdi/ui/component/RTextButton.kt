package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.util.Mth

class RTextButton(
    x: Int, y: Int,
    var msg: String, onClick: (Button) -> Unit,
    ) : RButton(x, y,mcTextWidthOf(msg)+5, msg,onClick) {
        constructor(msg: String,onClick: (Button) -> Unit):this(0,0,msg, onClick)
    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val component = if (this.isHoveredOrFocused) mcText(this.msg).withStyle(ChatFormatting.AQUA) else mcText(this.msg)
        guiGraphics.drawString(mcFont, component, this.x, this.y, 16777215 or (Mth.ceil(this.alpha * 255.0f) shl 24))

    }

}