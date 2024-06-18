package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.PlainTextButton

class RTextButton(
    x: Int, y: Int,
    val msg: String, onClick: (Button) -> Unit,

    ) : PlainTextButton(x, y, mc.font.width(msg), 20, mcText(msg), onClick, mc.font) {

        var text
            get() = msg
            set(value) {message= mcText(value)}
}