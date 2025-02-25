package calebxzhou.rdi

import calebxzhou.rdi.RDI.Companion.arrowCursor
import calebxzhou.rdi.RDI.Companion.handCursor
import calebxzhou.rdi.RDI.Companion.ibeamCursor
import calebxzhou.rdi.RDI.Companion.modIdChineseName
import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.blockguide.BlockGuide
import calebxzhou.rdi.chunkstats.ChunkStats
import calebxzhou.rdi.common.PINE_GREEN
import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.account.LoginSPacket
import calebxzhou.rdi.item.ItemInfo
import calebxzhou.rdi.item.RItems
import calebxzhou.rdi.lan.Lan
import calebxzhou.rdi.lang.EnglishStorage
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.tutorial.Tutorial
import calebxzhou.rdi.tutorial.TutorialCommand
import calebxzhou.rdi.ui.RGui
import calebxzhou.rdi.ui.general.SlotWidgetDebugRenderer
import calebxzhou.rdi.uiguide.UiGuide
import calebxzhou.rdi.util.*
import io.netty.util.concurrent.DefaultThreadFactory
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.runtime.IJeiRuntime
import mezz.jei.api.runtime.IRecipesGui
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.config.FoodExpiryTooltipStyle
import net.dries007.tfc.config.TFCConfig
import net.dries007.tfc.config.TimeDeltaTooltipStyle
import net.minecraft.client.gui.components.toasts.AdvancementToast
import net.minecraft.client.gui.components.toasts.RecipeToast
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.resources.language.ClientLanguage
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.minecraftforge.client.event.*
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.loading.FMLConfig
import net.minecraftforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW
import java.io.File
import java.util.concurrent.Executors


const val MOD_ID = "rdi"
val logger = LogManager.getLogger(MOD_ID)
val numberOfCores = Runtime.getRuntime().availableProcessors()
val threadPool = Executors.newFixedThreadPool(numberOfCores, DefaultThreadFactory("RDI-ThreadPool"))
val STORAGE = File(MOD_ID)

//线程池 异步
fun rAsync(todo: () -> Unit) {
    threadPool.execute(todo)
}

//render thread 同步
fun rSync(todo: () -> Unit) {
    mc.execute(todo)
}

//server thread 同步
fun risSync(todo: () -> Unit) {
    mcs?.execute(todo)
}

@Mod(MOD_ID)
//@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class RDI {
    val CREATIVE_TAB_REGISTRAR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Const.MODID)
    val CREATIVE_TAB = CREATIVE_TAB_REGISTRAR.register(Const.MODID){
        CreativeModeTab.builder()
            .title(mcText("rdi"))
            .icon{Items.REDSTONE.defaultInstance}
            .displayItems { params, output ->
                output.accept(RItems.METEORITE_SUMMONER.get())
            }
            .build()
    }
    init {
        System.setProperty("java.net.preferIPv6Addresses", "true")
        RDIEvents.init()
        STORAGE.mkdirs()
        RItems.REGISTER.register(FMLJavaModLoadingContext.get().modEventBus);
        CREATIVE_TAB_REGISTRAR.register(FMLJavaModLoadingContext.get().modEventBus)
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
        bus.addListener(::onReload)
        bus.addListener(::checkGuiOverlays)
        bus.addListener(::onPlayerLogin)
        bus.addListener(::onRecipeUpdated)
        // bus.addListener(::pureColorBackground)
        bus.addListener(::onRenderLevelStage)
        bus.addListener(::onAddToast)
        bus.addListener(::leftClickBlock)
        bus.addListener(::rightClickBlock)
        bus.addListener(::onRenderGui)
        bus.addListener(::afterScreenRender)
        bus.addListener(::onCloseScreen)
        bus.addListener(::screenMouseClick)
        bus.addListener(::onClientTick)
        bus.addListener(::onClientLevelTick)
        bus.addListener(::registerClientCommand)
        bus.addListener(::registerCommand)

    }


    fun onAddToast(e: ToastAddEvent) {
        //不显示进度toast
        if (e.toast is AdvancementToast || e.toast is RecipeToast) {
            e.isCanceled = true
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
                        val account = serdesJson.decodeFromString<RAccount>(resp.data)
                        RAccount.now = account
                        logger.info("自动登录成功")
                    } else {
                        logger.error("自动登录失败")
                    }
                }
            }
        }
        FMLConfig.updateConfig(FMLConfig.ConfigValue.VERSION_CHECK, false)
        TFCConfig.CLIENT.timeDeltaTooltipStyle.set(TimeDeltaTooltipStyle.DAYS)
        TFCConfig.CLIENT.foodExpiryTooltipStyle.set(FoodExpiryTooltipStyle.TIME_LEFT)
        File("saves").listFiles()?.forEach { subDir ->
            if (subDir.isDirectory && subDir.name.startsWith("__rdi_tutorial")) {
                subDir.deleteRecursively()
                println("删除教程存档: ${subDir.name}")
            }
        }
    }

    fun registerCommand(e: RegisterCommandsEvent) {
        val cmds = listOf(
            TutorialCommand.cmd,
        )
        // if (Const.DEBUG) {
        cmds.forEach { e.dispatcher.register(it) }
        // }

    }

    fun registerClientCommand(e: RegisterClientCommandsEvent) {
        val debugCmds = listOf(
            Banner.cmd
        )
        val cmds = listOf(
            OmniNavi.cmd(e.buildContext),
            Lan.cmd,
            ItemInfo.cmd,
            ChunkStats.cmd,
        )
        if (Const.DEBUG) {
            debugCmds.forEach { e.dispatcher.register(it) }
        }
        cmds.forEach { e.dispatcher.register(it) }
    }

    fun loadComplete(e: FMLLoadCompleteEvent) {

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

        EnglishStorage.lang = ClientLanguage.loadFrom(mc.resourceManager, listOf("en_us"), false)

    }
    fun onReload(e: AddReloadListenerEvent){

    }
    fun allLoadComplete(e: TextureStitchEvent.Post) {

        //mc.overlay=null
    }

    fun onRenderGui(e: RenderGuiEvent) {
        Banner.renderGui(e.guiGraphics)
        BlockGuide.now?.renderGui(e.guiGraphics)
        OmniNavi.renderGui(e.guiGraphics)
        Tutorial.now?.render(e.guiGraphics)
    }
    fun onCloseScreen(e: ScreenEvent.Closing){
        //如果正在进行uiguide 不允许关闭当前画面
        if(UiGuide.isOn) {
            e.isCanceled=true
        }
    }
    fun afterScreenRender(e: ScreenEvent.Render.Post) {
        UiGuide.now?.render(e.guiGraphics, e.mouseX, e.mouseY)
        Banner.renderScreen(e.guiGraphics, e.screen)
        if (e.screen is AbstractContainerScreen<*> || e.screen is IRecipesGui) {
            Tutorial.now?.render(e.guiGraphics, true)
        }
        if (Const.DEBUG) {
            SlotWidgetDebugRenderer.render(e.guiGraphics, e.screen)
        }
    }

    fun onRenderLevelStage(e: RenderLevelStageEvent) {
        OmniNavi.renderLevelStage(e)
        if (e.stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {

            BlockGuide.now?.render(e)
        }
    }

    fun onLevelTick(e: TickEvent.ServerTickEvent) {


        Tutorial.tick(e.server)
    }

    fun onClientLevelTick(e: TickEvent.LevelTickEvent) {
        if (e.side.isClient) {
            OmniNavi.tick()
            BlockGuide.now?.tick(e.level)

        }
    }

    fun onClientTick(e: TickEvent.ClientTickEvent) {
        if (e.phase == TickEvent.Phase.END) {
            Banner.tick()
            mc.screen?.let {
                UiGuide.now?.tick(it)
            }
        }
    }

    fun screenMouseClick(e: ScreenEvent.MouseButtonPressed.Pre) {
        UiGuide.now?.let { guide ->
            if (!guide.onClick(e))
                e.isCanceled = true
        }
    }

    fun onRecipeUpdated(e: RecipesUpdatedEvent) {

    }

    fun onPlayerLogin(e: PlayerLoggedInEvent) {
        Tutorial.now?.let {
            //超平坦地图设置边界
            if (it.isFlatLevel) {
                val border = e.entity.level().worldBorder
                border.setCenter(0.0, 0.0)
                border.size = 64.0
            }
            it.changeStep(0, e.entity as ServerPlayer)
        }
    }

    fun registerOverlays(event: RegisterGuiOverlaysEvent) {
        event.registerAboveAll(RGui.Armor.ID, RGui.Armor)
        event.registerAboveAll(RGui.Network.ID, RGui.Network)
    }

    fun leftClickBlock(e: LeftClickBlock) {
        val block = e.level.getBlockState(e.pos)
        if (block.`is`(BlockTags.LOGS) && e.entity.mainHandItem.isEmpty) {
            mc.addHudMessage("砍树必须用斧头")
        }

    }

    fun rightClickBlock(e: RightClickBlock) {
        //原木堆
        if (e.level.getBlockState(e.hitVec.blockPos).`is`(TFCBlocks.LOG_PILE.get())) {
            //只有手持原木or空手 才能打开原木堆画面
            if (!(e.entity handHas ItemTags.LOGS) && !e.entity.handsAir) {
                e.cancellationResult = InteractionResult.PASS
                //e.isCanceled = true
            }
        }
        BlockGuide.now?.onRightClick(e.level)
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