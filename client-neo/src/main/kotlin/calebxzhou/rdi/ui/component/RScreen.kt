package calebxzhou.rdi.ui.component

import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.util.drawTextAtCenter
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.MutableComponent

abstract class RScreen(open val title: MutableComponent) : Screen(title) {
    constructor(name: String) : this(mcText(name))
    open var clearColor = true
    open var showTitle = true
    open var closeable = true

    private val widgets = arrayListOf<AbstractWidget>()

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (clearColor) {
            renderBackground(guiGraphics)
        }
        if (showTitle) {
            drawTextAtCenter(guiGraphics, title, 5)
        }
        doRender(guiGraphics, mouseX, mouseY, partialTick)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    open fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {}
    override fun init() {
        super.init()
        if(closeable){
            widgets += RIconButton(icon = "back"){onClose()}
        }
        widgets.forEach { addRenderableWidget(it) }
    }

    fun registerWidget(widget: AbstractWidget) {
        widgets += widget
    }
    operator fun plusAssign(widget: AbstractWidget) {
        widgets += widget
    }

    override fun clearWidgets() {
        widgets.clear()
        super.clearWidgets()
    }

    override fun shouldCloseOnEsc(): Boolean {
        return closeable
    }
}