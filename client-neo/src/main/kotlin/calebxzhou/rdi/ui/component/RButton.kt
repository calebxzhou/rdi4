package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.MutableComponent


open class RButton(
    text: MutableComponent,
    x: Int=0,
    y: Int=0,
    width: Int= mcTextWidthOf(text)+20,
    height: Int=20,
    onClick: (Button) -> Unit,
) : Button(x, y, width, height, text, onClick , { text.plainCopy() }) {



}