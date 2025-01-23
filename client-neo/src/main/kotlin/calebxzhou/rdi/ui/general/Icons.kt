package calebxzhou.rdi.ui.general

import calebxzhou.rdi.util.matrixOp
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

object Icons {
    operator fun get(name: String): ResourceLocation {
        return ResourceLocation("rdi","textures/gui/icons/${name}.png")
    }
    fun ResourceLocation.draw(guiGraphics: GuiGraphics,x:Int,y:Int,size: Int=64){
        guiGraphics.matrixOp {
            translate(0f,0f,1f)
            scale(0.20f,0.20f,1f)
            translate(x.toFloat()*4,y.toFloat()*4,1f)
            guiGraphics.blit(this@draw, x, y, 0f, 0f, size,size,size,size)
        }
    }
}