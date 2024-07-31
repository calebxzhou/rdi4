package calebxzhou.rdi.ui

import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcUIHeight
import calebxzhou.rdi.util.mcUIWidth
import kotlinx.serialization.json.JsonNull.content
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractScrollWidget
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.client.gui.layouts.SpacerElement
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import java.util.function.Consumer

fun docWidget(x:Int = 0,y:Int = 20,width: Int = mcUIWidth-5 , height: Int = mcUIHeight-25, builder: RDocWidget.Builder.()->Unit) : RDocWidget.Builder{
    return RDocWidget.Builder(x, y, width, height).apply(builder)
}
class RDocWidget(pX: Int, pY: Int, pWidth: Int, pHeight: Int, val content: Content) :
    AbstractScrollWidget(pX, pY, pWidth, pHeight, mcText("")) {
    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, this.content.narration)
    }

    override fun getInnerHeight(): Int {
        return this.content.container.height
    }

    override fun scrollRate(): Double {
        return 9.0
    }


    override fun renderContents(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        val i = this.y + this.innerPadding()
        val j = this.x + this.innerPadding()
        pGuiGraphics.pose().pushPose()
        pGuiGraphics.pose().translate(j.toDouble(), i.toDouble(), 0.0)
        this.content.container.visitWidgets { widget: AbstractWidget ->
            widget.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        }
        pGuiGraphics.pose().popPose()
    }

    data class Content(val container: GridLayout, val narration: Component)

    class Builder(val x: Int,val y:Int,val width: Int,val height: Int) {
        private val grid = GridLayout()
        private val helper: GridLayout.RowHelper
        private val alignHeader: LayoutSettings
        private val narration: MutableComponent = Component.empty()

        init {
            grid.defaultCellSetting().alignHorizontallyLeft()
            this.helper = grid.createRowHelper(1)
            helper.addChild(SpacerElement.width(width))
            this.alignHeader = helper.newCellSettings().alignHorizontallyCenter().paddingHorizontal(32)
        }


        fun spacer(pHeight: Int) {
            helper.addChild(SpacerElement.height(pHeight))
        }

        fun build(): RDocWidget {
            grid.arrangeElements()
            return RDocWidget(x,y,width,height,Content(this.grid, this.narration))
        }

        //一级标题 居中 绿粗
        fun h1(str: String) {
            helper.addChild(
                MultiLineTextWidget(
                    mcText(str).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GREEN),
                    mcFont
                ).setMaxWidth(this.width - 64).setCentered(true),
                this.alignHeader.paddingTop(8).paddingBottom(8)
            )
            narration.append(content).append("\n")
        }

        //二级标题 居左 蓝
        fun h2(str: String) {
            helper.addChild(
                MultiLineTextWidget(
                    mcText(str).withStyle(ChatFormatting.AQUA),
                    mcFont
                ).setMaxWidth(this.width - 64).setCentered(false),
                helper.newCellSettings().paddingTop(4).paddingBottom(4)
               // this.alignHeader
            )
            narration.append(content).append("\n")
        }

        //正文
        fun p(str: String) {
            helper.addChild(
                MultiLineTextWidget(mcText("　　").append(mcText(str)), mcFont).setMaxWidth(this.width),
                helper.newCellSettings().paddingTop(2).paddingBottom(2)
            )
            narration.append(content).append("\n")
        }
        //图片
        fun img(path: String){
            helper.addChild(
                ImageWidget(64,64, ResourceLocation("rdi","textures/${path}.png")),
                this.alignHeader.paddingTop(3).paddingBottom(3)
            )
        }
    }

}