package calebxzhou.rdi.ui.general

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen

class ROptionScreen(private vararg val operations: Pair<String,(ROptionScreen)->Unit>) : RScreen("请选择操作") {
    override fun init() {
        var startY = height/2-operations.size*10
        var longestBtnWidth = 0
        operations.forEach { opr ->
            val btn = RButton(width/2,startY,opr.first) { opr.second(this) }
            if(longestBtnWidth < btn.width)
                longestBtnWidth = btn.width
            registerWidget(btn)
            startY += btn.height
        }
        super.init()
    }
}