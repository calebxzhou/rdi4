package calebxzhou.rdi.tutorial

import calebxzhou.rdi.mixin.client.tfc.AInventoryBlockEntity
import calebxzhou.rdi.util.*
import net.dries007.tfc.common.blockentities.PlacedItemBlockEntity
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.rock.Rock
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.blocks.wood.Wood
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * calebxzhou @ 2024-10-29 10:21
 */
fun Player.hasRockTool(type: RockCategory.ItemType): Boolean {
    return RockCategory.entries.any { category ->
        bagHas(TFCItems.ROCK_TOOLS[category]!![type]!!.get())
    }
}

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
infix fun Player.giveRock(count:Int) =
    give(TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().asItem() * count)
infix fun Player.giveTwig(count:Int) =
    give(TFCBlocks.WOODS[Wood.OAK]!![Wood.BlockType.TWIG]!!.get().asItem() * count)
infix fun Player.giveLog(count:Int) =
    give(TFCBlocks.WOODS[Wood.OAK]!![Wood.BlockType.LOG]!!.get().asItem() * count)
infix fun Player.giveRockTool(type: RockCategory.ItemType) {
    give(type.item)
}

fun lookingPitKilnHas(player: Player, item: Item): Boolean {
    val looking = player.lookingAtBlockEntity
    if(looking is PlacedItemBlockEntity){
        for(i in 0..3){
            if((looking as AInventoryBlockEntity<*>).getInventory().getStackInSlot(i).`is`(item))
                return true
        }
    }
    return false
}