package calebxzhou.rdi.ui.general

import calebxzhou.rdi.ui.RMessageType
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Overlay
import org.lwjgl.glfw.GLFW.*

/**
 * Created  on 2023-07-23,22:05.
 */
enum class RDialogType{
    CONFIRM,ALERT
}

class RDialog (val msgType: RMessageType, val msg: String) : Overlay() {


    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val w = mcUIWidth
        val h = mcUIHeight
        val baseH = h / 5
        guiGraphics.fill(0, baseH * 2, w, baseH * 3, KLEIN_BLUE)
        guiGraphics.drawCenteredString(
            mc.font, msg,
            w / 2,
            h / 2 - 20,
            WHITE
        )
        guiGraphics.drawCenteredString(
            mc.font,
            "<右键点击=明白>",
            w / 2,
            h / 2 + 10,
            WHITE
        )
        if (glfwGetMouseButton(mcWindowHandle, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS) {
            mc.overlay = null
        }
    }

}
