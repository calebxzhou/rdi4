package calebxzhou.rdi.ui.component

import calebxzhou.rdi.MOD_ID
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
    val imgRL = ResourceLocation(MOD_ID, "textures/gui/$imgPath")
    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        val pose = pGuiGraphics.pose()
        pose.pushPose()
        pose.translate(0f,0f,1f)
        pose.scale(0.20f,0.20f,1f)
        pose.translate(x.toFloat()*4,y.toFloat()*4,1f)
        pGuiGraphics.blit(imgRL, x, y, 0f, 0f, 64, 64,64,64)
        pose.popPose()
        pGuiGraphics.drawString(
            mcFont, text, x+20, y+3, if (isHoveredOrFocused)
                ChatFormatting.AQUA.color ?: 16777215
            else
                16777215
        )
    }
}