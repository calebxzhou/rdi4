package calebxzhou.craftcone.utils

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11C.*
import java.awt.Color.*
import java.nio.FloatBuffer
import java.nio.IntBuffer


/**
 * Created  on 2023-04-08,20:31.
 */
object Gl {
    @JvmStatic
    fun clearColor(color: Int) {

        clearColor(
            (color shr 16 and 0xFF) / 255f,
            (color shr 8 and 0xFF) / 255f,
            (color and 0xFF) / 255f,
            (color shr 24 and 0xFF) / 255f
        )
    }

    @JvmStatic
    fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        GlStateManager._clearColor(red, green, blue, alpha)
        GlStateManager._clear(16384, Minecraft.ON_OSX)
        RenderSystem.enableBlend()
    }

    //glfw创建窗口，自动计算窗口大小
    fun createWindow(title: String): Long {
        val width = 1280
        val height = 720
        return createWindow(width, height, title)
    }

    //glfw创建窗口
    fun createWindow(width: Int, height: Int, title: String): Long {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
        glfwInit()
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API)
        glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1)
        val windowHandle = glfwCreateWindow(
            width,
            height,
            title,
            0L,
            0L
        )
        glfwMakeContextCurrent(windowHandle)
        return windowHandle
    }

    fun destroyWindow(windowHandle: Long) {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null)?.free();
    }

    fun renderLoop(windowHandle: Long, doOnLoop: () -> Unit) {
        GL.createCapabilities();
        while (!glfwWindowShouldClose(windowHandle)) {
            doOnLoop.invoke()

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer
            glfwSwapBuffers(windowHandle) // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }

    private fun createFloatBuffer(data: FloatArray): FloatBuffer {
        val buffer = BufferUtils.createFloatBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }

    private fun createIntBuffer(data: IntArray): IntBuffer {
        val buffer = BufferUtils.createIntBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }
}
