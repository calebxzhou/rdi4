package calebxzhou.rdi

import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.util.mcWindowHandle
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW


const val MOD_ID = "rdi"
val log = LogManager.getLogger(MOD_ID)

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class RDI {
    companion object {
        @JvmStatic
        fun log(): Logger {
            return log
        }

        @JvmField
        var handCursor: Long = 0

        @JvmField
        var ibeamCursor: Long = 0

        @JvmField
        var arrowCursor: Long = 0

        fun onInitialize(event: FMLCommonSetupEvent) {
            log.info("载入RDI")

        }

        @JvmStatic
        fun onMcStart() {
            RSoundPlayer.play("assets/rdi/sounds/title.ogg")

        }
        @JvmStatic
        @SubscribeEvent
        fun load(event: FMLClientSetupEvent){
            log.info("客户端启动")
            handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_POINTING_HAND_CURSOR)
            ibeamCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)
            arrowCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
            log.info("调整窗口大小")

            val monitor = GLFW.glfwGetPrimaryMonitor()
            val vidmode = GLFW.glfwGetVideoMode(monitor)?:let {
                log.error("无法获取视频模式 videoMode=null")
                return
            }

            val screenW = vidmode.width()
            val screenH = vidmode.height()
            val w = screenW / 3 * 2
            val h = screenH / 3 * 2
            val x = screenW/2-w/2
            val y = screenH/2-h/2
            GLFW.glfwSetWindowSize(mcWindowHandle, w, h)
            GLFW.glfwSetWindowPos(mcWindowHandle,x,y)
        }
        @JvmStatic
        @SubscribeEvent
        fun loadComplete(e: TextureStitchEvent.Post){

        }
    }

}