package calebxzhou.rdi.ui.general

import calebxzhou.rdi.ui.RMessageType
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.RTextButton
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.ResourceLocation
import org.checkerframework.checker.units.qual.m
import org.lwjgl.util.tinyfd.TinyFileDialogs


/**
 * Created on 2024-06-23,22:05.
 */
fun confirm(msg: String,
            prevScreen: Screen?=null,yesHandler: () -> Unit){
    dialog(msg, prevScreen,RDialogType.CONFIRM, yesHandler = yesHandler)
}
fun alertInfo(msg: String, prevScreen: Screen?=null) {
    dialog(msg, prevScreen, msgType = RMessageType.INFO)
}
fun alertWarn(msg: String, prevScreen: Screen?=null) {
    dialog(msg, prevScreen, msgType = RMessageType.WARN)
}
fun alertErr(msg: String, prevScreen: Screen?=null) {
    dialog(msg, prevScreen, msgType = RMessageType.ERR)
}
fun alertOk(msg: String, prevScreen: Screen?=null) {
    dialog(msg, prevScreen, msgType = RMessageType.OK)
}

fun dialog(
    msg: String,
    prevScreen: Screen?=null,
    diagType: RDialogType = RDialogType.ALERT,
    msgType: RMessageType = RMessageType.INFO,
    yesHandler: () -> Unit = {},
) {
    if (isMcStarted && (mc.level != null || prevScreen != null)) {
        mc goScreen RDialog(msg, prevScreen, diagType, msgType, yesHandler)
    } else {
        //游戏未启动 显示操作系统提示框
        when (diagType) {
            RDialogType.ALERT -> {
                TinyFileDialogs.tinyfd_messageBox("RDI提示", msg, "ok", "info", true)
            }

            RDialogType.CONFIRM -> {
                if (TinyFileDialogs.tinyfd_messageBox("RDI提示", msg, "yesno", "question", false)) {
                    yesHandler()
                }
            }
        }
    }
}

enum class RDialogType {
    CONFIRM, ALERT
}

class RDialog(
    val msg: String,
    val prevScreen: Screen?,
    val diagType: RDialogType = RDialogType.ALERT,
    val msgType: RMessageType = RMessageType.INFO,
    val yesHandler: () -> Unit = {},
) : RScreen("提示") {
    override var clearColor = false
    override var closeable = false
    override var showTitle = false
    lateinit var okBtn: RButton
    lateinit var cancelBtn: RButton
    val lines = msg.split("\n")
    val icon = when (msgType) {
        RMessageType.INFO -> Icons["info"]
        RMessageType.ERR -> Icons["error"]
        RMessageType.OK -> Icons["success"]
        RMessageType.WARN -> Icons["warning"]
    }
    override val title = mcText(when (msgType) {
        RMessageType.INFO -> "提示"
        RMessageType.ERR -> "错误"
        RMessageType.OK -> "成功"
        RMessageType.WARN -> "警告"
    })
    var startY = mcUIHeight/2-lines.size*10
    override fun init() {
        okBtn = RTextButton(mcText("确定"),width / 2 - 50, mcUIHeight / 2 + 40, ) { onYes() }.also { registerWidget(it) }
        cancelBtn = RTextButton( mcText("取消"),width / 2, mcUIHeight / 2 + 40,) { onNo() }.also { registerWidget(it) }
        if (diagType == RDialogType.ALERT) {
            cancelBtn.visible = false
            okBtn.x = width / 2 - mcFont.width(">明白<")/2
            okBtn.message = mcText(">明白<")
        }

        super.init()

    }

    private fun onYes() {
        if (diagType == RDialogType.ALERT) {
            mc goScreen prevScreen
        } else if (diagType == RDialogType.CONFIRM) {
            yesHandler()
        }
    }

    private fun onNo() {
        mc goScreen prevScreen
    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.matrixOp {
            scale(0.5f,0.5f,1f)
            translate(mcUIWidth/2f,mcUIHeight/2f,1f)
            renderDirtBackground(guiGraphics)
        }
        guiGraphics.matrixOp {
            val size = 14
            val x = mcUIWidth/2-105
            val y = mcUIHeight/2-58
            guiGraphics.drawString(mcFont,title,x+size+2, y+3, WHITE)
            guiGraphics.blit(icon, x, y, 0f, 0f, size,size,size,size)
        }
        lines.forEachIndexed { i,line->
            drawTextAtCenter(guiGraphics, line, startY + i * 10)
        }
       /* guiGraphics.fill(
            mcUIWidth / 2 - 100,
            mcUIHeight / 2 - 50,
            mcUIWidth / 2 + 100,
            mcUIHeight / 2 + 50,
            OLIVE_GREEN
        )
        guiGraphics.renderOutline(
            mcUIWidth / 2 - 100, mcUIHeight / 2 - 50, 200, 100, when (msgType) {
                RMessageType.INFO -> KLEIN_BLUE
                RMessageType.ERR -> LIGHT_RED
                RMessageType.OK -> LIGHT_GREEN
                RMessageType.WARN -> LIGHT_YELLOW
            }
        )*/

        super.render(guiGraphics, mouseX, mouseY, partialTick)


    }


}
