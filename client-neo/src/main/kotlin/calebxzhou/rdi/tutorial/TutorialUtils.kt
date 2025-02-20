package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.LiteItemStack
import calebxzhou.rdi.util.addChatMessage
import calebxzhou.rdi.util.mc
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * calebxzhou @ 2024-10-29 10:21
 */

operator fun Item.times(count: Int) : LiteItemStack = this to count
val LiteItemStack.full
    get() = ItemStack(first,second)
infix fun Player.give(itemStack: ItemStack) {
    inventory.add(itemStack)
}
infix fun Player.give(lis: LiteItemStack){
    mc.addChatMessage("给你"+lis.first.description.string)
    give(lis.full)
}
infix fun Player.give(item: Item) {
    give(item*1)
}