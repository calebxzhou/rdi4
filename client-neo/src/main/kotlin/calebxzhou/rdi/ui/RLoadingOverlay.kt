package calebxzhou.rdi.ui

import calebxzhou.rdi.util.ServerConnector.ping
import calebxzhou.rdi.util.mc
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Overlay
import net.minecraft.client.renderer.CubeMap
import net.minecraft.client.renderer.PanoramaRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ReloadInstance
import net.minecraft.util.FastColor.ARGB32
import net.minecraft.util.Mth
import java.util.*
import java.util.function.Consumer
import kotlin.math.min


class RLoadingOverlay (
    private var minecraft: Minecraft,
    private var reload: ReloadInstance,
    private var onFinish: Consumer<Optional<Throwable>>,
) : Overlay() {
    companion object {

        val LOGO: ResourceLocation =
            ResourceLocation("rdi", "textures/gui/title/mojangstudios.png")
    }
    val CUBE_MAP: CubeMap = CubeMap(ResourceLocation("rdi","textures/gui/title/background/panorama"))
    private
    val PANORAMA_OVERLAY: ResourceLocation = ResourceLocation("rdi","textures/gui/title/background/panorama_overlay.png")
    private val panorama = PanoramaRenderer(CUBE_MAP)

    private val reloading = true
    private var progress = 0f
    private var reloadCompleteTime = -1L
    private var reloadStartTime = -1L
    private var backgroundFadeStart: Long = 0
    private val loadProgress = 0f
    init {
        ping()


    }
    

    override fun isPauseScreen(): Boolean {
        return true
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        val width = mc.window.guiScaledWidth
        val height = mc.window.guiScaledHeight
        renderBackground(guiGraphics, delta,width, height)
        /*if (this.reloadCompleteTime > 1) {
            this.mc.setOverlay(null);
        }*/
        val h: Float
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
        var k: Int = (mc.window.guiScaledWidth.toDouble() * 0.5).toInt()
        val m = (mc.window.guiScaledHeight.toDouble() * 0.5).toInt()

        val n = min(
            mc.window.guiScaledWidth.toDouble() * 0.75,
            mc.window.guiScaledHeight.toDouble()
        ) * 0.25
        val p = (n * 0.5).toInt()
        val d = n * 4.0
        val q = (d * 0.5).toInt()
        panorama.render(delta, 1f)
        //RenderSystem.enableBlend()
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        guiGraphics.blit(LOGO,k - q, m - p, q, n.toInt(), -0.0625f, 0.0f, 120, 60, 120, 120)
        guiGraphics.blit(LOGO, k, m - p, q, n.toInt(), 0.0625f, 60.0f, 120, 60, 120, 120)

        guiGraphics.blit(PANORAMA_OVERLAY, 0, 0, width, height, 0.0f, 0.0f, 16, 128, 16, 128)
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        val r = (mc.window.guiScaledHeight.toDouble() * 0.8325).toInt()
        val s: Float = this.reload.actualProgress
        this.progress = Mth.clamp(this.progress * 0.95f + s * 0.050000012f, 0.0f, 1.0f)
        if (f < 1.0f) {
            this.renderProgressBar(guiGraphics, width / 2 - q, r - 5, width / 2 + q, r + 5, 1.0f - Mth.clamp(f, 0.0f, 1.0f))
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

    private fun renderProgressBar(matrices: GuiGraphics, minX: Int, minY: Int, maxX: Int, maxY: Int, opacity: Float) {
        val i = Mth.ceil((maxX - minX - 2).toFloat() * this.progress)
        val j = Math.round(opacity * 255.0f)
        val k = ARGB32.color(j, 255, 255, 255)
        matrices.fill(minX + 2, minY + 2, minX + i, maxY - 2, k)
        matrices.fill(minX + 1, minY, maxX - 1, minY + 1, k)
        matrices.fill( minX + 1, maxY, maxX - 1, maxY - 1, k)
        matrices.fill( minX, minY, minX + 1, maxY, k)
        matrices.fill( maxX, minY, maxX - 1, maxY, k)
    }

    private fun renderBackground(guiGraphics: GuiGraphics, delta: Float,width:Int,height:Int) {
        if (backgroundFadeStart == 0L) {
            this.backgroundFadeStart = Util.getMillis()
        }


        }

}