package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.tutorial.Tutorial
import calebxzhou.rdi.ui.RScreenRectTip
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.formScreen
import calebxzhou.rdi.ui.general.optionScreen
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.drawTextAtCenter
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.gui.screens.OptionsScreen

//回城 岛屿管理 工具箱 设置 退出
class RPauseScreen : RScreen("暂停") {
    override fun init() {
        gridLayout {

            button("设置"){
                mc goScreen OptionsScreen(this@RPauseScreen,mc.options)
            }
            button("退出"){
                mc.level?.disconnect()
                if(Tutorial.isDoingTutorial){
                    Tutorial.now?.quit()
                    return@button
                }
                if(mc.isLocalServer){

                    mc.clearLevel(GenericDirtMessageScreen(mcText("存档中，请稍候")))
                } else{
                    mc.clearLevel()
                }
                Banner.reset()
                OmniNavi.reset()
                mc goScreen RTitleScreen()

            }
        }.buildForIteration { registerWidget(it) }

        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        if(Tutorial.isDoingTutorial){
            drawTextAtCenter(guiGraphics,"入门教程模式")
        }

    }
}