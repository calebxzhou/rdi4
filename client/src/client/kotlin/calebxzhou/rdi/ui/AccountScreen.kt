package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.general.ROkCancelScreen
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.multiplayer.resolver.ServerAddress

class AccountScreen : ROkCancelScreen(RTitleScreen(),"输入昵称、密码、档案号（MC-UUID）"){
    lateinit var nameBox : REditBox
    lateinit var pwdBox : REditBox
    lateinit var uuidBox : REditBox
    lateinit var regenUuidBtn: RButton
    /*lateinit var skinBox : REditBox
    lateinit var capeBox : REditBox*/
    override fun init() {
        nameBox = REditBox("游戏昵称", width / 2 - 50, 80, 100).also { registerWidget(it) }
        pwdBox = RPasswordEditBox("密码", width / 2 - 50, 120, 100).also { registerWidget(it) }
        uuidBox = REditBox("档案编号(mc-uuid)", width / 2 - 50, 160, 200).also { registerWidget(it) }
        regenUuidBtn = RButton(width-80,height-40,50,"重置档案号"){uuidBox.value=
            Utils.createUuid(nameBox.value).toString()
        }.also { registerWidget(it) }
        nameBox.value = LocalStorage["name"] ?: mc.user.name
        pwdBox.value = LocalStorage["pwd"] ?: ""
        uuidBox.value = LocalStorage["uuid"] ?: Utils.createUuid(nameBox.value).toString()
        super.init()
    }
    override fun onSubmit() {
        if(!uuidBox.value.isValidUuid()){
            dialogErr("错误的档案号格式")
            return
        }
        LocalStorage += "name" to nameBox.value
        LocalStorage += "pwd" to pwdBox.value
        LocalStorage += "uuid" to uuidBox.value
        ConnectScreen.startConnecting(AccountScreen(), mc, ServerAddress(Const.SERVER_ADDR, Const.SERVER_PORT), Const.SERVER_DATA,false)
    }
}