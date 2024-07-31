package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.Mth

class RTextButton(
    x: Int,
    y: Int,
    //string变component 方便加粗等操作
    var msg: MutableComponent,
    val onClick: ((Button) -> Unit)?,
) : RButton(x, y, mcTextWidthOf(msg) + 5, msg, onClick) {

    constructor(msg: String, onClick: (Button) -> Unit) : this(0, 0, mcText (msg), onClick)
    constructor(msg:MutableComponent, onClick: ((Button) -> Unit)?=null) : this(0, 0, msg, onClick)

    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        //有点击事件 = 鼠标放上去变蓝色
        if (onClick != null) {
            guiGraphics.drawString(
                mcFont,
                if (this.isHoveredOrFocused) this.msg.withStyle(ChatFormatting.AQUA) else this.msg,
                this.x,
                this.y,
                16777215 or (Mth.ceil(this.alpha * 255.0f) shl 24)
            )
        }

    }

}