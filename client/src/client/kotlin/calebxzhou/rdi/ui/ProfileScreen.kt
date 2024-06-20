package calebxzhou.rdi.ui

import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.client.gui.screens.inventory.InventoryScreen

class ProfileScreen(val account: RAccount) : RScreen("个人信息管理"){
    lateinit var nameBtn : RTextButton
    lateinit var skinBtn : RTextButton
    lateinit var capeBtn : RTextButton

    override fun init() {
        nameBtn = RTextButton(width/2, height/5+5,account.name){
            mc goScreen RChangeNameScreen(ProfileScreen(account))
        }.also { registerWidget(it) }
        val skincape = account.skin.takeLast(16)+"/"+account.cape.takeLast(16)
        skinBtn = RTextButton(width/2, height/3,"皮肤披风：$skincape"){

        }.also { registerWidget(it) }
        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        PlayerFaceRenderer.draw(guiGraphics, account.skinLocation, width/2-50, height/5, 20)
        drawTextAtCenter(guiGraphics, "RDID     ${account.id}", height / 2)
        drawTextAtCenter(guiGraphics, "UUID     ${account.uuid}", height / 2 + 30)
        mc.player?.let {
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics, 200, 200, 30, mouseX.toFloat(), mouseY.toFloat(),
                it
            )
        }
    }
}