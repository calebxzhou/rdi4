package calebxzhou.rdi.ui

import calebxzhou.rdi.util.Gl
import calebxzhou.rdi.util.ServerConnector.ping
import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcUIHeight
import calebxzhou.rdi.util.mcUIWidth
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Overlay
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ReloadInstance
import net.minecraft.util.FastColor.ARGB32
import net.minecraft.util.Mth
import java.util.*
import java.util.function.Consumer
import kotlin.math.roundToInt


class RLoadingOverlay (
    private var minecraft: Minecraft,
    private var reload: ReloadInstance,
    private var onFinish: Consumer<Optional<Throwable>>,
) : Overlay() {
    companion object {

        val LOGO: ResourceLocation =
            ResourceLocation("rdi", "textures/gui/title/mojangstudios.png")
    }


    private val reloading = true
    private var progress = 0f
    private var reloadCompleteTime = -1L
    private var reloadStartTime = -1L
    init {
        ping()
    }
    

    override fun isPauseScreen(): Boolean {
        return true
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        Gl.clearColor(WHITE)
        /*if (this.reloadCompleteTime > 1) {
            this.mc.setOverlay(null);
        }*/
        val g: Float

        val l = Util.getMillis()
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l
        }
        val f = if (this.reloadCompleteTime > -1L) (l - this.reloadCompleteTime).toFloat() / 1000.0f else -1.0f
        g = if (this.reloadStartTime > -1L) (l - this.reloadStartTime).toFloat() / 500.0f else -1.0f

        if (f >= 1.0f) {
            if (mc.screen != null) {
                mc.screen!!.render(guiGraphics, 0, 0, delta)
            }
        } else {
            if (mc.screen != null && g < 1.0f) {
                mc.screen!!.render(guiGraphics, mouseX, mouseY, delta)
            }
        }
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        guiGraphics.blit(LOGO, mcUIWidth / 2 - 60, mcUIHeight / 2 - 25, -0.0625f, 0.0f, 120, 60, 120, 120)
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        this.progress = Mth.clamp(this.progress * 0.99f + this.reload.actualProgress * 0.01f, 0.0f, 1.0f)
        if (f < 1.0f) {
            val i = Mth.ceil(mcUIWidth.toFloat() * this.progress)
            guiGraphics.fill(0,mcUIHeight-20,i,mcUIHeight, 0xFF000000.toInt())
        }
        if (f >= 2.0f) {
            mc.overlay = null
        }
        if (this.reloadCompleteTime == -1L && this.reload.isDone() && (!this.reloading || g >= 2.0f)) {
            try {
                this.reload.checkExceptions()
                onFinish.accept(Optional.empty())
            } catch (throwable: Throwable) {
                onFinish.accept(Optional.of(throwable))
            }
            this.reloadCompleteTime = Util.getMillis()
            if (mc.screen != null) {
                mc.screen!!.init(
                    mc,
                    mc.window.guiScaledWidth, mc.window.guiScaledHeight
                )
            }
        }
    }



}