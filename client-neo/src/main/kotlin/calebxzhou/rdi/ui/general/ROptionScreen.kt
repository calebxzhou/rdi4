package calebxzhou.rdi.ui.general

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.screens.Screen

/*
optionScreen{
    "1" to {
        print(1)
    }
"3" to {
        print(3)
    }
    "2" to {
        print(2)
    }
}
 */
typealias ROptionAction = (ROptionScreen) -> Unit

fun optionScreen(
    prev: Screen,
    title: String = "请选择操作", init: ROptionScreen.() -> Unit
): RScreen {
    return ROptionScreen(prev, title).also { it.init() }.mcScreen
}

class ROptionScreen(
    val prev: Screen,
    val title: String,
) {
    val options = linkedMapOf<String, ROptionAction>()

    infix fun String.to(action: ROptionAction) {
        options[this] = action
    }
    infix fun String.to(screen: Screen) {
        options[this] = {mc goScreen  screen}
    }

    val mcScreen
        get() = object : RScreen(title) {
            override fun init() {
                var startY = height / 2 - options.size * 10
                options.onEachIndexed { index, (text, opr) ->
                    val btn = RButton(mcText("${index + 1}. ${text}"), width / 2, startY) { opr(this@ROptionScreen) }
                    registerWidget(btn)
                    startY += btn.height
                }
                super.init()
            }

            override fun onClose() {
                mc goScreen prev
            }
        }
}
