package calebxzhou.rdi.uiguide

import calebxzhou.rdi.logger
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcUIHeight
import calebxzhou.rdi.util.mcUIWidth
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.Rect2i
import net.minecraft.world.inventory.Slot
import net.minecraftforge.client.event.ScreenEvent

/**
 * calebxzhou @ 2024-10-23 12:02
 */
//黑色遮罩强制点击区域
fun uiGuide(builder: UiGuide.Builder.() -> Unit) {
    UiGuide.Builder(mc.screen?.javaClass).apply(builder).start()
}

class UiGuide(
    val screenClass: Class<Screen>?,
    val rightClick: Boolean,
    val steps: List<Step>
) {
    private var stepIndex = 0
    val stepNow
        get() = steps.getOrNull(stepIndex)
    var areaNow: Rect2i? = null

    fun render(guiGraphics: GuiGraphics) {
        areaNow?.let { area ->
            guiGraphics.fill(0, 0, mcUIWidth, mcUIHeight, 0xaa000000.toInt())
            guiGraphics.fill(area.x, area.y, area.x + area.width, area.y + area.height, 0xaa00bb00.toInt())
        }
    }

    fun tick(screen: Screen) {
        stepNow?.let { step ->
            if (screen.javaClass != screenClass) {
                return
            }
            //更新区域
            step.areaSupplier(screen)?.let { area ->
                if (areaNow == null)
                    areaNow = area
            } ?: stop()

        } ?: stop()
    }

    fun onClick(e: ScreenEvent.MouseButtonPressed.Pre) : Boolean{
        val mx = (e.mouseX).toInt()
        val my = (e.mouseY).toInt()
        if (rightClick && e.button == InputConstants.MOUSE_BUTTON_LEFT)
            return false
        areaNow?.let { area ->
            //不允许点击绿框以外 的部分
            if ((mx in area.x..area.x + area.width)
                && (my in area.y..area.y + area.height)
            ) {
                next()
                return true
            }else return false
        }?:
        //没有区域限制时
        return true
    }

    fun next() {
        stepIndex++
        areaNow = null
    }

    fun start() {
        logger.info("已开始uiGuide $screenClass ${steps.size}steps")
        now = this
    }

    fun stop() {
        logger.info("已结束uiGuide $screenClass ${steps.size}steps")
        now = null
    }

    companion object {
        var now: UiGuide? = null
            private set
        val isOn
            get() = now!=null
    }

    data class Step(
        val areaSupplier: (Screen) -> Rect2i?,
    )

    class Builder(val screenClass: Class<Screen>?) {
        val steps = arrayListOf<Step>()
        var rightClick = false
        fun step(areaSupplier: (Screen) -> Rect2i?) {
            steps += Step(areaSupplier)
        }

        //背包里的空位(E键画面)
        fun airSlotInv() {
            slotScreen { _, it -> (it.index in 9..44) && !it.hasItem() }
        }

        //背包里的空位(任意容器画面)
        fun airSlotContainer() {
            slotScreen { screen, slot ->
                if(screen is AbstractContainerScreen<*>){
                    val invSlotEndIndex = screen.menu.slots.size-1
                    val invSlotStartIndex = screen.menu.slots.size - 4*9
                    !slot.hasItem() && slot.index in invSlotStartIndex..invSlotEndIndex
                }else
                    false
            }
        }

        fun slot(vararg indexes: Int) {
            indexes.forEach { index -> slotScreen { _, it -> it.index == index } }
        }

        fun slot(findCondition: (Slot) -> Boolean) {
            slotScreen { _, slot -> findCondition(slot) }
        }
        fun slotScreen(findCondition: (Screen, Slot) -> Boolean) {
            step { screen ->
                if (screen is AbstractContainerScreen<*>) {
                    val oX = screen.guiLeft
                    val oY = screen.guiTop
                    screen.menu.slots.find{slot -> findCondition(screen,slot)}?.let {
                        //给格子窜到ui的位置上
                        val x = oX + it.x
                        val y = oY + it.y
                        return@step Rect2i(x, y, 16, 16)
                    } ?: let {
                        logger.warn("$screen 找不到 $findCondition")
                        return@step null
                    }
                } else {
                    logger.warn("$screen 不是容器画面 无法slot find")
                    return@step null
                }
            }
        }

        fun widgets(vararg indexes: Int) {

            indexes.forEach { index ->
                steps += Step { screen ->
                    screen.children()
                        .filterIndexed { i, w ->
                            w is AbstractWidget && i == index
                        }
                        .firstOrNull()?.let {
                            (it as AbstractWidget).run {
                               // logger.info("找到${screenClass}的控件${index}=${this}")
                                Rect2i(x, y, width, height)
                            }
                        } ?: let {
                        logger.warn("找不到${screenClass}的控件${index}")
                        return@Step null
                    }
                }
            }
        }

        fun start() {

            UiGuide(screenClass, rightClick,steps).start()
        }

    }
    /*data class WidgetIndexStep(
        val index: Int
    ): Step()
    data class SlotIndexStep(
        val index: Int
    ): Step()
    data class SlotFindStep(
        val predicate: (Slot) -> Boolean
    ):Step()*/
}