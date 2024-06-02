package calebxzhou.rdi.ui.component

import calebxzhou.rdi.exception.RAccountException
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import java.net.ConnectException

abstract class ROkCancelScreen(val prevScreen: RScreen, name: String) : RScreen(name) {
    lateinit var okBtn : RButton
    lateinit var cancelBtn : RButton


    override fun tick() {
        when {
            //当按下回车键
            InputConstants.isKeyDown(mcWindowHandle, InputConstants.KEY_RETURN) || InputConstants.isKeyDown(
                mcWindowHandle,
                InputConstants.KEY_NUMPADENTER
            ) -> {
                onPressEnterKey()
            }
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
    }catch (e: RAccountException) {
        dialogErr("账户错误：${e.localizedMessage}")
    }catch (e: ConnectException){
        dialogErr("请检查网络连接")
        e.printStackTrace()
    }
    catch (e: Exception) {
        dialogErr("错误：$e")
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