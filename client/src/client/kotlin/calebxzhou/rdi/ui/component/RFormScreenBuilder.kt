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
data class RFormScreenSubmitHandler(val screen:Screen,val okBtn:RButton,val formData:Map<String, String>){
    fun finish(){
        okBtn.active=true
    }
}
class RFormScreenBuilder(val prevScreen: Screen, val title: String) {
    private val widgets = linkedMapOf<String, AbstractWidget>()
    private lateinit var handler: (RFormScreenSubmitHandler)->Unit
    fun text(
        id: String,
        label: String,
        maxLength: Int,
        numberOnly: Boolean = false,
        nullable: Boolean = false,
        defaultValue: String? = null,
        validator: REditBoxValidator = if(nullable)DEFAULT_VALIDATOR else REQUIRED_VALIDATOR
    ) {
        widgets += id to REditBox(label, maxLength, validator).apply {
            isNullable = nullable
            isNumberOnly = numberOnly
            value = defaultValue
        }
    }

    fun pwd(id: String, label: String) {
        widgets += id to RPasswordEditBox(label)
    }
    fun checkbox(id: String, label: String) {
        widgets += id to RCheckbox(label)
    }

    fun submit(handler: (RFormScreenSubmitHandler)->Unit) {
        this.handler = (handler)
    }

    fun build(): ROkCancelScreen {
        return object : ROkCancelScreen(prevScreen, title) {
            override fun init() {
                var y = 50
                widgets.forEach {
                    val widget = it.value
                    widget.x = width / 2 - widget.width / 2
                    widget.y = y
                    registerWidget(it.value)
                    y += 30
                }
                super.init()
            }

            override fun onSubmit() {
                okBtn.active = false
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
                val formData = widgets
                    //.filter { it.value is REditBox }
                    .map { it.key to it.value.let { widget ->
                        when (widget) {
                            is REditBox -> widget.value.trim()
                            is RCheckbox -> {
                                widget.selected().toString()
                            }
                            else -> {
                                ""
                            }
                        }
                    }
                    }
                    .toMap()
                handler(RFormScreenSubmitHandler(this,okBtn, formData))
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