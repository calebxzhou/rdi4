package calebxzhou.rdi.ui.component

import calebxzhou.rdi.log
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants.KEY_NUMPADENTER
import com.mojang.blaze3d.platform.InputConstants.KEY_RETURN
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen


fun formScreen(prevScreen: Screen, title: String, builder: RFormScreen.() -> Unit): RScreen {
    return RFormScreen(prevScreen, title).apply(builder).build()
}
data class RFormScreenSubmitHandler(val screen:Screen,val okBtn:RButton,val formData:Map<String, String>){
    fun finish(){
        okBtn.active=true
    }
}
class RFormScreen(val prev: Screen, val title: String) {
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

    fun pwd(id: String, label: String,defaultValue: String? = null,) {
        widgets += id to RPasswordEditBox(label,defaultValue=defaultValue)
    }
    fun checkbox(id: String, label: String) {
        widgets += id to RCheckbox(label)
    }

    fun submit(handler: (RFormScreenSubmitHandler)->Unit) {
        this.handler = (handler)
    }

    fun build(): RScreen {
        return object : RScreen(title) {
            lateinit var okBtn : RButton
            lateinit var cancelBtn : RButton
            override fun tick() {
                if (mc pressingKey KEY_RETURN || mc pressingKey KEY_NUMPADENTER) {
                    if (okBtn.visible && okBtn.active){
                        try {
                            onSubmit()
                        }
                        catch (e: Exception) {
                            alertErr("错误：$e")
                            e.printStackTrace()
                        }
                    }
                }
                if(okBtn.isHoveredOrFocused){
                    okBtn.active = true
                }
                super.tick()
            }
            override fun init() {
                okBtn = RButton(mcText("确定").withStyle(ChatFormatting.GREEN),width/2-50, height-20, 50, ) { onSubmit() }.also { registerWidget(it) }
                cancelBtn = RButton(mcText("取消"),width/2, height-20, 50, ) { onClose() }.also { registerWidget(it) }
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

            fun onSubmit() {
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
                                log.error("不支持的控件类型：${widget.javaClass.name}")
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