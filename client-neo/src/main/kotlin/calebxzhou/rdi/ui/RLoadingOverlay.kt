package calebxzhou.rdi.ui

import calebxzhou.rdi.util.*
import calebxzhou.rdi.util.ServerConnector.ping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Overlay
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ReloadInstance
import net.minecraft.util.Mth
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.*
import java.util.function.Consumer


class RLoadingOverlay(
    private var minecraft: Minecraft,
    private var reload: ReloadInstance,
    private var onFinish: Consumer<Optional<Throwable>>,
) : Overlay() {

    private val bg: ResourceLocation = asset("textures/screen_bg.png")
    private val bgb: ResourceLocation = asset("textures/screen_bg_mono.png")
    private val logo: ResourceLocation =
        ResourceLocation("rdi", "textures/logo.png")


    private var progress = 0.0

    init {
        ping()
    }

    override fun isPauseScreen(): Boolean {
        return true
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {

        val celProg = Mth.ceil(mcUIWidth.toFloat() * this.progress)
        guiGraphics.blit(
            bgb,
            0, 0, 0f, 0f, mcUIWidth, mcUIHeight, mcUIWidth, mcUIHeight
        )
        guiGraphics.blit(bg, 0, 0, 0f, 0f, celProg, mcUIHeight, mcUIWidth, mcUIHeight)
        guiGraphics.matrixOp {
            guiGraphics.blit(logo, mouseX - 50, mouseY - 25, -0.0625f, 0.0f, 120, 60, 120, 120)
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