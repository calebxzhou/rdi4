package calebxzhou.rdi.doc

import calebxzhou.rdi.doc.RDocument.*
import net.minecraft.ChatFormatting
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class Paragraph(val runs: List<Run>,val align: Alignment = Alignment.CENTER){
    class Builder{
        val runs = arrayListOf<Run>()
        fun text(str: String,vararg formats: ChatFormatting){
            runs += TextRun(str,formats.toList())
        }
        fun item(item: Item, amount: Int=1){
            runs += ItemRun(ItemStack(item,amount))
        }
        fun img(path: String,width: Int=32,height: Int=32){
            runs += ImageRun(path,width,height)
        }
        fun build(): Paragraph{
            return Paragraph(runs)
        }
    }
}