package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.tutorial.Chapter
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.RTextButton
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen

class RTutorialScreen(private val prevScreen: Screen) : RScreen("互动教程") {
    val startX = 20
    val startY = 10
    override fun init() {
        var x = startX
        var y = startY
        Chapter.ALL.forEach {
            val chapterBtn = RTextButton(mcText(it.name), x, y).also { registerWidget(it) }
            y += chapterBtn.height
            val grid = gridLayout(this, x, y) {
                it.tutorials.forEach { tutorial ->
                    button(mcText(tutorial.name).withStyle(
                        if (tutorial.isDone)
                            ChatFormatting.GREEN
                        else
                            ChatFormatting.WHITE
                    )) {
                        tutorial.start()
                    }
                }
            }
            y += grid.height+5
        }
        super.init()
    }

    override fun onClose() {
        mc goScreen prevScreen
    }
}