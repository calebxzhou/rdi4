package calebxzhou.rdi.ui.general

import ca.weblite.objc.RuntimeUtils.msg
import calebxzhou.rdi.logger
import calebxzhou.rdi.ui.RMessageLevel
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RDocWidget
import calebxzhou.rdi.ui.component.RIconButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.docWidget
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import org.lwjgl.util.tinyfd.TinyFileDialogs


/**
 * Created on 2024-06-23,22:05.
 */
fun alertOs(msg: String){
    TinyFileDialogs.tinyfd_messageBox("RDI提示", msg, "ok", "info", true)
}
fun alertErr(msg: String){
    dialog(msg.toMcText(), msglvl = RMessageLevel.ERR)
}
fun alert(msg: String){
    dialog(msg.toMcText(),)
}
fun dialog(
    msgBuilder: MutableComponent,
    diagType: RDialogType = RDialogType.ALERT,
    msglvl: RMessageLevel = RMessageLevel.INFO,
    yesText: String = if (diagType == RDialogType.ALERT) "明白" else "是",
    noText: String = "否",
    onYes: () -> Unit = {},
    onNo: () -> Unit = {},
) {
    val title = mcText(
        when (msglvl) {
            RMessageLevel.INFO -> "提示"
            RMessageLevel.ERR -> "错误"
            RMessageLevel.OK -> "成功"
            RMessageLevel.WARN -> "警告"
        }
    )
    mc goScreen RDialog( mc.screen,title, diagType, msglvl, msgBuilder,yesText,noText,onYes,onNo)
}
enum class RDialogType {
    CONFIRM, ALERT
}

class RDialog(
    val prevScreen: Screen?,
    override val title: MutableComponent,
    val diagType: RDialogType,
    val msglvl: RMessageLevel,
    val msg: MutableComponent,
    val yesText: String ,
    val noText: String,
    val onYes: () -> Unit ,
    val onNo: () -> Unit,
) : RScreen("提示") {
    companion object {
        val BG_RL = ResourceLocation("textures/block/stone.png")
    }

    override var clearColor = false
    override var closeable = false
    override var showTitle = false
    lateinit var yesBtn: RButton
    lateinit var noBtn: RButton
    lateinit var msgWidget: MultiLineTextWidget
    var startX = mcUIWidth / 2 - 100
    var startY = mcUIHeight / 2 - 50
    var width = 200
    var height = 100
    val iconSize = 14
    val icon = when (msglvl) {
        RMessageLevel.INFO -> Icons["info"]
        RMessageLevel.ERR -> Icons["error"]
        RMessageLevel.OK -> Icons["success"]
        RMessageLevel.WARN -> Icons["warning"]
    }/*
    override */

    override fun init() {
        yesBtn = RIconButton(Icons["success"], mcText(yesText), startX+width/2-30,startY+height) {
            onYes()
            mc goScreen prevScreen
        }.also { registerWidget(it) }
        noBtn = RIconButton(Icons["error"],mcText(noText), startX+width/2+30,startY+height) {
            onNo()
            mc goScreen prevScreen
        }.also { registerWidget(it) }
        if (diagType == RDialogType.ALERT) {
            noBtn.visible = false
            yesBtn.x = startX+width/2-15
        }
        msgWidget = MultiLineTextWidget(startX, startY + 16,msg, mcFont)
        super.init()

    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {


        guiGraphics.blit(
            BG_RL, startX,
            startY, 0, 0.0F, 0.0F, width, height+16, 32, 32
        );
        guiGraphics.fill(startX, startY + 16, startX + width, startY + height+16, 0xAA000000.toInt())

        guiGraphics.blit(icon, startX + 1, startY + 1, 0f, 0f, iconSize, iconSize, iconSize, iconSize)
        guiGraphics.drawString(mcFont, title, startX + iconSize + 2, startY + 4, WHITE)
        msgWidget.render(guiGraphics,mouseX,mouseY,partialTick)


        super.render(guiGraphics, mouseX, mouseY, partialTick)


    }


}
