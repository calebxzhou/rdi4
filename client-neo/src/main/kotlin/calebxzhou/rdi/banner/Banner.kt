package calebxzhou.rdi.banner

import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.util.*
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object Banner {
    private var textNow: Component? = null
    private var displayTicks = 0
    private val isDisplaying
        get() = textNow != null
    private var delay = -1
    val cmd = Commands.literal("banner").then(
        Commands.argument("1", StringArgumentType.string())
            .executes {
                val com = StringArgumentType.getString(it, "1")
                Banner += mcText(com)
                1

            }
    )

    fun renderGui(guiGraphics: GuiGraphics) {
        if (!isDisplaying)
            return
        textNow?.let { textNow ->
            guiGraphics.matrixOp {
                translate(0.0, 24.0, 100.0)
                guiGraphics.fill(0, 0, mcUIWidth, 20, 0x66000000.toInt())
                guiGraphics.drawCenteredString(mcFont, textNow, mcUIWidth / 2, 6, WHITE)
            }
        }
    }

    fun reset() {
        textNow = null
        delay = -1
        displayTicks = 0
    }

    operator fun plusAssign(com: Component) {
        reset()
        textNow = com
    }
    //到达一定时间后自动reset
    operator fun plusAssign(comDelay: Pair<Component,Int>){
        reset()
        textNow = comDelay.first
        delay = comDelay.second
    }

    //在容器,背包UI界面上层显示
    fun renderScreen(guiGraphics: GuiGraphics, screen: Screen) {
        if (!isDisplaying)
            return
        if (screen is InventoryScreen || screen is AbstractContainerScreen<*>)
            renderGui(guiGraphics)
    }

    fun tick() {
        if (!isDisplaying)
            return
        if (delay > 0) {
            if (displayTicks >= delay) {
                reset()
            } else {
                displayTicks++
            }
        }
    }
}