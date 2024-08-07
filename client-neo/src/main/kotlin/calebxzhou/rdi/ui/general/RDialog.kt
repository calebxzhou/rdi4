package calebxzhou.rdi.ui.general

import calebxzhou.rdi.ui.RMessageType
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import org.lwjgl.util.tinyfd.TinyFileDialogs


/**
 * Created on 2024-06-23,22:05.
 */
fun confirm(msg: String,
            prevScreen: Screen,yesHandler: () -> Unit){
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
    val screenNow = prevScreen ?: mc.screen
    if (isMcStarted && screenNow != null) {
        mc goScreen RDialog(msg, screenNow, diagType, msgType, yesHandler)
    } else {
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
    val prevScreen: Screen,
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
    var startY = mcUIHeight/2-lines.size*10
    override fun init() {
        okBtn = RButton(mcText("确定"),width / 2 - 50, mcUIHeight / 2 + 30, 50, ) { onYes() }.also { registerWidget(it) }
        cancelBtn = RButton( mcText("取消"),width / 2, mcUIHeight / 2 + 30, 50) { onNo() }.also { registerWidget(it) }
        if (diagType == RDialogType.ALERT) {
            cancelBtn.visible = false
            okBtn.x = width / 2 - 25
            okBtn.message = mcText("明白")
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
        guiGraphics.fill(
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
        )
        lines.forEachIndexed { i,line->
        drawTextAtCenter(guiGraphics, line, startY + i * 10)
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick)


    }


}
