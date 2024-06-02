package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.network.chat.Component

class RCheckbox(x: Int, y: Int, message: String) :
    Checkbox(x, y, mcFont().width(message)+50, 80, mcText(message), false) {
}