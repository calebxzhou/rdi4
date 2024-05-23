package calebxzhou.rdi.ui

import calebxzhou.rdi.core.util.GraphicUtils
import calebxzhou.rdi.util.DialogUtils
import calebxzhou.rdi.util.ServerConnector
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundSource
import java.util.*

class  RdiTitleScreen : Screen(Component.literal("主界面")) {

    public override fun init() {
        //关闭音乐
        Minecraft.getInstance().options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0)
        //检查RDID文件是否存在，不存在则进入注册界面
        //if(!RdiFiles.OptionFile.exists()){
            // minecraft!!.setScreen(RdidInitScreen())
        //}
    }
    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        GraphicUtils.clear()

        guiGraphics.drawString(this.font, "按Enter键", width / 2 - 18, height / 2, 0,false);
        guiGraphics.drawString(this.font, "D=单人 M=mod列表 S=设置", width / 2 - 18, height-20, 0,false);

        //font.draw(matrices,  "△t=${"%.2f".format(LoadProgressRecorder.getLoadTimeSeconds())}s >${"%.2f".format(LoadProgressRecorder.getLoadTimePercentBeyondPlayers())}%", 0f, 0f, 0x00FFFFFF)
    }

    override fun tick() {
        val handle = Minecraft.getInstance().window.window
        /*if (InputConstants.isKeyDown(handle, InputConstants.KEY_0)) {
            if (RdiSharedConstants.DEBUG) minecraft!!.setScreen(JoinMultiplayerScreen(this))
            return
        }*/
        if (InputConstants.isKeyDown(handle, InputConstants.KEY_D)) {
            minecraft!!.setScreen(SelectWorldScreen(this))
            return
        }
        if (InputConstants.isKeyDown(handle, InputConstants.KEY_M)) {
            try {
                minecraft!!.setScreen(
                    Class.forName("com.terraformersmc.modmenu.gui.ModsScreen").getConstructor(
                        Screen::class.java
                    ).newInstance(this) as Screen
                )
            } catch (e: Exception) {
                DialogUtils.showMessageBox("error", "必须安装ModMenu模组以使用本功能！！")
                e.printStackTrace()
            }
            return
        }
        if (InputConstants.isKeyDown(handle, InputConstants.KEY_S)) {
            minecraft!!.setScreen(OptionsScreen(this, minecraft!!.options))
            return
        }
        /*if (InputConstants.isKeyDown(handle, InputConstants.KEY_P)) {
            minecraft!!.setScreen(PasswordScreen())
            return
        }
        if (InputConstants.isKeyDown(handle, InputConstants.KEY_0)) {
            minecraft!!.setScreen(JoinMultiplayerScreen(this))
            return
        }*/
        if (InputConstants.isKeyDown(handle, InputConstants.KEY_RETURN) || InputConstants.isKeyDown(
                handle,
                InputConstants.KEY_NUMPADENTER
            )
        ) {
            ServerConnector.connect(this)
        }
    }




}