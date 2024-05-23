package calebxzhou.rdi.util

import com.mojang.blaze3d.platform.GLX
import com.mojang.blaze3d.platform.Window
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW
import org.lwjgl.util.tinyfd.TinyFileDialogs

object DialogUtils {
    @JvmStatic
    fun showYesNoBox(msg: String): Boolean {
        return TinyFileDialogs.tinyfd_messageBox("提示", msg, "yesno", "question", false)
    }

    @JvmStatic
    fun showMessageBox(type: String, title: String, msg: String) {
        TinyFileDialogs.tinyfd_messageBox(title, msg, "ok", type, true)
    }

    @JvmStatic
    fun showMessageBox(type: String, msg: String) {
        showMessageBox(type, "", msg)
    }

    @JvmStatic
    fun showPopup(type: String, msg: String) {
        showPopup(type, "", msg)
    }

    @JvmStatic
    fun shouldClose(window: Window): Boolean {
        if (GLX._shouldClose(window)) {
            return if (showYesNoBox("真的要退出RDI客户端吗？\n")) {
                GLFW.glfwSetWindowShouldClose(ofWindowHandle(), true)
                true
            } else {
                GLFW.glfwSetWindowShouldClose(ofWindowHandle(), false)
                false
            }
        }
        return false
    }

    //info warning error c
    @JvmStatic
    fun showPopup(type: String, title: String, msg: String) {
        TinyFileDialogs.tinyfd_notifyPopup(title, msg, type);
    }
}