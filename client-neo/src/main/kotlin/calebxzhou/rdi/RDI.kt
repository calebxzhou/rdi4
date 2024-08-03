package calebxzhou.rdi

import calebxzhou.rdi.RDI.Companion.arrowCursor
import calebxzhou.rdi.RDI.Companion.handCursor
import calebxzhou.rdi.RDI.Companion.ibeamCursor
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.ROverlay
import calebxzhou.rdi.util.mcWindowHandle
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.runtime.IJeiRuntime
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW


const val MOD_ID = "rdi"
val log = LogManager.getLogger(MOD_ID)

@Mod(MOD_ID)
//@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class RDI {
    init {
        RDIEvents.init()
    }
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


    }

}
object RDIEvents{
    fun init(){
        val bus = MinecraftForge.EVENT_BUS
        val busL = FMLJavaModLoadingContext.get().modEventBus
        bus.addListener(::checkGuiOverlays)
        busL.addListener(::load)
        busL.addListener(::loadComplete)
        busL.addListener(::registerOverlays)
    }
    fun checkGuiOverlays(event: RenderGuiOverlayEvent.Pre){
        val id = event.overlay.id
        //不绘制原版盔甲值
        if(id == VanillaGuiOverlay.ARMOR_LEVEL.id())
            event.isCanceled=true
    }
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
    fun loadComplete(e: TextureStitchEvent.Post){

    }
    fun registerOverlays(event: RegisterGuiOverlaysEvent){
        event.registerAboveAll(ROverlay.Armor.ID, ROverlay.Armor)
        event.registerAboveAll(ROverlay.Network.ID, ROverlay.Network)
    }
}
@JeiPlugin
class RDIJeiPlugin : IModPlugin{
    companion object{

    lateinit var jeiRuntime: IJeiRuntime
    }
    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation(MOD_ID,"jei_plugin")
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        log.info("rdi-jei插件可用")
        Companion.jeiRuntime=jeiRuntime
    }

}