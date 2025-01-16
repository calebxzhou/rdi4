package calebxzhou.rdi.ui.general

import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Tooltip
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
data class ROption(val text:String,val enabled: Boolean = true,val hoverText: String?=null,val action: (ROptionScreen) -> Unit)
fun optionScreen(title: String = "请选择", init: ROptionScreen.() -> Unit): RScreen {
    return ROptionScreen(mc.screen,title).apply(init).mcScreen
}
fun optionScreen(
    prev: Screen,
    title: String = "请选择操作", init: ROptionScreen.() -> Unit
): RScreen {
    return ROptionScreen(prev, title).also { it.init() }.mcScreen
}

class ROptionScreen(
    val prev: Screen?,
    val title: String,
) {
    val options = arrayListOf<ROption>()
    operator fun plusAssign(option: ROption){
        options += option
    }
    infix fun String.to(action: (ROptionScreen) -> Unit) {
        options += ROption(this, action = action)
    }
    infix fun String.to(screen: Screen) {
        options += ROption(this){mc go  screen}
    }

    val mcScreen
        get() = object : RScreen(title) {
            override var showTitle = false
            val startY = mcUIHeight / 2 - options.size * 10
            var nowY = startY
            override fun init() {
                nowY = startY
                options.onEachIndexed { index, option ->
                    val btn = RButton(mcText("${index + 1}. ${option.text}"), 0, nowY) { option.action(this@ROptionScreen) }
                    btn.x = width/2-btn.width/2
                    btn.tooltip = option.hoverText?.let { Tooltip.create(it.toMcText()) }
                    btn.active = option.enabled
                    registerWidget(btn)
                    nowY += btn.height
                }
                super.init()
            }

            override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
                guiGraphics.drawCenteredString(mcFont,this@ROptionScreen.title,width/2,startY-20, WHITE)
            }
            override fun tick() {

                if (mc pressingKey InputConstants.KEY_1) {
                    options.getOrNull(0)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_2) {
                    options.getOrNull(1)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_3) {
                    options.getOrNull(2)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_4) {
                    options.getOrNull(3)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_5) {
                    options.getOrNull(4)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_6) {
                    options.getOrNull(5)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_7) {
                    options.getOrNull(6)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_8) {
                    options.getOrNull(7)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_9) {
                    options.getOrNull(8)?.action?.invoke(this@ROptionScreen)
                }
                if (mc pressingKey InputConstants.KEY_0) {
                    options.getOrNull(9)?.action?.invoke(this@ROptionScreen)
                }
            }
            override fun onClose() {
                mc go prev
            }
        }
}
