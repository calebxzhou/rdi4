package calebxzhou.rdi.ui.layout

import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.LayoutElement

class RGridLayout(x:Int,y:Int) : GridLayout(x,y) {
    constructor() : this(mc.window.guiScaledWidth/2, mc.window.guiScaledHeight-30)
    init {
        defaultCellSetting().padding(2,4,2,0)
    }
    fun row(cols: Int,vararg elems: AbstractWidget){
        val row = createRowHelper(cols)

        elems.forEach { row.addChild(it) }
        arrangeElements()
        this.x -= width / 2
    }
}