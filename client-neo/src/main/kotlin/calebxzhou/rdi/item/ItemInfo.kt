package calebxzhou.rdi.item

import calebxzhou.rdi.util.addChatMessage
import calebxzhou.rdi.util.mc
import net.minecraft.commands.Commands
import net.minecraft.core.registries.BuiltInRegistries

object ItemInfo {
    val cmd = Commands.literal("iteminfo").executes {
        val stack = mc.player!!.mainHandItem
        mc.addChatMessage("${stack.count}x ${BuiltInRegistries.ITEM.getKey(stack.item)}")
        mc.addChatMessage(stack.itemHolder.tags().toList().map { it.location }.joinToString(","))
        stack.tag?.asString?.let { mc.addChatMessage(it) }
        1
    }
}