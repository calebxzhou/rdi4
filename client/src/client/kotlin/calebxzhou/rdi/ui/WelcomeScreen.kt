package calebxzhou.rdi.ui

import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class WelcomeScreen : Screen(mcText("欢迎")) {
    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        drawTextAtCenter(guiGraphics,"欢迎来到RDI服务器。",height/3)
        mcButton("登录",width/2-100,height/2-30,100,30){
            mc goScreen LoginScreen()
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }
}