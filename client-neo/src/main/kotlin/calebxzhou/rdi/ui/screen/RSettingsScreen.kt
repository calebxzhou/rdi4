package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI
import net.minecraft.client.Options
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.*
import net.minecraft.client.gui.screens.controls.ControlsScreen
import net.minecraft.client.gui.screens.packs.PackSelectionScreen
import net.minecraftforge.client.gui.ModListScreen

class RSettingsScreen(val prevScreen: Screen, val options: Options): RScreen("设置") {
    override fun init() {
        /*Account.now?.let { account ->
            RPlayerHeadButton(account, mcUIWidth/2- (mcFont.width(mcText(account.name))+14+5)/2,40,){}.also { registerWidget(it) }
            gridLayout(this,hAlign = HAlign.CENTER,y=80) {
                iconButton("basic_info",text = "资料"){
                    mc goScreen ControlsScreen(this@RSettingsScreen,options)
                }
                iconButton("clothes",text = "皮肤"){
                    mc goScreen ControlsScreen(this@RSettingsScreen,options)
                }
            }
        }*/
        gridLayout(this, hAlign = HAlign.CENTER,y=120, maxColumns = 4) {

            button("plugin",text = "模组"){
                mc go ModListScreen(this@RSettingsScreen)
            }
            button("resources",text = "资源包"){
                mc go PackSelectionScreen(mc.resourcePackRepository,{
                    options.updateResourcePacks(it)
                    mc go this@RSettingsScreen
                },mc.resourcePackDirectory, mcText("选择资源包"))
            }
            button("video",text = "画质"){
                mc go SodiumOptionsGUI(this@RSettingsScreen)
            }

        }
        gridLayout(this, hAlign = HAlign.CENTER,y=160){
            button("sound",text = "音频"){
                mc go SoundOptionsScreen(this@RSettingsScreen,options)
            }
            button("controller",text = "键位"){
                mc go ControlsScreen(this@RSettingsScreen,options)
            }
            button("accessibility",text = "辅助"){
                mc go AccessibilityOptionsScreen(this@RSettingsScreen,options)
            }
        }


        super.init()
    }

    override fun onClose() {
        mc go prevScreen
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {

    }
    override fun tick() {
        if(mc pressingKey InputConstants.KEY_L){
            mc go ChatOptionsScreen(this@RSettingsScreen,options)
        }
        super.tick()
    }
    override fun removed() {
        options.save()
    }
}