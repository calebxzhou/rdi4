package calebxzhou.rdi.util

import com.mojang.blaze3d.platform.GLX
import com.mojang.blaze3d.platform.Window
import org.lwjgl.glfw.GLFW
import kotlin.system.exitProcess

object DialogUtils {

    @JvmStatic
    fun shouldClose(window: Window): Boolean {
        if (GLX._shouldClose(window)) {
            return if (showYesNoBox("真的要退出RDI客户端吗？\n")) {
                exitProcess(0)
                true
            } else {
                GLFW.glfwSetWindowShouldClose(mcWindowHandle, false)
                false
            }
        }
        return false
    }

}
