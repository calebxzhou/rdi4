package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.mcText
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.MutableComponent

data class TutorialStep(
    val text: MutableComponent,
    //开始之前干啥
    val beforeOpr: (LocalPlayer) -> Unit = {},
    //教程中干啥
    //结束之后干啥
    val afterOpr: (LocalPlayer) -> Unit = {},
    val completeCondition: (LocalPlayer) -> Boolean
){
    constructor( text: String,
        //开始之前干啥
                 beforeOpr: (LocalPlayer) -> Unit = {},
        //结束之后干啥
                 afterOpr: (LocalPlayer) -> Unit = {},
                 completeCondition: (LocalPlayer,) -> Boolean):this(mcText(text),beforeOpr, afterOpr, completeCondition)
}
