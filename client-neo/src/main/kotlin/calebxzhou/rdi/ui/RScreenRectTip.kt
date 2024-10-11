package calebxzhou.rdi.ui

import calebxzhou.rdi.logger
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.world.inventory.Slot
import net.minecraftforge.client.event.ScreenEvent
import org.lwjgl.glfw.GLFW

typealias RectPoints = IntArray
fun rectTip( builder: RScreenRectTip.Builder.() -> Unit){
    RScreenRectTip.Builder().apply(builder).build()
}
//在界面上画一个绿色矩形,告诉玩家要点击的位置(教程用)
object RScreenRectTip {
    enum class Mode(val color: Int,val btnCode: Int) {LMB(GREEN,GLFW.GLFW_MOUSE_BUTTON_LEFT ),RMB(RED,GLFW.GLFW_MOUSE_BUTTON_RIGHT)}
    private var allRects = arrayListOf<RectPoints>()
    var rectIndex = -1
    var mode=Mode.LMB
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
            guiGraphics.renderOutline(x1, y1, width, height, mode.color)
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
        if (e.button == mode.btnCode) {
            if ((mx in x1..x1 + width) && (my in y1..y1 + height)) {
                if (allRects.getOrNull(rectIndex + 1) != null) {
                    rectIndex++
                }else{
                    reset()
                }
            }
        }
    }
    class Builder(){
        var allRects = arrayListOf<RectPoints>()
        var mode = Mode.LMB
        //背包里的空位(任意容器画面)
        fun emptyInvSlotContainer(){
            val screen = mc.screen
            if(screen is AbstractContainerScreen<*>){
                //把容器里的空位跳过去 只要背包的
                val invSlotEndIndex = screen.menu.slots.size-1
                val invSlotStartIndex = screen.menu.slots.size - 4*9
                slot { !it.hasItem() && it.slotIndex in invSlotStartIndex..invSlotEndIndex }
            }else{
                logger.warn("not container screen!")
            }
        }
        //背包里的空位(E键画面)
        fun emptyInvSlot(){
            val screen = mc.screen
            if(screen is InventoryScreen){
                slot { (it.slotIndex in 9..35) && !it.hasItem()    }
            }else
            {
                logger.warn("not inv screen!")
            }
        }
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
                val x = oX + it.x
                val y = oY + it.y
                allRects += intArrayOf(x, y, 16, 16)
                logger.info("found slot index=${it.slotIndex}. x${x}y${y}")
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
                val x = oX + it.x
                val y = oY + it.y
                allRects += intArrayOf(x, y, 16, 16)
                logger.info("found slot index=${it.slotIndex}. x${x}y${y}")
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
            RScreenRectTip.mode=mode
            rectIndex=0
        }
    }
}