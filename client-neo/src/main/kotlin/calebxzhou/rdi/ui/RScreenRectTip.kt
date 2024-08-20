package calebxzhou.rdi.ui

import calebxzhou.rdi.logger
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.world.inventory.Slot
import net.minecraftforge.client.event.ScreenEvent
import org.lwjgl.glfw.GLFW

typealias RectPoints = IntArray
fun rectTip(builder: RScreenRectTip.Builder.() -> Unit){
    RScreenRectTip.Builder().apply(builder).build()
}
//在界面上画一个绿色矩形,告诉玩家要点击的位置(教程用)
object RScreenRectTip {
    private var allRects = arrayListOf<RectPoints>()
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
    val isCompleted
        get() = rectIndex<0
    fun render(guiGraphics: GuiGraphics, screen: Screen) {


        guiGraphics.matrixOp {
            guiGraphics.renderOutline(x1, y1, width, height, GREEN)
        }
    }

    fun tick(screen: Screen) {


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
    class Builder(){
        var allRects = arrayListOf<RectPoints>()
        //按slot条件添加提示点
        fun slot(condition: (Slot) -> Boolean){
            val containerScreen = mc.screen as? AbstractContainerScreen<*> ?: let {
                logger.warn("screen=null || !container, cant set rect tip Predicate")
                return
            }
            val oX = containerScreen.guiLeft
            val oY = containerScreen.guiTop
            containerScreen.menu.slots.find(condition)?.let {
                //给格子窜到ui的位置上
                allRects += intArrayOf(oX + it.x, oY + it.y, 16, 16)
            }?: logger.warn("cant find slot  in $containerScreen")
        }
        //按slot index添加提示点
        fun slot(index: Int){
            val containerScreen = mc.screen as? AbstractContainerScreen<*> ?: let {
                logger.warn("screen=null || !container, cant set rect tip Predicate")
                return
            }
            val oX = containerScreen.guiLeft
            val oY = containerScreen.guiTop
            containerScreen.menu.slots.getOrNull(index)?.let {
                //给格子窜到ui的位置上
                allRects += intArrayOf(oX + it.x, oY + it.y, 16, 16)
            }?: logger.warn("cant find slot $index in $containerScreen")
        }
        fun widgets(vararg widgetIndexes: Int){
            mc.screen?.let {  screen -> screen.children()
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
            }?:let {
                logger.warn("screen=null, no set rect tip byWidgets")
            }
        }
        fun build(){
            RScreenRectTip .allRects = this.allRects
            rectIndex=0
        }
    }
}