package calebxzhou.rdi.ui

import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.ROptionScreen
import calebxzhou.rdi.ui.layout.RGridLayout
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.gui.screens.OptionsScreen

//回城 岛屿管理 工具箱 设置 退出
class RPauseScreen : RScreen("暂停") {
    override fun init() {
        RGridLayout().apply {
            row(6,
                RButton("前往主城"){
                    mc.connection?.sendCommand("spawn")
                },
                RButton("岛屿管理"){
                    mc goScreen IslandScreen()
                },
                RButton("工具箱"){
                    mc goScreen IslandScreen()
                },
                RButton("设置"){
                    mc goScreen OptionsScreen(this@RPauseScreen,mc.options)
                },

                RButton("退出"){
                    mc.level?.disconnect()
                    if(mc.isLocalServer){
                        mc.clearLevel(GenericDirtMessageScreen(mcText("存档中，请稍候")))
                    } else{
                        mc.clearLevel()
                    }
                    mc goScreen RTitleScreen()
                },
            )
        }.visitWidgets { registerWidget(it) }
        super.init()
    }
}