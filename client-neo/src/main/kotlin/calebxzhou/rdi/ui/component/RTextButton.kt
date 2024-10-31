package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.MutableComponent
import java.awt.SystemColor.text

class RTextButton(
    var text: MutableComponent,
    x: Int=0,
    y: Int=0,
    onClick: (Button) -> Unit={},
) : RButton(
    text,
    x,
    y,
    width = mcTextWidthOf(text) + 5,
    onClick = onClick
) {
    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {

        guiGraphics.drawString(
            mcFont,
            message,
            x,
            y,
            if (isHoveredOrFocused)
                ChatFormatting.AQUA.color?:16777215
            else
            16777215
            //16777215 or (Mth.ceil(this.alpha * 255.0f) shl 24)
        )
    }

}