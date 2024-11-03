package calebxzhou.rdi.text

import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.renderItemStack
import calebxzhou.rdi.util.hoverText
import calebxzhou.rdi.util.matrixOp
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

//文字，以及插在文字中的小图标
fun richText(x: Int=100, y: Int=100, builder: RichText.Builder.() -> Unit): RichText {
    return RichText.Builder(x, y).apply(builder).build()
}
typealias RichTextRenderer = (GuiGraphics) -> Unit

class RichText(val x: Int, val y: Int, val renderers: List<RichTextRenderer>) {
    class Builder(val startX: Int, val startY: Int) {
        val ren = arrayListOf<RichTextRenderer>()
        private var offsetX = 0f
        private var offsetY = 0f
        private fun PoseStack.dx(x: Float){
            translate(x,0f,0f)
            offsetX += x
        }
        private fun PoseStack.dy(y:Float){
            translate(0f,y,0f)
            offsetY += y
        }
        fun text(str: String, color: Int = WHITE,hoverText: String?=null,) {
            ren += {
                val txt = mcText(str)
                if(hoverText!=null){
                    txt.hoverText(hoverText)
                }
                val width = it.drawString(mcFont, txt, 0, 0, color)
                it.pose().dx(width.toFloat())
            }
        }

        fun player(skin: ResourceLocation) {
            ren += {
                PlayerFaceRenderer.draw(it, skin, 0, 0, 12)
                it.pose().dx(16f)
            }
        }

        fun item(item: Item) {
            ren += {

                it.pose().dx(5f)
                it.matrixOp {
                    it.pose().translate(0f, 4f, 0f)
                    it.renderItemStack(item.defaultInstance, 12, 12)
                }
                it.pose().dx(8f)
            }
        }

        fun icon(name: String) {
            ren += {
                it.matrixOp {

                //稍微往上窜一点 不然跟文字对不齐
                it.pose().translate(0f, -1f, 0f)
                it.blit(Icons[name], 0, 0, 0f, 0f, 10, 10, 10, 10)
                }

                it.pose().dx(12f)
            }
        }
        fun ret(){
            ren += {
                it.pose().dx(-offsetX)
                it.pose().dy(12f)
            }
        }

        fun build(): RichText {
            return RichText(startX, startY, ren)
        }
    }

    fun render(guiGraphics: GuiGraphics) {
        guiGraphics.matrixOp {
            translate(x.toFloat(), y.toFloat(), 1f)
            renderers.forEach { it(guiGraphics) }
        }
    }
}