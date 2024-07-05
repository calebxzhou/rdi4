package calebxzhou.rdi.ui.component

import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.PlayerFaceRenderer

class RPlayerHeadButton(private val account: RAccount, x:Int=0, y:Int=0, handler: (Button)->Unit) : RButton(x,y,account.name,handler) {
    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        PlayerFaceRenderer.draw(guiGraphics, account.skinLocation, x, y, 16)
        val component = if (this.isHoveredOrFocused) mcText(account.name).withStyle(ChatFormatting.AQUA) else mcText(account.name)
        guiGraphics.drawString(mcFont,component,x+20,y+3,0xffffff)
    }
}