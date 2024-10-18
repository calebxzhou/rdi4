package calebxzhou.rdi.tfc

import net.dries007.tfc.common.capabilities.size.ItemSizeManager
import net.dries007.tfc.common.capabilities.size.Size
import net.dries007.tfc.common.capabilities.size.Size.*
import net.dries007.tfc.common.capabilities.size.Weight
import net.dries007.tfc.common.capabilities.size.Weight.*
import net.dries007.tfc.util.Helpers
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

object RTfcItemSizeManager {
    @JvmStatic
    fun addTooltipInfo(stack: ItemStack, text: MutableList<Component>) {
        val size = ItemSizeManager.get(stack)
        val weightS = when(size.getWeight(stack)){
            VERY_LIGHT -> "超轻"
            LIGHT -> "轻便"
            MEDIUM -> "适中"
            HEAVY -> "沉重"
            VERY_HEAVY -> "笨重"
        }
        val sizeS = when(size.getSize(stack)){
            TINY -> "微小"
            VERY_SMALL -> "细小"
            SMALL -> "小巧"
            NORMAL -> "适中"
            LARGE -> "大"
            VERY_LARGE -> "巨大"
            HUGE -> "庞大"
        }
        text.add(
            Component.literal("\u2696")
                .append(weightS)
                .append(" \u21F2")
                .append(sizeS)
                .withStyle(ChatFormatting.GRAY)
        )
    }
}