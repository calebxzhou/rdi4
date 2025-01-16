package calebxzhou.rdi.ui

import calebxzhou.rdi.util.*
import com.mojang.math.Axis
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Overlay
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ReloadInstance
import net.minecraft.util.Mth
import java.util.*
import java.util.function.Consumer


class RLoadingOverlay(
    private var minecraft: Minecraft,
    private var reload: ReloadInstance,
    private var onFinish: Consumer<Optional<Throwable>>,
) : Overlay() {

    private val bg: ResourceLocation = asset("textures/screen_bg.png")
    private val logo: ResourceLocation =
        ResourceLocation("rdi", "textures/logo.png")


    private var progress = 0.0

    init {
    }

    override fun isPauseScreen(): Boolean {
        return true
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {

            guiGraphics.blit(bg, 0, 0, 0f, 0f, mcUIWidth, mcUIHeight, mcUIWidth, mcUIHeight)
        val millis =
            Util.getMillis() % 1000L // Get the current time in milliseconds and take the remainder when divided by 1000
        val angle = (millis / 1000.0) * 360 // Map the time to a range of 0 to 360 degrees
        guiGraphics.matrixOp {
            val f = 0.5F + 1.5F * (1.0F + Mth.cos(angle.toFloat() * (Math.PI.toFloat() / 180F)))
            val scaledF = f * 35.0F / 80f.toFloat()
            translate(mouseX.toFloat(), mouseY.toFloat(), 1f)
            mulPose(Axis.ZP.rotationDegrees(angle.toFloat())) // Rotate around the Z-axis
            translate(-64 * scaledF, -32 * scaledF, 0f) // Translate to center the logo
            scale(scaledF, scaledF, scaledF)
            guiGraphics.blit(logo, 0,0, 0f, 0.0f, 128, 64, 128, 64)
        }

        this.progress = (this.progress * 0.995 + this.reload.actualProgress * 0.01).coerceIn(0.0, 1.0)
        if (this.reload.isDone) {
            try {
                this.reload.checkExceptions()
                onFinish.accept(Optional.empty())
            } catch (throwable: Throwable) {
                onFinish.accept(Optional.of(throwable))
            }
            mc.overlay = null
            mc.screen?.init(
                mc,
                mc.window.guiScaledWidth, mc.window.guiScaledHeight
            )

        }
    }


}