package calebxzhou.rdi.tutorial

import calebxzhou.rdi.Const
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import net.dries007.tfc.TerraFirmaCraft
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.core.registries.Registries
import net.minecraft.world.Difficulty
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions

//教程状态
object TutorialManager {
    //入门教程
    var isDoingTutorial = false
        private set
    val STEPS = listOf(
        TutorialStep(mcText("按WASD移动，摇晃鼠标观察四周")){ mc pressingKey InputConstants.KEY_W},
        TutorialStep(mcText("在地上寻找石子，")){ mc pressingKey InputConstants.KEY_W},
    )
    fun quitTutorial(){
        isDoingTutorial=false
        mc titled Const.VERSION_STR
    }
    fun startTutorial(){
        mc goScreen GenericDirtMessageScreen(mcText("启动入门教程中...."))
        isDoingTutorial=true
        mc titled "入门教程"
        val levelName = "__rdi_tutorial"
        if (mc.levelSource.levelExists(levelName)) {
            mc.levelSource.createAccess(levelName).deleteLevel()
        }
        //关闭mc原版教程
        mc.options.tutorialStep = TutorialSteps.NONE
        mc.tutorial.setStep(TutorialSteps.NONE)
        mc.options.hideBundleTutorial=true
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
    }

}