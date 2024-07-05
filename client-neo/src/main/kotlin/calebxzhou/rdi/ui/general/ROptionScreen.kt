package calebxzhou.rdi.ui.general

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.screens.Screen

class ROptionScreen(private val prevScreen: Screen?, vararg operations: Pair<String,(ROptionScreen)->Unit>) : RScreen("请选择操作") {
    private val oprs = operations.toMutableList()
    override fun init() {
        var startY = height/2-oprs.size*10
        var longestBtnWidth = 0
        oprs.forEach { opr ->
            val btn = RButton(width/2,startY,opr.first) { opr.second(this) }
            if(longestBtnWidth < btn.width)
                longestBtnWidth = btn.width
            registerWidget(btn)
            startY += btn.height
        }
        super.init()
    }

    override fun onClose() {
        if(prevScreen != null)
            mc goScreen prevScreen
    }
}