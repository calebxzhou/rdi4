package calebxzhou.rdi.ui.component

import calebxzhou.craftcone.utils.Gl
import calebxzhou.rdi.util.PINE_GREEN
import calebxzhou.rdi.util.WHITE
import calebxzhou.rdi.util.drawTextAtCenter
import calebxzhou.rdi.util.mcFont
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

abstract class RScreen(private val name: String) : Screen(Component.literal(name)) {
    open var clearColor = true
    open var showTitle = true
    open var showCloseButton = true
    private val widgets = arrayListOf<AbstractWidget>()

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if (clearColor) {

            Gl.clearColor(PINE_GREEN)
        }
        if (showTitle) {

            drawTextAtCenter(guiGraphics, name, 10)
        }
        doRender(guiGraphics, mouseX, mouseY, partialTick)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    open fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {}
    override fun init() {
        super.init()
        if(showCloseButton){
            widgets += RTextButton(5,5,"←返回/Esc"){onClose()}
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
}