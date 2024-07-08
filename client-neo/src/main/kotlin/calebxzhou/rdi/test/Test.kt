import calebxzhou.rdi.service.HardwareInfo
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

fun main() {
    print(HardwareInfo.toString())
}
