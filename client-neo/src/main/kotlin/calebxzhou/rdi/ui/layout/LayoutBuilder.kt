package calebxzhou.rdi.ui.layout

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RIconButton
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcUIHeight
import calebxzhou.rdi.util.mcUIWidth
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

fun gridLayout(
    x: Int = mcUIWidth / 2,
    y: Int = mcUIHeight - 20,
    maxColumns: Int = 0,
    builder: GridLayoutBuilder.() -> Unit,
): GridLayoutBuilder {
    return GridLayoutBuilder(maxColumns, x, y).apply(builder)
}

class GridLayoutBuilder(val maxColumns: Int, val x: Int, val y: Int) {
    val children = arrayListOf<LayoutElement>()

    fun button(text: String, onClick: (Button) -> Unit) {
        button(mcText(text), onClick)
    }

    fun button(text: MutableComponent, onClick: (Button) -> Unit) {
        children += RButton(text, onClick = onClick)
    }

    fun imageButton(iconPath: ResourceLocation, text: String, onClick: (Button) -> Unit) {
        imageButton(iconPath, mcText(text), onClick)
    }

    fun imageButton(iconPath: ResourceLocation, text: MutableComponent, onClick: (Button) -> Unit) {
        children += RIconButton(iconPath, text, onClick = onClick)
    }

    fun buildForIteration(consumer: (AbstractWidget) -> Unit) {
        val layout = GridLayout(x, y)

        val rowHelper = layout.createRowHelper(if (maxColumns > 0) maxColumns else children.size)
        children.forEach { rowHelper.addChild(it) }
        layout.arrangeElements()
      //  layout.x -= layout.width / 2
        layout.visitWidgets(consumer)
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