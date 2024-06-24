package calebxzhou.rdi.ui.component

import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.util.dialogErrMc
import calebxzhou.rdi.util.drawTextAt
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcTextWidthOf
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen


/*
DSL
formScreen("注册"){
    inputText("name","昵称",32)
    inputNum("qq","QQ号",10)
    inputPwd("pwd","密码")
    inputPwd("cpwd","确认密码")
    submit{ inputs ->
        IhqClient.post("register", listOf(
            "pwd" to inputs["pwd"],
            "qq" to inputs["qq"],
            "name" to inputs["name"],
        )){
            toastOk("注册成功")
            LocalStorage += "usr" to qq
            LocalStorage += "pwd" to pwd
            onClose()
        }
    }
}
 */
fun formScreen(prevScreen: Screen, title: String, builder: RFormScreenBuilder.() -> Unit): RFormScreenBuilder {
    return RFormScreenBuilder(prevScreen, title).apply(builder)
}

class RFormScreenBuilder(val prevScreen: Screen, val title: String) {
    private val widgets = linkedMapOf<String, AbstractWidget>()
    private lateinit var submitHandler: (Map<String, String>) -> Unit
    fun text(
        id: String, label: String, maxLength: Int, numberOnly: Boolean = false,
        validator: REditBoxValidator = REQUIRED_VALIDATOR
    ) {
        widgets += id to REditBox(label, maxLength, validator).apply { isNumberOnly = numberOnly }
    }

    fun pwd(id: String, label: String) {
        widgets += id to RPasswordEditBox(label)
    }

    fun submit(handler: (Map<String, String>) -> Unit) {
        this.submitHandler = (handler)
    }

    fun build(): ROkCancelScreen {
        return object : ROkCancelScreen(prevScreen, title) {
            override fun init() {
                var y = 50
                widgets.forEach {
                    val widget = it.value
                    widget.x = width / 2 - widget.width / 2
                    if (widget is REditBox) {
                        widget.x += mcTextWidthOf(widget.label)
                    }
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
                            dialogErrMc(result.reason)
                            return
                        }
                    }
                }
                //只要editbox
                submitHandler(widgets.filter { it.value is REditBox }.map{ it.key to (it.value as REditBox).value}.toMap())
            }

            override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
                widgets.forEach { (id, widget) ->
                    if (widget is REditBox) {
                        //无内容->在输入框前边画label
                        if (widget.value.isNotBlank()) {
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