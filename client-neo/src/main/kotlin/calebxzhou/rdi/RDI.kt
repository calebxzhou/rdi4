package calebxzhou.rdi

import calebxzhou.rdi.RDI.Companion.arrowCursor
import calebxzhou.rdi.RDI.Companion.handCursor
import calebxzhou.rdi.RDI.Companion.ibeamCursor
import calebxzhou.rdi.RDI.Companion.modIdChineseName
import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.account.LoginSPacket
import calebxzhou.rdi.model.Account
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.tutorial.Tutorial
import calebxzhou.rdi.tutorial.TutorialCommand
import calebxzhou.rdi.ui.ROverlay
import calebxzhou.rdi.ui.RScreenRectTip
import calebxzhou.rdi.ui.general.SlotWidgetDebugRenderer
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.util.*
import io.netty.util.concurrent.DefaultThreadFactory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.runtime.IJeiRuntime
import net.minecraft.client.gui.components.toasts.AdvancementToast
import net.minecraft.client.gui.components.toasts.RecipeToast
import net.minecraft.client.resources.language.ClientLanguage
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraftforge.client.event.RecipesUpdatedEvent
import net.minecraftforge.client.event.RegisterClientCommandsEvent
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent
import net.minecraftforge.client.event.RenderGuiEvent
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.client.event.ToastAddEvent
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW
import java.util.concurrent.Executors


const val MOD_ID = "rdi"
val logger = LogManager.getLogger(MOD_ID)
val numberOfCores = Runtime.getRuntime().availableProcessors()
val threadPool = Executors.newFixedThreadPool(numberOfCores, DefaultThreadFactory("RDI-ThreadPool"))
//线程池 异步
fun rAsync(todo: () -> Unit){
    threadPool.execute(todo)
}
//render thread 同步
fun rSync(todo: () -> Unit){
    mc.execute(todo)
}
//server thread 同步
fun risSync(todo: () -> Unit){
    mcs?.execute(todo)
}
@Mod(MOD_ID)
//@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class RDI {
    init {
        RDIEvents.init()
    }

    companion object {
        //mod id与中文名称map，高亮显示等用
        @JvmField
        val modIdChineseName = hashMapOf<String, String>()

        @JvmStatic
        fun log(): Logger {
            return logger
        }

        @JvmField
        var handCursor: Long = 0

        @JvmField
        var ibeamCursor: Long = 0

        @JvmField
        var arrowCursor: Long = 0


        fun onInitialize(event: FMLCommonSetupEvent) {
            logger.info("载入RDI")
        }

        @JvmStatic
        fun onMcStart() {
            logger.info("*********MINECRAFT STARTS*************")
        }


    }

}

object RDIEvents {
    fun init() {
        val bus = MinecraftForge.EVENT_BUS
        val busL = FMLJavaModLoadingContext.get().modEventBus
        busL.addListener(::load)
        busL.addListener(::loadComplete)
        busL.addListener(::allLoadComplete)
        busL.addListener(::registerOverlays)


        bus.addListener(::onLevelTick)
        bus.addListener(::checkGuiOverlays)
        bus.addListener(::onPlayerLogin)
        bus.addListener(::onRecipeUpdated)
       // bus.addListener(::pureColorBackground)
        bus.addListener(::onRenderLevelStage)
        bus.addListener(::onAddToast)
        bus.addListener(::leftClickBlock)
        bus.addListener(::onRenderGui)
        bus.addListener(::afterScreenRender)
        bus.addListener(::afterScreenMouseClick)
        bus.addListener(::onClientTick)
        bus.addListener(::registerClientCommand)
    }
    fun onAddToast(e: ToastAddEvent){
        //不显示进度toast
        if(e.toast is AdvancementToast || e.toast is RecipeToast) {
            e.isCanceled=true
        }
    }
    fun checkGuiOverlays(event: RenderGuiOverlayEvent.Pre) {
        val id = event.overlay.id
        //不绘制原版盔甲值
        if (id == VanillaGuiOverlay.ARMOR_LEVEL.id())
            event.isCanceled = true
    }

    fun load(event: FMLClientSetupEvent) {
        logger.info("客户端启动")
        RSoundPlayer.info()
        //设置光标类型
        handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_POINTING_HAND_CURSOR)
        ibeamCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)
        arrowCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        logger.info("调整窗口大小")

        val monitor = GLFW.glfwGetPrimaryMonitor()
        val vidmode = GLFW.glfwGetVideoMode(monitor) ?: let {
            logger.error("无法获取视频模式 videoMode=null")
            return
        }

        val screenW = vidmode.width()
        val screenH = vidmode.height()
        val w = screenW / 3 * 2
        val h = screenH / 3 * 2
        val x = screenW / 2 - w / 2
        val y = screenH / 2 - h / 2
        GLFW.glfwSetWindowSize(mcWindowHandle, w, h)
        GLFW.glfwSetWindowPos(mcWindowHandle, x, y)
        logger.info("尝试自动登录")
        LocalStorage["usr"]?.let { usr ->
            LocalStorage["pwd"]?.let { pwd ->
                IhqClient.send(LoginSPacket(usr, pwd)) { resp ->
                    if (resp.ok) {
                        val account = serdesJson.decodeFromString<Account>(resp.data)
                        Account.now = account
                        logger.info("自动登录成功")
                    } else {
                        logger.error("自动登录失败")
                    }
                }
            }
        }
    }

    fun registerClientCommand(e: RegisterClientCommandsEvent) {
        val debugCmds = listOf(
            TutorialCommand.cmd,
            Banner.cmd
        )
        val cmds = listOf(
            OmniNavi.cmd(e.buildContext),
            )
        if(Const.DEBUG){
            debugCmds.forEach { e.dispatcher.register(it) }
        }
        cmds.forEach{e.dispatcher.register(it)}
    }

    fun loadComplete(e: FMLLoadCompleteEvent) {
        //初始化modid to 中文名称
       /* ModList.get().mods.forEach {
            val id = it.modId
            val name = it.displayName
            var modName = ClientLanguage.getInstance().getOrDefault("itemGroup.${id}", name)
            if (modName == name) {
                modName = ClientLanguage.getInstance().getOrDefault("itemGroup.${id}.base", name)
            }
            if (modName == name) {
                modName = ClientLanguage.getInstance().getOrDefault("itemGroup.${id}.${id}", name)
            }
            modIdChineseName += id to modName
            logger.info("modid cn name: ${id}=${modName}")
        }*/
        modIdChineseName += "tfc" to "群峦传说"
        modIdChineseName += "firmalife" to "群峦人生"
        modIdChineseName += "ae2" to "应用能源2"
        modIdChineseName += "create" to "机械动力"
        modIdChineseName += "create_connected" to "机械动力·创意传动"
        modIdChineseName += "vinery" to "葡园酒香"
        modIdChineseName += "aether" to "天境"
        modIdChineseName += "farmersdelight" to "农夫乐事"
        modIdChineseName += "chefsdelight" to "厨师乐事"
        modIdChineseName += "aethersdelight" to "天境乐事"
        modIdChineseName += "oceansdelight" to "海洋乐事"
        modIdChineseName += "cuisinedelight" to "料理乐事"
        modIdChineseName += "computercraft" to "电脑"
        modIdChineseName += "minecraft" to "原版"

    }

    fun allLoadComplete(e: TextureStitchEvent.Post) {
        //mc.overlay=null
    }

    fun onRenderGui(e: RenderGuiEvent) {
        Banner.renderGui(e.guiGraphics)
        OmniNavi.renderGui(e.guiGraphics)
    }

    fun afterScreenRender(e: ScreenEvent.Render.Post) {
        Banner.renderScreen(e.guiGraphics,e.screen)
        SlotWidgetDebugRenderer.render(e.guiGraphics,e.screen)
        RScreenRectTip.render(e.guiGraphics, e.screen)

    }

    fun onRenderLevelStage(e: RenderLevelStageEvent) {
        OmniNavi.renderLevelStage(e)
    }

    fun onLevelTick(e: TickEvent.ServerTickEvent) {


            Tutorial.tick()
    }

    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (e.phase == TickEvent.Phase.END) {
            Banner.tick()
            if(mc.level!=null){
                OmniNavi.tick()
            }

            mc.screen?.let {

                RScreenRectTip.tick(it)
            }
        }
    }

    fun afterScreenMouseClick(e: ScreenEvent.MouseButtonPressed.Post) {
        RScreenRectTip.onClick(e)
    }

    fun onRecipeUpdated(e: RecipesUpdatedEvent) {

    }

    fun onPlayerLogin(e: PlayerLoggedInEvent) {
        if (Tutorial.isDoingTutorial) {
            mc.addChatMessage(mcText("欢迎来到入门教程\n跟随画面提示进行操作"))

        }
    }

    fun registerOverlays(event: RegisterGuiOverlaysEvent) {
        event.registerAboveAll(ROverlay.Armor.ID, ROverlay.Armor)
        event.registerAboveAll(ROverlay.Network.ID, ROverlay.Network)
    }

    fun leftClickBlock(e: LeftClickBlock) {
        val block = e.level.getBlockState(e.pos)
        if (block.`is`(BlockTags.LOGS) && e.entity.mainHandItem.isEmpty) {
            alertErr("砍树必须用斧头\n赤手空拳是不起作用的")
        }
    }

    fun pureColorBackground(event: ScreenEvent.BackgroundRendered) {
        if (mc.level == null)
            event.guiGraphics.fill(0, 0, event.guiGraphics.guiWidth(), event.guiGraphics.guiHeight(), PINE_GREEN)
    }
}

@JeiPlugin
class RDIJeiPlugin : IModPlugin {
    companion object {

        lateinit var jeiRuntime: IJeiRuntime
    }

    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation(MOD_ID, "jei_plugin")
    }

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        logger.info("rdi-jei插件可用")
        Companion.jeiRuntime = jeiRuntime
    }

}