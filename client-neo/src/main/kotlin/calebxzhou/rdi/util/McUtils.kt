package calebxzhou.rdi.util

import calebxzhou.rdi.logger
import calebxzhou.rdi.ui.RMessageLevel
import calebxzhou.rdi.ui.general.RToast
import com.jcraft.jorbis.Block
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.vertex.PoseStack
import net.dries007.tfc.common.capabilities.food.TFCFoodData
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.server.IntegratedServer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.LevelChunkSection
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.lwjgl.glfw.GLFW
import org.lwjgl.util.tinyfd.TinyFileDialogs

val mc: Minecraft
    get() = Minecraft.getInstance() ?: run {
        throw IllegalStateException("Minecraft Not Start !")
    }
val mcs: IntegratedServer?
    get() = mc.singleplayerServer
val isMcStarted
    get() = Minecraft.getInstance() != null

fun mcMainThread(run: () -> Unit) {
    mc.execute(run)
}

val mcFont
    get() = mc.font
val mcWindowHandle
    get() = mc.window.window
val mcUIWidth
    get() = mc.window.guiScaledWidth
val mcUIHeight
    get() = mc.window.guiScaledHeight
val mcUIScale
    get() = mc.window.guiScale
val Player.thrist: Float
    get() {
        val d = foodData
        return if (d is TFCFoodData)
            d.thirst
        else 0f
    }

fun mcsCommand(cmd: String) {
    mcs?.let { mcs ->
        mcs.commands.performPrefixedCommand(mcs.createCommandSourceStack(), cmd)
    }?.let {
        logger.warn("mcs not run!")
    }
}

fun mcTick(sec: Int): Int {
    return sec * 20
}

fun mcText(str: String? = null): MutableComponent {
    return str?.let {
        Component.literal(it)
    } ?: Component.empty()
}

fun GuiGraphics.matrixOp(handler: PoseStack.() -> Unit) {
    val stack = pose()
    stack.pushPose()
    handler(stack)
    stack.popPose()
}

infix fun Minecraft.goScreen(screen: Screen?) {
    execute {
        setScreen(screen)
    }
}

infix fun Minecraft.titled(title: String) {
    mc.window.setTitle(title)
}

infix fun Minecraft.pressingKey(keyCode: Int): Boolean {
    return InputConstants.isKeyDown(mcWindowHandle, keyCode)
}

operator fun MutableComponent.plus(component: Component): MutableComponent {
    return append(component)
}

operator fun MutableComponent.plus(component: String): MutableComponent {
    return append(component)
}

fun Screen.drawTextAtCenter(gr: GuiGraphics, text: String) {
    drawTextAtCenter(gr, text, height / 2)
}

val Player.lookingAtBlock: BlockState?
    get() {
        val hit = pick(20.0, 0.0f, false)
        if (hit.type == HitResult.Type.BLOCK) {
            val bpos = (hit as BlockHitResult).blockPos
            val bstate = level().getBlockState(bpos)
            return bstate
        }
        return null
    }

fun Minecraft.addToast(toast: Toast) {
    toasts.addToast(toast)
}

fun Minecraft.addChatMessage(msg: String) {
    gui.chat.addMessage(mcText(msg))
}

fun Minecraft.addChatMessage(msg: Component) {
    gui.chat.addMessage(msg)
}

fun Minecraft.addHudMessage(msg: String) {
    gui.setOverlayMessage(mcText(msg), false);
}

fun LevelChunkSection.forEachBlock(todo: (BlockState) -> Unit) {
    for (x in 0..15)
        for (y in 0..15)
            for (z in 0..15)
                todo(this.getBlockState(x, y, z))
}

fun mcButton(text: String, x: Int, y: Int, w: Int, h: Int, onPress: Button.OnPress): Button {
    return mcButton(mcText(text), x, y, w, h, onPress);
}

fun mcButton(text: Component, x: Int, y: Int, w: Int, h: Int, onPress: Button.OnPress): Button {
    return Button.builder(text, onPress).bounds(x, y, w, h).build();
}

fun Screen.drawTextAtCenter(gr: GuiGraphics, text: String, height: Int) {
    drawTextAtCenter(gr, text, height, WHITE)
}

fun Screen.drawTextAt(gr: GuiGraphics, text: String, x: Int, y: Int) {
    val font = mcFont
    gr.drawString(font, text, x, y, 0xFFFFFF, false);
}

fun Screen.drawTextAtCenter(gr: GuiGraphics, text: String, height: Int, color: Int) {

    val font = mcFont
    gr.drawString(font, text, width / 2 - font.width(text) / 2, height, color, false);
}

fun Screen.drawTextAtCenter(gr: GuiGraphics, text: Component, height: Int) {

    val font = mcFont
    gr.drawString(font, text, width / 2 - font.width(text) / 2, height, 0xFFFFFF, false);
}

fun popupInfo(msg: String) {
    TinyFileDialogs.tinyfd_notifyPopup(msg, "RDI提示您", "info");
}

fun toastOk(msg: String) {
    mc.toasts.addToast(RToast(RMessageLevel.OK, msg))
}

fun showYesNoBox(msg: String): Boolean {
    return TinyFileDialogs.tinyfd_messageBox("提示", msg, "yesno", "question", false)
}

fun showToast(msg: String) {
    mc.toasts.addToast(RToast(RMessageLevel.INFO, msg))
}

fun mcTextWidthOf(text: String): Int {
    return mc.font.width(text)
}

fun mcTextWidthOf(text: Component): Int {
    return mc.font.width(text)
}

fun copyToClipboard(s: String) {
    GLFW.glfwSetClipboardString(mcWindowHandle, s)
}

object McUtils {
    @JvmStatic
    fun getWindowSize(screenW: Int, screenH: Int): Pair<Int, Int> {
        if (screenW >= 3840 && screenH >= 2160)
            return 1920 to 1080
        if (screenW >= 1920 && screenH >= 1080)
            return 1600 to 900
        if (screenW >= 1600 && screenH >= 900)
            return 1280 to 720
        return 800 to 480
    }

    @JvmStatic
    val mcHwnd
        get() = mcWindowHandle

    @JvmStatic
    val emptyButton = Button.Builder(
        Component.empty()
    ) { _: Button? -> }.build()
}