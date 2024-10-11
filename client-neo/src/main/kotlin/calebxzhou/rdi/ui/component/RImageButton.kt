package calebxzhou.rdi.ui.component

import calebxzhou.rdi.MOD_ID
import calebxzhou.rdi.util.matrixOp
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

class RImageButton(
    val imgPath: String,
    val text: MutableComponent,
    x: Int = 0,
    y: Int = 0,
    width: Int = mcTextWidthOf(text) + 24,
    height: Int = 20,
    val onClick: (Button) -> Unit
) : RButton(text, x, y, width, height, onClick) {
    val imgRL = ResourceLocation(MOD_ID, "textures/gui/icons/$imgPath")
    override fun renderWidget(gg: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        gg.matrixOp {
            translate(0f,0f,1f)
            scale(0.20f,0.20f,1f)
            translate(x.toFloat()*4,y.toFloat()*4,1f)
            gg.blit(imgRL, x, y, 0f, 0f, 64, 64,64,64)
        }
        gg.matrixOp {
            translate(0f,0f,1f)
            scale(0.90f,0.90f,1f)
            translate(x.toFloat()/0.9f+18,y.toFloat()/0.9f+2.8f,1f)
            gg.drawString(
                mcFont, text,0,0,if (isHoveredOrFocused)
                    ChatFormatting.AQUA.color ?: 16777215
                else
                    16777215
            )
        }
    }
}