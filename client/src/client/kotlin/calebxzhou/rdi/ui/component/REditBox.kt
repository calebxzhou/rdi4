package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcFont
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component

open class REditBox(label: String, x: Int, y: Int, width: Int) : EditBox(mcFont(), x, y, width, 20, Component.literal(label)) {
    init {
        setHint(Component.literal(label))
    }
}