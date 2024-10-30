package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.screens.Screen

class RTutorialScreen(private val prevScreen: Screen) :RScreen("互动教程"){

    override fun onClose() {
        mc goScreen prevScreen
    }
}