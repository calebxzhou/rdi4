package calebxzhou.rdi.doc

import net.minecraft.ChatFormatting
import net.minecraft.world.item.ItemStack

/**
 * calebxzhou @ 2024-08-04 11:30
 */
//字列
interface Run{

}
data class TextRun(val text: String,val formats: List<ChatFormatting>): Run {

}
data class ItemRun(val stack: ItemStack,): Run {

}
data class ImageRun(val path: String,val width:Int = 32,val height:Int=32): Run {

}

