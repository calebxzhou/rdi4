package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.mcText
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.player.Player

data class TutorialStep(
    val text: MutableComponent,
    //开始之前干啥
    val beforeOpr: (Player) -> Unit = {},
    //结束之后干啥
    val afterOpr: (Player) -> Unit = {},
    //完成条件 每tick检查一遍
    val completeCondition: (Player) -> Boolean
){
    constructor( text: String,
        //开始之前干啥
                 beforeOpr: (Player) -> Unit = {},
        //结束之后干啥
                 afterOpr: (Player) -> Unit = {},
                 completeCondition: (Player,) -> Boolean):this(mcText(text),beforeOpr, afterOpr, completeCondition)
}
