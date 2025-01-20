package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.common.TL_BLACK
import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.math.Axis
import net.minecraft.Util
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

class LoadingScreen(val prevScreen: RScreen) : RScreen("请求中") {
    var startX = 0
    var startY = mcUIHeight / 2 - 50
    override var showTitle = false
    override var clearColor = false
    override var closeable = false
    companion object {
        val ICON_RL = Icons["loading"]
        val BG_RL = ResourceLocation("textures/block/dirt.png")
    }

    override fun init() {

        super.init()
    }
    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        prevScreen.render(guiGraphics,mouseX,mouseY,partialTick)
        guiGraphics fill TL_BLACK
        guiGraphics.blit(
            BG_RL, startX,
            startY, 0, 0.0F, 0.0F, width, 32, 32, 32
        )
        val millis = Util.getMillis() % 1000L // Get the current time in milliseconds and take the remainder when divided by 1000
        val angle = (millis / 1000.0) * 360 // Map the time to a range of 0 to 360 degrees

        guiGraphics.matrixOp {
            // Translate to center the logo scale(scaledF, scaledF, scaledF)
            translate(mouseX.toFloat(), mouseY.toFloat() , 1f)
            mulPose(Axis.ZP.rotationDegrees(angle.toFloat()))
            // Adjust translation to account for scaling
            translate(-16f, -16f, 0f)
            guiGraphics.blit(ICON_RL, 0, 0, 0f, 0f, 32, 32, 32, 32)
        }
        guiGraphics.drawCenteredString(mcFont, "载入中，请稍候...", mcUIWidth / 2, startY + 12, WHITE)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }


    override fun tick() {
        if(mc pressingKey InputConstants.KEY_LALT){
            mc go prevScreen
        }
        super.tick()
    }

    override fun onClose() {
        mc go prevScreen
    }
}