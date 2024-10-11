package calebxzhou.rdi.tfc

import calebxzhou.rdi.ui.RScreenRectTip
import calebxzhou.rdi.ui.rectTip
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import net.dries007.tfc.client.ClientHelpers
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.client.screen.button.KnappingButton
import net.minecraft.client.gui.GuiGraphics
import java.awt.SystemColor.text


object RTfcKnappingScreen {

    fun knappingIndexToPosition(index: Int): String {
        val prefix = index / 5 + 1
        val suffix = 'a' + (index % 5)
        return "$prefix$suffix"
    }
    @JvmStatic
    fun onRender(screen: KnappingScreen,guiGraphics: GuiGraphics, mouseX:Int,  mouseY:Int){

        /*screen.children().filterIsInstance<KnappingButton>().forEachIndexed { i,btn->
            val color = if(!btn.visible) RED else if(btn.isHoveredOrFocused) LIGHT_YELLOW else GREEN
            guiGraphics.drawString(mcFont,knappingIndexToPosition(i),btn.x+4,btn.y+4,color,true)
0  5  10  15  20
1  6  11  16  21
2  7  12  17  22
3  8  13  18  23
4  9  14  19  24
        }*/

        //guiGraphics.drawCenteredString(mcFont,"制作石斧头子: 点击1a 3a 4a 5a 5b 5d 5e 4e 3e 1e",screen.width/2, 30, WHITE)
        guiGraphics.drawCenteredString(mcFont,"点击灰色箭头,查看更多的敲击石头方法.",screen.width/2, 20, WHITE)
       
       /* val pose = guiGraphics.pose()
        pose.pushPose()
        pose.translate(0f,0f,1f)
        pose.scale(0.75f,0.75f,1f)
        RTfcRecipeStorage.rockKnappingRecipes.forEachIndexed { index, knappingRecipe ->
            val item = knappingRecipe.getResultItem(null)
            val y = 5 + index * 8
            guiGraphics.renderItem(item,20, y)
            val pattern =
                (0..24).filter { knappingRecipe.pattern.get(it) }.joinToString(",") { knappingIndexToPosition(it) }
            val text = item.hoverName.copy() + "=" + pattern
            guiGraphics.drawString(mcFont,text,35, y, WHITE)
        }
        pose.popPose()*/
    }
}