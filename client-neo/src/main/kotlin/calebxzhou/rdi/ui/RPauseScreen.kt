package calebxzhou.rdi.ui

import calebxzhou.rdi.tutorial.TutorialManager
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.drawTextAtCenter
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.gui.screens.OptionsScreen

//回城 岛屿管理 工具箱 设置 退出
class RPauseScreen : RScreen("暂停") {
    override fun init() {
        gridLayout {
            button("工具箱"){
                mc goScreen IslandScreen()
            }
            button("设置"){
                mc goScreen OptionsScreen(this@RPauseScreen,mc.options)
            }
            button("退出"){
                mc.level?.disconnect()
                if(mc.isLocalServer){
                    if(TutorialManager.isDoingTutorial){
                        TutorialManager.quitTutorial()
                    }
                    mc.clearLevel(GenericDirtMessageScreen(mcText("存档中，请稍候")))
                } else{
                    mc.clearLevel()
                }
                mc goScreen RTitleScreen()
            }
        }.buildForIteration { registerWidget(it) }

        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if(TutorialManager.isDoingTutorial){
            drawTextAtCenter(guiGraphics,"入门教程模式")
        }

    }
}