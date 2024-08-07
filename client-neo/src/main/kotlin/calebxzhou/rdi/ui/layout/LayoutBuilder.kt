package calebxzhou.rdi.ui.layout

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RImageButton
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import kotlinx.coroutines.NonCancellable.children
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.MutableComponent

fun gridLayout(
    maxColumns: Int = 0,
    x: Int = mc.window.guiScaledWidth / 2,
    y: Int = mc.window.guiScaledHeight - 30,
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

    fun imageButton(iconPath: String, text: String, onClick: (Button) -> Unit) {
        imageButton(iconPath, mcText(text), onClick)
    }

    fun imageButton(iconPath: String, text: MutableComponent, onClick: (Button) -> Unit) {
        children += RImageButton(iconPath, text, onClick = onClick)
    }

    fun buildForIteration(consumer: (AbstractWidget) -> Unit) {
        val layout = GridLayout(x, y)

        val rowHelper = layout.createRowHelper(if (maxColumns > 0) maxColumns else children.size)
        children.forEach { rowHelper.addChild(it) }
        layout.arrangeElements()
        layout.visitWidgets(consumer)
    }
}

class LayoutBuilder(val layout: Layout) {
    val children = arrayListOf<LayoutElement>()
    fun button(iconPath: String? = null, text: MutableComponent, onClick: (Button) -> Unit) {
        if (iconPath == null) {

            children += RButton(text, onClick = onClick)
        } else {
            children += RImageButton(iconPath, text, onClick = onClick)
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