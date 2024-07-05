package calebxzhou.rdi.ui.general

import calebxzhou.rdi.exception.RGeneralException
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants.KEY_NUMPADENTER
import com.mojang.blaze3d.platform.InputConstants.KEY_RETURN
import net.minecraft.client.gui.screens.Screen

abstract class ROkCancelScreen(val prevScreen: Screen, name: String) : RScreen(name) {
    lateinit var okBtn : RButton
    lateinit var cancelBtn : RButton


    override fun tick() {
        if (mc pressingKey KEY_RETURN || mc pressingKey KEY_NUMPADENTER) {
            onPressEnterKey()
        }

        super.tick()
    }

    override fun init() {
        okBtn = RButton(width/2-50, height-20, 50, "确定") { trySubmit() }.also { registerWidget(it) }
        cancelBtn = RButton(width/2, height-20, 50, "取消") { onClose() }.also { registerWidget(it) }
        super.init()

    }
    open fun onPressEnterKey() {
        if (okBtn.visible)
            trySubmit()
    }

    private fun trySubmit() = try {
        onSubmit()
    }catch (e: RGeneralException) {
        alertErr("错误：${e.localizedMessage}")
    }
    catch (e: Exception) {
        alertErr("错误：$e")
        e.printStackTrace()
    }

    abstract fun onSubmit()

    override fun shouldCloseOnEsc(): Boolean {
        return true
    }

    override fun onClose() {
        mc goScreen prevScreen
    }
}