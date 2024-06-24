package calebxzhou.rdi.ui.layout

import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.ProfileScreen
import calebxzhou.rdi.ui.RChangePwdScreen
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout


class RLinearLayout(x:Int,y:Int,width:Int,height:Int,ort: Orientation) : LinearLayout(x,y,width,height,ort) {
    constructor(): this(0,0,0,0,Orientation.HORIZONTAL)
    fun elem(vararg elems: LayoutElement){
        elems.forEach { addChild(it) }
        arrangeElements()
    }
}
fun linearLayout(screen:RScreen,vararg elems: LayoutElement): RLinearLayout{
    val layout = RLinearLayout()
    layout.apply {
        elem(*elems)
    }.visitWidgets { screen.registerWidget(it) }
    return layout
}