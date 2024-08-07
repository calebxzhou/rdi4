package calebxzhou.rdi.quest

import net.minecraft.world.item.ItemStack

data class RQuest(
    val name: String,
    val description: String,
    val needs: List<ItemStack>,
    val rewards: List<ItemStack>
)
