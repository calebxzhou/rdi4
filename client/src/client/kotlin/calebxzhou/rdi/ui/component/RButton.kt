package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class RButton (x: Int, y: Int, width: Int, private val msg: String, onClick: (Button) -> Unit,

               ) : Button(x, y, width, 20, Component.literal(msg), onClick, { Component.literal(msg) }) {
                   constructor(x: Int, y: Int, msg: String, onClick: (Button) -> Unit):this(x-(mc.font.width(msg)+20)/2, y, mc.font.width(msg)+20, msg, onClick)
}