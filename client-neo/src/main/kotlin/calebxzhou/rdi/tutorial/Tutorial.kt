package calebxzhou.rdi.tutorial

import calebxzhou.rdi.Const
import calebxzhou.rdi.STORAGE
import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.blockguide.BlockGuide
import calebxzhou.rdi.blockguide.blockGuide
import calebxzhou.rdi.logger
import calebxzhou.rdi.uiguide.UiGuide
import calebxzhou.rdi.uiguide.uiGuide
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import mezz.jei.api.runtime.IRecipesGui
import net.dries007.tfc.TerraFirmaCraft
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.MultiLineLabel
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Difficulty
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraft.world.level.levelgen.presets.WorldPresets
import java.io.File

fun tutorial(id: String, name: String, flat: Boolean = true, builder: Tutorial.Builder.() -> Unit): Tutorial {
    return Tutorial.Builder(id, name, flat).apply(builder).build()
}

data class Tutorial(
    val id: String,
    val name: String,
    val isFlatLevel: Boolean,
    //开局给的
    val initKit: List<ItemStack>,
    val steps: List<TutorialStep>,
) {
    companion object {
        val isDoingTutorial
            get() = now != null
        var now: Tutorial? = null
            private set


        fun tick(server: MinecraftServer) {
            now?.tick(server)
        }
    }

    val levelName = "__rdi_tutorial_${id}"
    var stepIndex = -1
    var isPaused = false
    val stepNow
        get() = steps.getOrNull(stepIndex)

    fun reset() {
        stepIndex = 0

    }

    fun nextStep(player: ServerPlayer) = changeStep(stepIndex + 1, player)
    fun prevStep(player: ServerPlayer) = changeStep(stepIndex - 1, player)
    fun changeStep(newStepIndex: Int, player: ServerPlayer) {
        stepIndex = newStepIndex
        val newStep = this.stepNow
        if (newStep != null) {
            logger.info("开始教程${stepIndex}")
            mc.addChatMessage("")
            mc.addChatMessage(mcText("${stepIndex + 1}.") + newStep.text)
            player.playNote()
            newStep.beforeOpr(player)
        } else {
            isDone = true
            mc.addChatMessage("恭喜你完成了教程“${name}”，可以退出了")
        }
    }

    fun render(guiGraphics: GuiGraphics) {
        //只在容器ui上层渲染
        if (mc.screen is AbstractContainerScreen<*> || mc.screen is IRecipesGui) {
            stepNow?.let { stepNow ->
                guiGraphics.matrixOp {
                    translate(0.0, 24.0, 100.0)
                    guiGraphics.fill(0, 50, 100, 300, 0x66000000)
                    MultiLineLabel.create(mcFont, stepNow.text, 100).renderLeftAligned(guiGraphics, 0, 50, 10, WHITE)
                }
            }
        }
    }

    fun tick(server: MinecraftServer) {
        server.playerList.players.forEach { player ->
            stepNow?.let { stepNow ->
                if (stepNow.completeCondition(player) && !isPaused) {
                    logger.info("已完成教程${id}/${stepIndex}")
                    changeStep(stepIndex + 1, player)
                }
            }
        }
    }

    fun start() {
        mc goScreen GenericDirtMessageScreen(mcText("请稍候"))
        //关闭mc原版教程
        mc.options.tutorialStep = TutorialSteps.NONE
        mc.tutorial.setStep(TutorialSteps.NONE)
        mc.options.hideBundleTutorial = true
        mc.options.save()
        deleteMap()
        val levelSettings = LevelSettings(
            levelName,
            GameType.SURVIVAL,
            false,
            Difficulty.NORMAL,
            Const.DEBUG,
            GameRules().apply {
                getRule(GameRules.RULE_COMMANDBLOCKOUTPUT).set(false, null)
                getRule(GameRules.RULE_RANDOMTICKING).set(20, null)
                getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null)
                getRule(GameRules.RULE_ANNOUNCE_ADVANCEMENTS).set(false, null)
                getRule(GameRules.RULE_DAYLIGHT).set(false, null)
            },
            WorldDataConfiguration.DEFAULT
        )

        mc.createWorldOpenFlows()
            .createFreshLevel(
                levelName,
                levelSettings,
                WorldOptions(Const.SEED, false, false)
            ) {
                if (isFlatLevel) {
                    it.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT)
                        .value().createWorldDimensions();
                } else {
                    it.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(TerraFirmaCraft.PRESET)
                        .value().createWorldDimensions();
                }
            }
        now = this
    }

    var file = File(STORAGE.resolve("tutorial").also { it.mkdirs() }, id)
    var isDone: Boolean
        get() = file.exists()
        set(value) {
            if (value)
                file.createNewFile()
            else
                file.delete()
        }

    fun quit() {
        now = null
        Banner.reset()
        TutorialState.reset()
        BlockGuide.now?.stop()
        deleteMap()
    }

    fun deleteMap() {
        File("saves/${levelName}").deleteRecursively()
    }


    class Builder(val id: String, val name: String, val flat: Boolean, vararg val initKit: ItemStack) {
        val steps = arrayListOf<TutorialStep>()
        fun step(
            text: String,
            beforeOpr: (ServerPlayer) -> Unit = {},
            completeCondition: (ServerPlayer) -> Boolean
        ) {
            steps += TutorialStep(text, beforeOpr, completeCondition)
        }
        fun buide(
            text: String,
            beforeOpr: (ServerPlayer) -> Unit = {},
            guider:  BlockGuide. Builder.() -> Unit
        ){
            step(text,{
                beforeOpr(it)
                blockGuide(guider)
            }){BlockGuide.isOff}
        }
        //让玩家自行检查操作是否完成 完成以后 点聊天框的完成按钮 必须手动下一步
        fun selfChk(text: String, beforeOpr: (ServerPlayer) -> Unit = {}) {
            val cmp = mcText(text) + mcText(" 完成后按T键，点击") +
                    mcText("<这里>")
                        .withStyle(
                            Style.EMPTY
                                .applyFormat(ChatFormatting.GREEN)
                                .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, mcText("检查完成情况，下一步")))
                                .withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial next"))
                        )
            steps += TutorialStep(cmp, beforeOpr) { false }
        }

        fun tip(
            text: String,
            builder: UiGuide.Builder.() -> Unit,
        ) {
            step(text, { uiGuide(builder) }) { !UiGuide.isOn }
        }

        fun esc( ) {
            step("按ESC键关闭画面 (在键盘左上角)") { mc.screen == null }
        }

        fun jump() {
            step("按空格键跳跃 (键盘下面的大长条按钮,字母CVBNM底下)") {
                mc pressingKey InputConstants.KEY_SPACE//(it as ALivingEntity).jumping
            }
        }

        fun build(): Tutorial {
            return Tutorial(id, name, flat, initKit.toList(), steps)
        }
    }
}
/*
val stoneAge = listOf(
            tutorial("stone1", "石器时代1 树与火") {
                jump()
                step("按WASD键前后左右走路,晃动鼠标改变视角,然后走到光柱位置",
                    {
                        OmniNavi.navigate { blockState ->
                            blockState.block is LooseRockBlock && blockState.getValue(LooseRockBlock.COUNT) > 1
                        }
                    }) { OmniNavi.posNow == null }
                step("让画面中央的十字对准石子,按鼠标右键捡起") { it.inventory.contains(TFCTags.Items.ROCK_KNAPPING) }
                step("四处走走 捡10个一样的石子") { player -> player.inventory.hasAnyMatching { it.`is`(ROCK_KNAPPING) && it.count >= 10 } }
                step("拿着石子,看天空,按鼠标右键打磨",
                    { mc.addChatMessage("下面咱们来做一把石斧 砍树用") }) { mc.screen is KnappingScreen }
                tip("依次点击绿框,打磨石斧头子") {
                    widgets(0, 10, 15, 20, 21, 23, 24, 19, 14, 4)
                    slot(0)
                    emptyInvSlotContainer()
                }
                esc()
                step("继续走到光柱位置 捡起树枝",
                    {
                        OmniNavi.navigate { blockState ->
                            blockState.block is GroundcoverBlock &&
                                    (blockState.block as GroundcoverBlock).getShape(
                                        blockState,
                                        it.level(),
                                        null,
                                        null
                                    ) == GroundcoverBlock.TWIG
                        }
                    }) { OmniNavi.posNow == null }
                step("在附近树下捡10个一样的树枝") { player -> player.inventory.hasAnyMatching { it.`is`(FIREPIT_STICKS) && it.count >= 10 } }
                step("按E键打开背包") { mc.screen is InventoryScreen }
                tip("依次点击绿框,合成石斧",
                    {
                        slot { it.item.`is`(FIREPIT_STICKS) }
                        slot(3)
                        slot { it.item.toString().contains("axe_head") }
                        slot(1)
                        slot(0)
                        emptyInvSlot()
                    }
                )
                esc()
                step("斧拿手上,对准树的根部 长按鼠标左键砍树,拿5个原木") { player ->
                    player.inventory.hasAnyMatching {
                        it.`is`(
                            ItemTags.LOGS
                        ) && it.count >= 5
                    }
                }
                step("按E键打开背包") { mc.screen is InventoryScreen }
                tip("鼠标右键点击树枝", {
                    rectTip {
                        mode = Mode.RMB
                        slot { it.item.`is`(FIREPIT_STICKS) }
                    }
                })
                tip("鼠标左键点击绿框", {
                    rectTip {
                        emptyInvSlot()
                    }
                })
                tip("依次点击绿框 合成打火器", {
                    rectTip {
                        slot { it.item.`is`(FIREPIT_STICKS) }
                        slot(3)
                        slot { it.item.`is`(FIREPIT_STICKS) }
                        slot(2)
                        slot(0)
                        emptyInvSlot()
                    }
                })
                step("按Q键扔东西 把3个树枝1个原木扔在一起 用打火器长按右键点燃") {
                    it.lookingAtBlock?.`is`(TFCBlocks.FIREPIT.get()) == true
                }
                step("右键点击篝火") {
                    mc.screen is FirepitScreen
                }

            },
            tutorial("stone2", "石器时代2 陶器") {
                jump()
                step("拿着石子,看天空,按鼠标右键打磨", {
                    mcsCommand("give @a tfc:rock/loose/dolomite 32")
                    mcsCommand("give @a tfc:wood/twig/maple 20")
                    mcsCommand("give @a tfc:firestarter")
                    mcsCommand("give @a tfc:wood/log/birch 16")
                }) { mc.screen is KnappingScreen }
                tip("依次点击绿框,打磨石铲头子"
                ) {

                    widgets(0, 1, 2, 3, 4, 9, 19, 20, 21, 22, 23, 24)
                    slot(0)
                    emptyInvSlotContainer()
                }
                step("按E键打开背包") { mc.screen is InventoryScreen }
                tip("依次点击绿框,合成石铲") {

                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot(3)
                    slot { it.item.toString().contains("shovel_head") }
                    slot(1)
                    slot(0)
                    emptyInvSlot()

                }
                esc()
                step("拿着石子,看天空,按鼠标右键打磨") { mc.screen is KnappingScreen }
                tip("依次点击绿框,打磨石刀刃"
                ) {

                    widgets(5, 10, 11, 12, 13, 14, 15)
                    slot(0)
                    emptyInvSlotContainer()

                }
                step("按E键打开背包") { mc.screen is InventoryScreen }
                tip("依次点击绿框,合成石刀") {

                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot(3)
                    slot { it.item.toString().contains("knife_head") }
                    slot(1)
                    slot(0)
                    emptyInvSlot()

                }
                step("石刀拿手上 对准草 鼠标左键割草 割20个干草") { player ->
                    player.inventory.hasAnyMatching {
                        it.`is`(
                            TFCItems.STRAW.get()
                        ) && it.count >= 20
                    }
                }
                step("走到光柱位置 寻找黏土草块",
                    {
                        OmniNavi.navigate { blockState ->
                            blockState.`is`(TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.SANDY_LOAM]!!.get()) ||
                                    blockState.`is`(TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.SILTY_LOAM]!!.get()) ||
                                    blockState.`is`(TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.LOAM]!!.get()) ||
                                    blockState.`is`(TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.SILT]!!.get())
                        }
                    }) { OmniNavi.posNow == null }
                step("用铲子铲附近的黏土草块 拿12个黏土球") { player -> player.inventory.hasAnyMatching { it.`is`(Items.CLAY_BALL) && it.count >= 12 } }
                step("拿着黏土,看天空,按鼠标右键开始捏黏土") { mc.screen is KnappingScreen }
                tip("依次点击绿框,合成陶罐,可以拿来装水") {

                    widgets(0, 10, 15, 17, 19, 20, 21, 23, 24)
                    slot(0)
                    emptyInvSlotContainer()

                }
                esc()
                step("继续拿着黏土,看天空,按鼠标右键开始捏黏土") { mc.screen is KnappingScreen }
                tip("依次点击绿框,合成小缸,可以拿来装小物件和吃的") {

                    widgets(0, 4, 20, 24)
                    slot(0)
                    emptyInvSlotContainer()

                }
                step("挖一个坑 拿着陶器胚 朝着坑里按V键 放入陶器胚") {
                    it.lookingAtBlock?.`is`(TFCBlocks.PLACED_ITEM.get()) == true
                }
                step("拿8个干草 右键坑 放进去") {
                    it.lookingAtBlock?.`is`(TFCBlocks.PIT_KILN.get()) == true
                            &&
                            it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.STRAW_END
                }
                step("拿8个原木 右键坑 放进去") {
                    it.lookingAtBlock?.`is`(TFCBlocks.PIT_KILN.get()) == true
                            &&
                            it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.LIT - 1
                }
                step("起火器对准坑窑 长按右键") {
                    it.lookingAtBlock?.`is`(Blocks.FIRE) == true
                }
                step("等8分钟 取出熟陶器（也可以从侧边挖开坑窑 立刻取出）") { player ->
                    player.inventory.hasAnyMatching { it.`is`(TFCItems.VESSEL.get()) }
                }
            })

 */