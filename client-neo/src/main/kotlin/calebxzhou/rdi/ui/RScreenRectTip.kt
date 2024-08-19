package calebxzhou.rdi.ui

import calebxzhou.rdi.ui.screen.RTitleScreen
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.world.inventory.Slot
import net.minecraftforge.client.event.ScreenEvent
import org.lwjgl.glfw.GLFW
import kotlin.math.abs

typealias RectPoints = IntArray

//在界面上画一个绿色矩形,告诉玩家要点击的位置(教程用)
object RScreenRectTip {
    var allRects = arrayListOf<RectPoints>()
    var rectIndex = -1
    val rectNow: RectPoints
        get() = allRects.getOrNull(rectIndex)?:RectPoints(4)
    val x1
        get() = rectNow[0]
    val y1
        get() = rectNow[1]
    val width
        get() = rectNow[2]
    val height
        get() = rectNow[3]
    val hasNo
        get() = rectIndex<0
    var lastScreen: Screen = RTitleScreen()
    fun render(guiGraphics: GuiGraphics, screen: Screen) {


        guiGraphics.matrixOp {
            guiGraphics.renderOutline(x1, y1, width, height, GREEN)
        }
    }

    fun tick(screen: Screen) {
        lastScreen = screen

    }

    //给一堆控件index,获取坐标
    fun byWidgets(vararg widgetIndexes: Int) {
        lastScreen.children()
            .filterIndexed { index, widget -> index in widgetIndexes && widget is AbstractWidget }
            .mapIndexedNotNullTo(allRects) { index, widget ->
                (widget as AbstractWidget).run {
                    intArrayOf(
                        x,
                        y,
                        width,
                        height
                    )
                }
            }
        rectIndex=0
    }
    /*fun bySlotIndex(slotIndex: Int){
            lastMenuSlots.getOrNull(slotIndex)?.let {
                position = it.x to it.y
            }
    }*/

    fun bySlotPredicate(pred: (Slot) -> Boolean) {
        val containerScreen = lastScreen as? AbstractContainerScreen<*> ?: return
        val oX = containerScreen.guiLeft
        val oY = containerScreen.guiTop
        containerScreen.menu.slots.find(pred)?.let {
            //给格子窜到ui的位置上
            allRects += intArrayOf(oX + it.x, oY + it.y, 16, 16)
        }
        rectIndex=0
    }

    fun reset() {
        allRects.clear()
        rectIndex = -1
    }

    //点完以后把绿块去了
    fun onClick(e: ScreenEvent.MouseButtonPressed.Post) {
        val mx = (e.mouseX).toInt()
        val my = (e.mouseY).toInt()
        if (e.button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return

        if ((mx in x1..x1 + width) && (my in y1..y1 + height)) {
            if (allRects.getOrNull(rectIndex + 1) != null) {
                rectIndex++
            }else{
                reset()
            }
        }


    }
}