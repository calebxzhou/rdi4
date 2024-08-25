package calebxzhou.rdi.tutorial

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.logger
import calebxzhou.rdi.mixin.client.ALivingEntity
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.RScreenRectTip
import calebxzhou.rdi.ui.general.confirm
import calebxzhou.rdi.ui.rectTip
import calebxzhou.rdi.ui.screen.RTitleScreen
import calebxzhou.rdi.util.RED
import calebxzhou.rdi.util.addChatMessage
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.lookingAtBlock
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import com.ibm.icu.impl.SimpleFormatterImpl.IterInternal.step
import net.dries007.tfc.TerraFirmaCraft
import net.dries007.tfc.client.screen.FirepitScreen
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.common.TFCTags
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.TFCTags.Items.ROCK_KNAPPING
import net.dries007.tfc.common.blocks.GroundcoverBlock
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.TFCBlocks.KAOLIN_CLAY_GRASS
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.items.TFCItems
import net.dries007.tfc.util.registry.RegistrySoilVariant
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.Difficulty
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraftforge.event.TickEvent
import java.io.File

fun tutorial(id: String, name: String, builder: Tutorial.Builder.() -> Unit): Tutorial {
    return Tutorial.Builder(id, name).apply(builder).build()
}

data class Tutorial(
    val id: String,
    val name: String,

    //开局给的
    val initKit: List<ItemStack>,
    val steps: List<TutorialStep>,
) {
    companion object{
        val isDoingTutorial
            get() = now==null
        var now: Tutorial?=null
        private set
        val stone1 = tutorial("stone1","石器时代1"){
            step("按空格键跳跃 (键盘下面最长那个键,字母CVBNM底下)") { (it as ALivingEntity).jumping }
            step("按下WASD键,前后左右走路,晃动鼠标观察四周,然后走到光柱位置",
                {
                    OmniNavi.navigate { blockState ->
                        blockState.block is LooseRockBlock && blockState.getValue(LooseRockBlock.COUNT) > 1
                    }
                }) { OmniNavi.posNow == null }
            step("让画面中央的十字对准石子,按鼠标右键捡起") { it.inventory.contains(TFCTags.Items.ROCK_KNAPPING) }
            step("四处走走 捡30个一样的石子") { player -> player.inventory.hasAnyMatching { it.`is`(ROCK_KNAPPING) && it.count >= 30 }  }
            step("拿着石子,看天空,按鼠标右键打磨") { mc.screen is KnappingScreen }
            step("依次点击绿框,打磨石斧头子",
                {
                    rectTip {
                        widgets(0, 10, 15, 20, 21, 23, 24, 19, 14, 4)
                        slot(0)
                        slot { !it.hasItem() && it.slotIndex!=0 }
                    }
                }) { RScreenRectTip.isCompleted }
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
            step("依次点击绿框,合成石斧", {
                rectTip {
                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot (3)
                    slot { it.item.toString().contains("axe_head")}
                    slot (1)
                    slot (0)
                    slot { !it.hasItem() &&   it.slotIndex in 10..35 }
                }
            }) {  RScreenRectTip.isCompleted }
            esc()
            step("斧拿手上,对准树的根部 长按鼠标左键砍树,拿5个原木") { player -> player.inventory.hasAnyMatching { it.`is`(ItemTags.LOGS) && it.count >= 5 } }
            step("按E键打开背包") { mc.screen is InventoryScreen }
            step("点击绿框 放置树枝", {
                rectTip {
                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot (3)
                }
            }) {  RScreenRectTip.isCompleted }
            step("鼠标右键点击红框 树枝斜对角放在合成栏 合成打火器", {
                rectTip {
                    color=RED
                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot (2)
                    slot (0)
                    slot { !it.hasItem() &&   it.slotIndex in 10..35 }
                }
            }) {  RScreenRectTip.isCompleted }
            step("按Q键扔东西 3个树枝1个原木扔在一起 用打火器长按右键点燃"){
                it.lookingAtBlock()?.`is`(TFCBlocks.FIREPIT.get()) == true
            }
            step("右键点击篝火"){
                mc.screen is FirepitScreen
            }
            step("放入树枝,点燃成为火把",{
                rectTip {
                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot (0)
                }
            }){ player ->
                player.inventory.hasAnyMatching{it.`is`(TFCItems.TORCH.get())}
            }
            step("拿着石子,看天空,按鼠标右键打磨") { mc.screen is KnappingScreen }
            step("依次点击绿框,打磨石铲头子",
                {
                    rectTip {
                        widgets(0,1,2,3,4,9,19,20,21,22,23,24)
                        slot(0)
                        slot { !it.hasItem() && it.slotIndex!=0 }
                    }
                }) { RScreenRectTip.isCompleted }
            step("按E键打开背包") { mc.screen is InventoryScreen }
            step("依次点击绿框,合成石铲", {
                rectTip {
                    slot { it.item.`is`(FIREPIT_STICKS) }
                    slot (3)
                    slot { it.item.toString().contains("shovel_head")}
                    slot (1)
                    slot (0)
                    slot { !it.hasItem() &&   it.slotIndex in 10..35 }
                }
            }) {  RScreenRectTip.isCompleted }
            esc()
            step("走到光柱位置 ",
                {
                    OmniNavi.navigate { blockState ->
                        blockState.`is`(SoilBlockType.CLAY_GRASS.create(SoilBlockType.Variant.SANDY_LOAM))||
                        blockState.`is`(SoilBlockType.CLAY_GRASS.create(SoilBlockType.Variant.SILTY_LOAM))||
                        blockState.`is`(SoilBlockType.CLAY_GRASS.create(SoilBlockType.Variant.LOAM))||
                        blockState.`is`(SoilBlockType.CLAY_GRASS.create(SoilBlockType.Variant.SILT))
                    }
                }) { OmniNavi.posNow == null }
            step("用铲子铲附近的黏土草块 拿10个黏土球") { player -> player.inventory.hasAnyMatching { it.`is`(Items.CLAY_BALL) && it.count >= 10 } }
            step("拿着黏土,看天空,按鼠标右键开始捏黏土") { mc.screen is KnappingScreen }
            step("依次点击绿框,合成陶罐,可以拿来装水", {
                rectTip {
                    widgets(0,10,15,17,19,20,21,23,24)
                    slot(0)
                    slot { !it.hasItem() && it.slotIndex!=0 }
                }
            }) {  RScreenRectTip.isCompleted }
            esc()
            step("继续拿着黏土,看天空,按鼠标右键开始捏黏土") { mc.screen is KnappingScreen }
            step("依次点击绿框,合成小缸,可以拿来装小物件和吃的", {
                rectTip {
                    widgets(0,4,20,24)
                    slot(0)
                    slot { !it.hasItem() && it.slotIndex!=0 }
                }
            }) {  RScreenRectTip.isCompleted }
            //todo 烧陶器
            /*step("继续走到光柱位置",
                {
                    OmniNavi.navigate { blockState ->
                        blockState.fluidState.`is`(Fluids.WATER)
                    }
                }) { OmniNavi.posNow == null }
            step("对准水 按右键喝水", { waterP = it.thrist } { waterP = 0f }
            ) { it.thrist > waterP }*/
        }

        fun tick(e: TickEvent.LevelTickEvent) {
            now?.tick()
        }
    }

    val levelName = "__rdi_tutorial_${id}"
    var stepIndex = 0
    var isPaused = false
    val stepNow
        get() = steps.getOrNull(stepIndex)

    fun reset() {
        stepIndex = 0
    }

    fun nextStep(player: LocalPlayer) {
        stepIndex++
        val nextStep = this.stepNow
        if (nextStep != null) {
            logger.info("开始教程${stepIndex}")
            RSoundPlayer.info()
            Banner.textNow = nextStep.text
            nextStep.beforeOpr(player)
        } else {
            quit()
        }
    }

    fun tick() {
        mc.player?.let { player ->
            stepNow?.let { stepNow ->
                if (stepNow.completeCondition(player) && !isPaused) {
                    stepNow.afterOpr(player)
                    logger.info("已完成教程${id}/${stepIndex}")
                    nextStep(player)
                }
            }
        }
    }
    fun start(){
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
        now = this
        reset()
        Banner.textNow = this.steps[0].text
    }
    fun quit() {
        reset()
        now=null
        Banner.reset()
        confirm("教程\"${name}\"已结束,现在退出教程吗?") {
            mc.clearLevel()

            //删掉教程存档
            File("saves/${levelName}").delete()
            mc goScreen RTitleScreen()
        }
    }

    fun prevStep(player: LocalPlayer) {
        stepIndex--
        val prevStep = this.stepNow
        if (prevStep != null) {
            logger.info("开始教程${stepIndex}")
            Banner.textNow = prevStep.text
            prevStep.beforeOpr(player)
        } else {
            mc.addChatMessage(mcText("没有上一步了"))
        }
    }

    class Builder(val id: String, val name: String, vararg val initKit: ItemStack) {
        val steps = arrayListOf<TutorialStep>()
        fun step(
            text: String,
            beforeOpr: (LocalPlayer) -> Unit = {},
            afterOpr: (LocalPlayer) -> Unit = {},
            completeCondition: (LocalPlayer) -> Boolean
        ): TutorialStep {
            steps += TutorialStep(text, beforeOpr, afterOpr, completeCondition).also { return it }
        }
        fun esc(){
            steps += step("按ESC键(在键盘左上角)关闭画面"){mc.screen==null}
        }
        fun build(): Tutorial {
            return Tutorial(id, name, initKit.toList(), steps)
        }
    }
}