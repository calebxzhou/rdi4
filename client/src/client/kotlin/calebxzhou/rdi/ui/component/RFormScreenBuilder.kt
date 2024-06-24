package calebxzhou.rdi.ui.component

import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.util.drawTextAt
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen


fun formScreen(prevScreen: Screen, title: String, builder: RFormScreenBuilder.() -> Unit): RFormScreenBuilder {
    return RFormScreenBuilder(prevScreen, title).apply(builder)
}
typealias RFormScreenSubmitHandler = (Screen,Map<String, String>) -> Unit
class RFormScreenBuilder(val prevScreen: Screen, val title: String) {
    private val widgets = linkedMapOf<String, AbstractWidget>()
    private lateinit var submitHandler: RFormScreenSubmitHandler
    fun text(
        id: String, label: String, maxLength: Int,
        numberOnly: Boolean = false,
        defaultValue: String = "",
        validator: REditBoxValidator = REQUIRED_VALIDATOR
    ) {
        widgets += id to REditBox(label, maxLength, validator).apply {
            isNumberOnly = numberOnly
            value = defaultValue
        }
    }

    fun pwd(id: String, label: String) {
        widgets += id to RPasswordEditBox(label)
    }

    fun submit(handler: RFormScreenSubmitHandler) {
        this.submitHandler = (handler)
    }

    fun build(): ROkCancelScreen {
        return object : ROkCancelScreen(prevScreen, title) {
            override fun init() {
                var y = 50
                widgets.forEach {
                    val widget = it.value
                    widget.x = width / 2 - widget.width / 2
                    /*if (widget is REditBox) {
                        widget.x += 30
                    }*/
                    widget.y = y
                    registerWidget(it.value)
                    y += 30
                }
                super.init()
            }

            override fun onSubmit() {
                widgets.forEach { (id, widget) ->
                    if (widget is REditBox) {
                        val result = widget.validate()
                        if (!result.isSuccess) {
                            alertErr(result.reason,this)
                            return
                        }
                    }
                }
                //只要editbox
                val allEditBoxes = widgets.filter { it.value is REditBox }.map { it.key to (it.value as REditBox).value }
                    .toMap()
                submitHandler(this,allEditBoxes)
            }

            override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
                widgets.forEach { (id, widget) ->
                    if (widget is REditBox) {
                        //无内容->在输入框前边画label
                        if (widget.value.trim().isNotBlank() || widget.isFocused) {
                            drawTextAt(
                                guiGraphics,
                                widget.label + "：",
                                widget.x - 10 - mcTextWidthOf(widget.label),
                                widget.y + mc.font.lineHeight / 2
                            )
                        }
                    }
                }

            }
        }
    }
}