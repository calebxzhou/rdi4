package calebxzhou.rdi.ui.layout

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RIconButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcUIHeight
import calebxzhou.rdi.util.mcUIWidth
import calebxzhou.rdi.util.toMcText
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
fun GridLayout.clear(){
}

fun gridLayout(
    screen: RScreen,
    x: Int = mcUIWidth / 2,
    y: Int = mcUIHeight - 20,
    hAlign: HAlign = HAlign.LEFT,
    maxColumns: Int = 0,
    rowSpacing: Int=0,
    colSpacing: Int=0,
    builder: GridLayoutBuilder.() -> Unit,
): GridLayout {
    return GridLayoutBuilder(screen,maxColumns, x, y,hAlign,rowSpacing,colSpacing).apply(builder).build()
}

class GridLayoutBuilder(
    val screen: RScreen,
    val maxColumns: Int,
    val x: Int,
    val y: Int,
    val hAlign: HAlign,
    val rowSpacing: Int=0,
    val colSpacing: Int=0,
) {
    val children = arrayListOf<LayoutElement>()

    fun widget(widget: LayoutElement){
        children += widget
    }
    fun button(text: String, onClick: (Button) -> Unit) {
        button(mcText(text), onClick)
    }

    fun button(text: MutableComponent, onClick: (Button) -> Unit) {
        children += RButton(text, onClick = onClick)
    }

    fun button(
        icon: String?=null,
        item: Item?=null,
        text: String = "",
        comp: MutableComponent = text.toMcText(),
        hoverText: String = "",
        x: Int = 0,
        y: Int = 0,
        size: Int = 64,
        onClick: (Button) -> Unit = {}
    ) = RIconButton(icon,item, text,comp, hoverText,x, y, size,onClick).also{ children += it }


    fun build(): GridLayout{
        val layout = GridLayout(x, y)

        val rowHelper = layout.createRowHelper(if (maxColumns > 0) maxColumns else children.size)
        layout.rowSpacing(rowSpacing)
        layout.columnSpacing(colSpacing)
        children.forEach { rowHelper.addChild(it) }
        layout.arrangeElements()
        when(hAlign){
            HAlign.LEFT -> {}
            HAlign.RIGHT -> layout.x += layout.width / 2
            HAlign.CENTER -> layout.x -= layout.width / 2
        }

        layout.visitWidgets { screen.registerWidget(it) }
        return  layout
    }

}

class LayoutBuilder(val layout: Layout) {
    val children = arrayListOf<LayoutElement>()
    fun button(iconPath: String? = null, text: MutableComponent, onClick: (Button) -> Unit) {
        if (iconPath == null) {

            children += RButton(text, onClick = onClick)
        } else {
           // children += RIconButton(iconPath, text, onClick = onClick)
        }
    }

    fun build() {
        if (layout is GridLayout) {
            val row = layout.createRowHelper(children.size)
            children.forEach { row.addChild(it) }
        }
        if (layout is LinearLayout) {

            children.forEach { layout.addChild(it) }
        }

    }
}