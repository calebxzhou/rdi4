package calebxzhou.rdi.ui.component

import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.MutableComponent

class RImageButton(
    val imgPath: String,
    val text:MutableComponent,
    x: Int = 0,
    y: Int = 0,
    width: Int = mcTextWidthOf(text) +20,
    height: Int = 20,
    val onClick: (Button) -> Unit
) : RButton(text,x,y,width,height,onClick){
}