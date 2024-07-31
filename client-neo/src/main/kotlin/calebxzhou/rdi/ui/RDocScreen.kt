package calebxzhou.rdi.ui

import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.RTextButton
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.util.mcText
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen

/*
文档型画面
<-返回            文档标题
内容...
内容...
内容...

控制1     控制2        控制3      控制4.....
 */
/* DSL
{
    h1("一级标题")
    h2("二级标题")
    p("段落")
    img("3.png")
    item("minecraft:.....")
    item(Items.STONE)
    block(...)
    block(...)
    control("控制1"){ toastOk("已点击控制1",it.screen) }
    control("控制2"){ toastOk("已点击控制2",it.screen) }
}
 */
fun docScreen(prevScreen: Screen, title: String) {

}

