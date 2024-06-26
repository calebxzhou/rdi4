package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.network.chat.Component

class RCheckbox(message: String,x: Int=0, y: Int=0, ) :
    Checkbox(x, y, mcFont.width(message)+50, 20, mcText(message), false) {
}