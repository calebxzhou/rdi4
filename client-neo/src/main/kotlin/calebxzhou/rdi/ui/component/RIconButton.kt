package calebxzhou.rdi.ui.component

import calebxzhou.rdi.ui.general.Icons
import calebxzhou.rdi.ui.general.renderItemStack
import calebxzhou.rdi.util.*
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripRenderHandler.pose
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.awt.SystemColor.text


class RIconButton(
    val icon: String?=null,
    val item: Item?=null,
    val text: String ="",
    val cpnt: MutableComponent=text.toMcText(),
    x: Int=0,
    y: Int=0,
    val onClick: (Button) -> Unit
) : RButton(cpnt, x, y, mcTextWidthOf(cpnt) + 24, 20, onClick) {

    override fun renderWidget(gg: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        icon?.let {
            gg.matrixOp {
                translate(0f,0f,1f)
                scale(0.20f,0.20f,1f)
                translate(x.toFloat()*4,y.toFloat()*4,1f)
                gg.blit(Icons[icon], x, y, 0f, 0f, 64, 64,64,64)
            }
        }
        item?.let {
            gg.matrixOp {
                gg.pose().translate(x.toFloat()+8, y.toFloat()+5f, 1f)
                gg.renderItemStack(item.defaultInstance, 14, 14)
            }
        }
        //渲染文字
        gg.matrixOp {
            translate(0f,0f,1f)
            scale(0.90f,0.90f,1f)
            translate(x.toFloat()/0.9f+18,y.toFloat()/0.9f+2.8f,1f)
            gg.drawString(
                mcFont, cpnt,0,0,if (isHoveredOrFocused)
                    ChatFormatting.AQUA.color ?: 16777215
                else
                    16777215
            )
        }
    }
}