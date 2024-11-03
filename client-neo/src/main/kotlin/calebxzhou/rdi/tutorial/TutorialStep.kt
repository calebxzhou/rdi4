package calebxzhou.rdi.tutorial

import calebxzhou.rdi.text.RichText
import calebxzhou.rdi.text.richText
import calebxzhou.rdi.util.mcText
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

data class TutorialStep(
    val text: RichText ,
    //开始之前干啥
    val beforeOpr: (ServerPlayer) -> Unit = {},
    //完成条件 每tick检查一遍
    val completeCondition: (ServerPlayer) -> Boolean
){
    constructor( text: String,
        //开始之前干啥
                 beforeOpr: (ServerPlayer) -> Unit = {},
                 completeCondition: (ServerPlayer,) -> Boolean):this(richText{text(text)},beforeOpr, completeCondition)
}
