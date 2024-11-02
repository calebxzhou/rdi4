package calebxzhou.rdi.text

import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.renderItemStack
import calebxzhou.rdi.util.matrixOp
import calebxzhou.rdi.util.mcFont
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

//文字，以及插在文字中的小图标
typealias RichTextRenderer = (GuiGraphics) -> Unit
class RichText(val x:Int,val y:Int,val renderers: List<RichTextRenderer>) {
    class Builder{
        val ren = arrayListOf<RichTextRenderer>()
        fun text(str:String,color: Int = WHITE){
            ren += {it.drawString(mcFont,str,0,0, color)}
        }
        fun player(skin: ResourceLocation){
            ren += { PlayerFaceRenderer.draw(it, skin, 0,0, 12)}
        }
        fun item(item: Item){
            ren += {it.renderItemStack(item.defaultInstance)}
        }
        fun icon(name: String){
            ren += {it.blit(Icons[name], 0, 0, 0f, 0f, 32, 32,32,32)}
        }
    }
    fun render(guiGraphics: GuiGraphics){
        guiGraphics.matrixOp {

        }
    }
}