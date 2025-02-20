package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.go
import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.screens.Screen

class RTutorialScreen(private val prevScreen: Screen) : RScreen("互动教程") {
    val startX = 20
    val startY = 10
    override fun init() {
        var x = startX
        var y = startY

        super.init()
    }

    override fun onClose() {
        mc go prevScreen
    }
}