package calebxzhou.rdi.tutorial

import net.minecraft.network.chat.MutableComponent

data class TutorialStep(
    val text: MutableComponent,
    //开始之前干啥
    val beforeOpr: () -> Unit = {},
    //结束之后干啥
    val afterOpr: () -> Unit = {},
    val completeCondition: () -> Boolean
)
