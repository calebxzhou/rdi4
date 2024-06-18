package calebxzhou.rdi.ui.component

import calebxzhou.rdi.log
import calebxzhou.rdi.mixin.client.AEditBox
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.pressingKey
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component

class RPasswordEditBox(label: String, x: Int, y: Int, width: Int) : REditBox(label, x, y, width) {
    init {
        setHint(Component.literal(label))
    }
    var passwordVisible = false

    override fun tick() {

        super.tick()


    }
    override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (!this.isVisible) return
        this as AEditBox
        // 按alt显示密码
        passwordVisible = isFocused && (mc pressingKey InputConstants.KEY_LALT || mc pressingKey  InputConstants.KEY_RALT)

        //边框
        guiGraphics.fill(
            this.x - 1,
            this.y - 1,
            this.x + this.width + 1,
            this.y + this.height + 1,
            if (this.isFocused) -1 else -6250336
        )
        guiGraphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216)
        //输入的字符全都换成星号
        val value = if(!passwordVisible) value.replace(Regex("."), "*") else value
        val j = this.cursorPos - this.displayPos
        var k = this.highlightPos - this.displayPos
        val string =
            font.plainSubstrByWidth(value.substring(this.displayPos), this.innerWidth)
        val bl = j >= 0 && j <= string.length
        val bl2 = this.isFocused && (this.frame / 6 % 2 == 0) && bl
        val l = this.x + 4
        val m = this.y + (this.height - 8) / 2
        var n = l
        if (k > string.length) {
            k = string.length
        }

        if (string.isNotEmpty()) {
            val string2 = if (bl) string.substring(0, j) else string
            n = guiGraphics.drawString(
                this.font,
                formatter.apply(string2, this.displayPos), l, m, 14737632
            )
        }

        val bl3 = this.cursorPos < value.length || value.length >= this.invokeGetMaxLength()
        var o = n
        if (!bl) {
            o = if (j > 0) l + this.width else l
        } else if (bl3) {
            o = n - 1
            --n
        }

        if (string.isNotEmpty() && bl && j < string.length) {
            guiGraphics.drawString(
                this.font,
                formatter.apply(string.substring(j), this.cursorPos), n, m, 14737632
            )
        }

        if (this.hint != null && string.isEmpty() && !this.isFocused) {
            guiGraphics.drawString(this.font, this.hint, n, m, 14737632)
        }


        if (bl2) {
            if (bl3) {
                guiGraphics.fill(RenderType.guiOverlay(), o, m - 1, o + 1, m + 1 + 9, -3092272)
            } else {
                guiGraphics.drawString(this.font, "_", o, m, 14737632)
            }
        }

        if (k != j) {
            val p = l + font.width(string.substring(0, k))
            this.invokeRenderHighlight(guiGraphics, o, m - 1, p - 1, m + 1 + 9)
        }
    }

}