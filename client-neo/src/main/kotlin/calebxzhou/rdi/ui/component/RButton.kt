package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.checkerframework.checker.units.qual.m

//点按钮什么都不做
val BUTTON_ACTION_NONE: (Button) -> Unit = { btn: Button -> }

open class RButton(
    x: Int,
    y: Int,
    width: Int,
    private val msg: MutableComponent,
    onClick: ((Button) -> Unit)?,
) : Button(x, y, width, 20, msg, onClick ?: BUTTON_ACTION_NONE, { msg.plainCopy() }) {
    constructor(
        x: Int,
        y: Int,
        msg: String,
        onClick: (Button) -> Unit
    ) : this(x, y, mcText(msg), onClick)

    constructor(
        x: Int,
        y: Int,
        msg: MutableComponent,
        onClick: (Button) -> Unit
    ) : this(
        x - (mc.font.width(msg) + 20) / 2,
        y,
        mc.font.width(msg) + 20,
        msg,
        onClick
    )

    constructor(msg: String, onClick: (Button) -> Unit) : this(
        0, 0, mc.font.width(msg) + 20, mcText(msg), onClick
    )

}