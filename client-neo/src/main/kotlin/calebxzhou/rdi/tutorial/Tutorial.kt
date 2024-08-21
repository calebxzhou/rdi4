package calebxzhou.rdi.tutorial

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.logger
import calebxzhou.rdi.util.addChatMessage
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack

fun tutorial(id: String,name: String, builder: Tutorial.Builder.()->Unit): Tutorial {
    return Tutorial.Builder(id,name).apply { builder }.build()
}
data class Tutorial(
    val id: String,
    val name: String,

    //开局给的
    val initKit: List<ItemStack>,
    val steps: List<TutorialStep>,
) {
    var stepIndex = 0
    val stepNow
        get() = steps.getOrNull( stepIndex)
    fun reset(){
        stepIndex=0
    }
    fun nextStep(player: LocalPlayer) {
        stepIndex++
        val nextStep = this.stepNow
        if (nextStep != null) {
            logger.info("开始教程${stepIndex}")
            Banner.textNow = nextStep.text
            nextStep.beforeOpr(player)
        } else {
            mc.addChatMessage(mcText("教程已结束 可以退出了"))
            Banner.reset()
            reset()
        }
    }
    fun tick(){
        mc.player?.let { player ->
            stepNow?.let { stepNow ->
                if (stepNow.completeCondition(player)) {
                    stepNow.afterOpr(player)
                    logger.info("已完成教程${id}/${stepIndex}")
                    nextStep(player)
                }
            }
        }
    }
    class Builder(val id: String,val name:String,vararg val initKit: ItemStack){
        val steps = arrayListOf<TutorialStep>()
        fun step(text: String, beforeOpr: (LocalPlayer) -> Unit = {}, afterOpr: (LocalPlayer) -> Unit = {}, completeCondition: (LocalPlayer) -> Boolean){
            steps += TutorialStep(text,beforeOpr,afterOpr,completeCondition)
        }
        fun build(): Tutorial{
            return Tutorial(id,name,initKit.toList(),steps)
        }
    }
}