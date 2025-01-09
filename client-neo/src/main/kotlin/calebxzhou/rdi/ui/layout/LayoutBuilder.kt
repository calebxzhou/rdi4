package calebxzhou.rdi.ui.layout

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RIconButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.renderItemStack
import calebxzhou.rdi.util.*
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripRenderHandler.pose
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

fun gridLayout(
    screen: RScreen,
    x: Int = mcUIWidth / 2,
    y: Int = mcUIHeight - 20,
    hAlign: HAlign = HAlign.LEFT,
    maxColumns: Int = 0,
    builder: GridLayoutBuilder.() -> Unit,
): GridLayout {
    return GridLayoutBuilder(screen,maxColumns, x, y,hAlign).apply(builder).build()
}

class GridLayoutBuilder(
    val screen: RScreen,
    val maxColumns: Int,
    val x: Int,
    val y: Int,
    val hAlign: HAlign
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

    fun iconButton(
        icon: String?=null,
        item: Item?=null,
        text: String = "",
        cpnt: MutableComponent = text.toMcText(),
        x: Int = 0,
        y: Int = 0,
        onClick: (Button) -> Unit
    ) = RIconButton(icon,item, text,cpnt, x, y, onClick).also{ children += it }


    fun build(): GridLayout{
        val layout = GridLayout(x, y)

        val rowHelper = layout.createRowHelper(if (maxColumns > 0) maxColumns else children.size)
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