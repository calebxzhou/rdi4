package calebxzhou.rdi.ui.component

import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.Icons.draw
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item


class RIconButton(
    val icon: String?=null,
    val item: Item?=null,
    val text: String ="",
    val comp: MutableComponent=text.toMcText(),
    hoverText: String = "",
    x: Int=0,
    y: Int=0,
    val size: Int=64,
    val onClick: (Button) -> Unit
) : RButton(comp, x, y, mcTextWidthOf(comp) + 24, 20, hoverText,onClick) {
    val textWidget = MultiLineTextWidget(comp,mcFont).apply { setMaxWidth(mcUIWidth) }
    init {
        width = textWidget.width+24
    }
    override fun renderWidget(gg: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

        //渲染文字
        gg.matrixOp {
            translate(0f,0f,1f)
            scale(0.90f,0.90f,1f)

            translate(x.toFloat()/0.9f+18,y.toFloat()/0.9f+2 ,1f)
            textWidget.render(gg,pMouseX,pMouseY,pPartialTick)
        }

        icon?.let {
            Icons[icon].draw(gg,x,y,size)
        }
        item?.let {
            gg.matrixOp {
                gg.pose().translate(x.toFloat()-2, y.toFloat(), 1f)
                gg.renderItemStack(itemStack = item.defaultInstance, width =  16, height =  16)
            }
        }
    }
}