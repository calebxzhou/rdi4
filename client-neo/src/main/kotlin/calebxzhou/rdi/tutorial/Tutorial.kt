package calebxzhou.rdi.tutorial

import calebxzhou.rdi.Const
import calebxzhou.rdi.STORAGE
import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.blockguide.BlockGuide
import calebxzhou.rdi.blockguide.blockGuide
import calebxzhou.rdi.logger
import calebxzhou.rdi.text.RichText
import calebxzhou.rdi.uiguide.UiGuide
import calebxzhou.rdi.uiguide.uiGuide
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Difficulty
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraft.world.level.levelgen.presets.WorldPresets
import java.io.File
fun tutorial(
    id: String,
    name: String,
    flat: Boolean = true,
    icon: Item = Items.REDSTONE,
    builder: Tutorial.Builder.() -> Unit
): Tutorial {
    return Tutorial.Builder(id, name, flat,icon).apply(builder).build()
}

data class Tutorial(
    val id: String,
    val name: String,
    val isFlatLevel: Boolean,
    val icon:Item,
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
            mc.addChatMessage(newStep.text.plainText)
            player.playNote()
            newStep.beforeOpr(player)
        } else {
            isDone = true
            mc.addChatMessage("恭喜你完成了教程“${name}”，可以退出了")
        }
    }

    fun render(guiGraphics: GuiGraphics,isContainerScreen: Boolean=false) {
           stepNow?.let { stepNow ->
                guiGraphics.matrixOp {
                    if(isContainerScreen) {
                        translate(5f,5f,1f)
                    } else {
                        translate(80.0, 50.0, 1.0)
                    }
                    //guiGraphics.fill(0, 50, 100, 300, 0x66000000)
                    stepNow.text.render(guiGraphics)
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
        mc go GenericDirtMessageScreen(mcText("请稍候"))
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
                    it.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.NORMAL)
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


    class Builder(val id: String, val name: String, val flat: Boolean,val icon: Item) {
        val steps = arrayListOf<TutorialStep>()
        fun step(
            text: String,
            beforeOpr: (ServerPlayer) -> Unit = {},
            completeCondition: (ServerPlayer) -> Boolean
        ) {
            steps += TutorialStep(text, beforeOpr, completeCondition)
        }
        fun step(
            text: RichText,
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
        fun buide(
            text: RichText,
            beforeOpr: (ServerPlayer) -> Unit = {},
            guider:  BlockGuide. Builder.() -> Unit
        ){
            step(text,{
                beforeOpr(it)
                blockGuide(guider)
            }){BlockGuide.isOff}
        }
        //让玩家自行检查操作是否完成 完成以后 点聊天框的完成按钮 必须手动下一步
        /*fun selfChk(text: String, beforeOpr: (ServerPlayer) -> Unit = {}) {
            val cmp = mcText(text) + mcText(" 完成后按T键，点击") +
                    mcText("<这里>").clickCommand("/tutorial next")
            steps += TutorialStep(cmp, beforeOpr) { false }
        }
*/
        fun tip(
            text: String,
            builder: UiGuide.Builder.() -> Unit,
        ) {
            step(text, { uiGuide(builder) }) { !UiGuide.isOn }
        }

        fun esc( ) {
            step("按ESC键关闭画面 (在键盘左上角)") { mc.screen == null }
        }



        fun build(): Tutorial {
            return Tutorial(id, name, flat, icon, steps)
        }
    }
}