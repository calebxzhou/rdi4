package calebxzhou.rdi.ui.component

import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class RButton (x: Int, y: Int, width: Int, private val msg: String, onClick: (Button) -> Unit,

               ) : Button(x, y, width, 20, Component.literal(msg), onClick, { Component.literal(msg) }) {
}