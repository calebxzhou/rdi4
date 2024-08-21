package calebxzhou.rdi.tutorial

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.mixin.client.ALivingEntity
import calebxzhou.rdi.ui.RScreenRectTip
import calebxzhou.rdi.ui.rectTip
import calebxzhou.rdi.util.*
import net.dries007.tfc.TerraFirmaCraft
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.common.TFCTags
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.blocks.GroundcoverBlock
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.Difficulty
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraftforge.event.TickEvent
import java.io.File

//教程状态
object TutorialManager {
    //入门教程
    val isDoingTutorial
        get() = now==null
    var now: Tutorial?=null
    private val noobTutorial = tutorial("noob","新手教程"){
        step("按空格键跳跃 (键盘下面最长那个键,字母CVBNM底下)") { (it as ALivingEntity).jumping }
        step("按下WASD键,前后左右走路,晃动鼠标观察四周,然后走到光柱位置",
            {
                OmniNavi.navigate { blockState ->
                    blockState.block is LooseRockBlock && blockState.getValue(LooseRockBlock.COUNT) > 1
                }
            }) { OmniNavi.posNow == null }
        step("让画面中央的十字对准石子,按鼠标右键") { it.inventory.contains(TFCTags.Items.ROCK_KNAPPING) }
        step("拿着石子,看天空,按鼠标右键") { mc.screen is KnappingScreen }
        step("依次点击绿框 制作石斧头子",
            {
                rectTip {
                    widgets(0, 10, 15, 20, 21, 23, 24, 19, 14, 4)
                    slot(0)
                    slot { !it.hasItem() && it.slotIndex!=0 }
                }
            }) { RScreenRectTip.isCompleted }
        step("继续走到光柱位置",
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
        step("按鼠标右键捡起树枝,然后在附近捡5个一样的树枝") { player -> player.inventory.hasAnyMatching { it.`is`(FIREPIT_STICKS) && it.count >= 5 } }
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
        step("砍树的根部,拿到5个原木") { player -> player.inventory.hasAnyMatching { it.`is`(ItemTags.LOGS) && it.count >= 5 } }
        //todo  找粘土 生火
        /*step("继续走到光柱位置",
            {
                OmniNavi.navigate { blockState ->
                    blockState.fluidState.`is`(Fluids.WATER)
                }
            }) { OmniNavi.posNow == null }
        step("对准水 按右键喝水", { waterP = it.thrist } { waterP = 0f }
        ) { it.thrist > waterP }*/
    }



    fun startTutorial(tutorial: Tutorial) {

        val levelName = "__rdi_tutorial_${tutorial.id}"
        //删掉教程存档
        File("saves/${levelName}").delete()
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
        now = tutorial
        tutorial.reset()
        Banner.textNow = tutorial.steps[0].text
    }

    fun tick(e: TickEvent.LevelTickEvent) {

    }
    fun renderGui(guiGraphics: GuiGraphics) {

    }
}