package calebxzhou.rdi.text

import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.tutorial.full
import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.KeyboardIcons
import calebxzhou.rdi.ui.general.renderItemStack
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.MultiLineLabel
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

//文字，以及插在文字中的小图标
typealias RichTextRenderer = RichText.Context.() -> Unit

class RichText(val x: Int, val y: Int, val renderers: List<RichTextRenderer>) {
    data class Context(
        val guiGraphics: GuiGraphics,
        var width: Int = 0,
        var height: Int = 0,
        var nowX: Int,
        var nowY: Int
    ) {
        val pose
            get() = guiGraphics.pose()

    }

    class Builder(val startX: Int, val startY: Int) {
        val ren = arrayListOf<RichTextRenderer>()

        fun fill() {
            ren.add(0) {
                guiGraphics.fill(startX - 4, startY - 4, startX + width, startY + height, 0xAA000000.toInt())
            }
        }

        fun text(str: String, color: Int = WHITE) {
            ren += {
                val txt = mcText(str)
                val width =
                    MultiLineLabel.create(mcFont, txt, mcUIWidth).renderLeftAligned(guiGraphics, nowX, nowY, 10, color)
                //val width = guiGraphics.drawString(mcFont, txt, nowX, nowY, color)
                nowX += mcFont.width(txt)
            }
        }

        fun text(txt: Component) {
            ren += {
                val width = MultiLineLabel.create(mcFont, txt, mcUIWidth)
                    .renderLeftAligned(guiGraphics, nowX, nowY, 10, txt.style.color?.value ?: WHITE)
                //val width = guiGraphics.drawString(mcFont, txt, nowX, nowY, txt.style.color?.value ?: WHITE)
                nowX += mcFont.width(txt)
            }
        }

        fun player(skin: ResourceLocation) {
            ren += {
                PlayerFaceRenderer.draw(guiGraphics, skin, nowX, nowY, 12)
                nowX += 16
            }
        }

        fun item(item: Item) {
            item(item to 1)
        }

        fun item(item: LiteItemStack) {
            ren += {
                nowX += 5
                guiGraphics.matrixOp {
                    pose.translate(nowX.toFloat(), nowY.toFloat() + 4, 0f)
                    guiGraphics.matrixOp {
                        guiGraphics.renderItemStack(item.full, 12, 12)
                    }
                    //渲染数量
                    if (item.second > 1) {

                        pose.scale(0.9f, 0.9f, 1f)
                        pose.translate(2f, 0f, 1f)
                        guiGraphics.drawString(mcFont, "${item.second}", 0, 0, WHITE)
                    }

                }
                nowX += 8
            }
        }

        fun icon(name: String) {
            ren += {
                guiGraphics.matrixOp {
                    pose.translate(0f, -1f, 0f)
                    guiGraphics.blit(Icons[name], nowX, nowY, 0f, 0f, 10, 10, 10, 10)
                }
                nowX += 12
            }
        }

        //鼠标右键
        fun rmb() {
            icon("rmb")
        }

        //鼠标左键
        fun lmb() {
            icon("lmb")
        }

        /*fun key(name: String) {
            ren += {
                guiGraphics.matrixOp {
                    pose.translate(0f, -1f, 0f)
                    guiGraphics.blit(KeyboardIcons[name], nowX,nowY, 0f, 0f, 10, 10, 10, 10)
                }
                nowX += 12
            }
        }*/

        fun ret() {
            ren += {
                if (nowX > width)
                    width = nowX
                if (nowY > height)
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
        val ctx = Context(guiGraphics, widthHeight.first, widthHeight.second, x, y)
        guiGraphics.matrixOp {
            renderers.forEach { it(ctx) }
        }
        widthHeight = ctx.width to ctx.height
    }
}

fun richText(x: Int = 0, y: Int = 0, builder: RichText.Builder.() -> Unit): RichText {
    return RichText.Builder(x, y).apply(builder).build()
}
