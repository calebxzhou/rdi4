package calebxzhou.rdi.tutorial

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.blocknav.BlockNavigator
import calebxzhou.rdi.log
import calebxzhou.rdi.mixin.client.ALivingEntity
import calebxzhou.rdi.ui.RScreenRectTip
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import net.dries007.tfc.TerraFirmaCraft
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.common.TFCTags
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.blocks.GroundcoverBlock
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.dries007.tfc.common.capabilities.food.TFCFoodData
import net.dries007.tfc.common.fluids.TFCFluids
import net.dries007.tfc.util.Metal
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.Difficulty
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.block.WaterlilyBlock
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.material.WaterFluid
import net.minecraftforge.event.TickEvent

//教程状态
object TutorialManager {
    //入门教程
    val isDoingTutorial
        get() = stepIndex < 0
    var stepIndex = -1
    val stepNow
        get() = STEPS.getOrNull(stepIndex)
    var waterP = 0f
    private val STEPS = listOf(
        TutorialStep("按空格键跳跃 (键盘下面最长那个键,字母CVBNM底下)") { (it as ALivingEntity).jumping },
        TutorialStep("按下WASD键,前后左右走路,晃动鼠标观察四周,然后走到光柱位置",
            {
                BlockNavigator.navigate { blockState ->
                    blockState.block is LooseRockBlock && blockState.getValue(LooseRockBlock.COUNT) > 1
                }
            }) { BlockNavigator.posNow == null },
        TutorialStep("让画面中央的十字对准石子,按鼠标右键") { it.inventory.contains(TFCTags.Items.ROCK_KNAPPING) },
        TutorialStep("拿着石子,看天空,按鼠标右键") { mc.screen is KnappingScreen },
        TutorialStep("依次点击绿框",{RScreenRectTip.byWidgets(0,10,15,20,21,23,24,19,14,4)}){RScreenRectTip.hasNo},
        TutorialStep("斧头") { player ->
            player.inventory.hasAnyMatching {
                it.toString().contains("axe_head")
            }
        },
        TutorialStep("继续走到光柱位置",
            {
                BlockNavigator.navigate { blockState ->
                    blockState.block is GroundcoverBlock &&
                            (blockState.block as GroundcoverBlock).getShape(
                        blockState,
                        it.level(),
                        null,
                        null
                    ) == GroundcoverBlock.TWIG
                }
            }) { BlockNavigator.posNow == null },
        TutorialStep("在附近捡5个一样的树枝") { player -> player.inventory.hasAnyMatching { it.`is`(FIREPIT_STICKS) && it.count >= 5 } },
        TutorialStep("按E键打开背包") { mc.screen is InventoryScreen },
        TutorialStep("合成栏竖放石斧头部+树枝,做出石斧") { it.inventory.contains(ItemTags.AXES) },
        TutorialStep("砍树的根部,拿到5个原木") { player -> player.inventory.hasAnyMatching { it.`is`(ItemTags.LOGS) && it.count >= 5 } },
        TutorialStep("继续走到光柱位置",
            {
                BlockNavigator.navigate { blockState ->
                    blockState.fluidState.`is`(Fluids.WATER)
                }
            }) { BlockNavigator.posNow == null },
        TutorialStep("对准水 按右键喝水", { waterP = it.thrist }, { waterP = 0f }
        ) { it.thrist > waterP },
    )


    fun reset() {
        stepIndex = -1
    }

    fun startTutorial() {
        mc goScreen GenericDirtMessageScreen(mcText("启动入门教程中...."))

        mc titled "入门教程"
        val levelName = "__rdi_tutorial"
        if (mc.levelSource.levelExists(levelName)) {
            mc.levelSource.createAccess(levelName).deleteLevel()
        }
        //关闭mc原版教程
        mc.options.tutorialStep = TutorialSteps.NONE
        mc.tutorial.setStep(TutorialSteps.NONE)
        mc.options.hideBundleTutorial = true
        mc.options.save()
        val levelSettings = LevelSettings(
            levelName,
            GameType.SURVIVAL,
            false,
            Difficulty.HARD,
            true,
            GameRules().apply {
                getRule(GameRules.RULE_DAYLIGHT).set(false, null)
                getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null)
            },
            WorldDataConfiguration.DEFAULT
        )
        mc.createWorldOpenFlows().createFreshLevel(
            levelName,
            levelSettings,
            WorldOptions(11451400, false, false)
        ) {
            it.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(TerraFirmaCraft.PRESET)
                .value().createWorldDimensions();
        }
        stepIndex = 0
        Banner.textNow = stepNow?.text
    }

    fun tick(e: TickEvent.LevelTickEvent) {
        mc.player?.let { player ->
            stepNow?.let { stepNow ->
                if (stepNow.completeCondition(player)) {
                    stepNow.afterOpr(player)
                    mc.addChatMessage(mcText("已完成教程${stepIndex}"))
                    stepIndex++
                    val nextStep = this.stepNow
                    if (nextStep != null) {
                        log.info("开始教程${stepIndex}")
                        Banner.textNow = nextStep.text
                        nextStep.beforeOpr(player)
                    } else {
                        mc.addChatMessage(mcText("教程已结束 可以退出了"))
                        Banner.reset()

                    }
                }

            }
        }

    }

    fun renderGui(guiGraphics: GuiGraphics) {

    }
}