package calebxzhou.rdi.text

import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.renderItemStack
import calebxzhou.rdi.util.matrixOp
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

//文字，以及插在文字中的小图标
typealias RichTextRenderer = RichText.Context.() -> Unit

class RichText(val x: Int, val y: Int, val renderers: List<RichTextRenderer>) {
    data class Context(
        val guiGraphics: GuiGraphics,
        var width: Int=0,
        var height: Int=0,
        var nowX: Int  ,
        var nowY: Int ){
        val pose
            get() = guiGraphics.pose()

    }

    class Builder(val startX: Int, val startY: Int) {
        val ren = arrayListOf<RichTextRenderer>()

        fun fill() {
            ren.add(0) {
                guiGraphics.fill(startX-4,startY-4, startX+width,startY+ height, 0xAA000000.toInt())
            }
        }

        fun text(str: String, color: Int = WHITE) {
            ren += {
                val txt = mcText(str)
                val width = guiGraphics.drawString(mcFont, txt, nowX, nowY, color)
                nowX+=width
            }
        }

        fun text(txt: Component) {
            ren += {
                val width = guiGraphics.drawString(mcFont, txt, nowX, nowY, txt.style.color?.value ?: WHITE)
                nowX += width
            }
        }

        fun player(skin: ResourceLocation) {
            ren += {
                PlayerFaceRenderer.draw(guiGraphics, skin,  nowX, nowY, 12)
                nowX += 16
            }
        }

        fun item(item: Item) {
            ren += {
                nowX += 5
                guiGraphics.matrixOp {
                    pose.translate(0f, 4f, 0f)
                    guiGraphics.renderItemStack(item.defaultInstance, 12, 12)
                }
                nowX += 8
            }
        }

        fun icon(name: String) {
            ren += {
                guiGraphics.matrixOp {
                    pose.translate(0f, -1f, 0f)
                    guiGraphics.blit(Icons[name], nowX,nowY, 0f, 0f, 10, 10, 10, 10)
                }
                nowX += 12
            }
        }

        fun ret() {
            ren += {
                if(nowX > width)
                    width = nowX
                if(nowY > height)
                    height = nowY
                nowX = startX
                nowY += 12
            }
        }

        fun build(): RichText {
            //最后一行要换行才能正确计算高度+显示背景
            ret()
            return RichText(startX, startY, ren)
        }
    }
    var widthHeight = 0 to 0
    fun render(guiGraphics: GuiGraphics) {
        val ctx = Context(guiGraphics,widthHeight.first,widthHeight.second,x,y)
        guiGraphics.matrixOp {
            renderers.forEach { it(ctx) }
        }
        widthHeight = ctx.width to ctx.height
    }
}

fun richText(x: Int = 100, y: Int = 100, builder: RichText.Builder.() -> Unit): RichText {
    return RichText.Builder(x, y).apply(builder).build()
}
