package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcTextWidthOf
import calebxzhou.rdi.util.toMcText
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.MutableComponent


open class RButton(
    text: MutableComponent,
    x: Int = 0,
    y: Int = 0,
    width: Int = mcTextWidthOf(text) + 20,
    height: Int = 20,
    val hoverText: String = "",
    onClick: (Button) -> Unit,
) : Button(x, y, width, height, text, onClick, { text.plainCopy() }) {

    init {

        tooltip = hoverText.toMcText().let { Tooltip.create(it) }
    }

}