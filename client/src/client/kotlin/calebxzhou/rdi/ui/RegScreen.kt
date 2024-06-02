package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.exception.RAccountException
import calebxzhou.rdi.log
import calebxzhou.rdi.net.LoginC2SPacket
import calebxzhou.rdi.net.RegisterC2SPacket
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.ROkCancelScreen
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.util.dialogErr
import calebxzhou.rdi.util.isNumber
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import io.netty.channel.ChannelFuture
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl
import net.minecraft.client.quickplay.QuickPlayLog
import net.minecraft.network.Connection
import net.minecraft.network.ConnectionProtocol
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.handshake.ClientIntentionPacket
import kotlin.concurrent.thread

class RegScreen: ROkCancelScreen(RTitleScreen(), "注册"){
    @Volatile
    var connection: Connection? = null
    var channelFuture: ChannelFuture? = null
    var connectThread : Thread? = null

    lateinit var nameBox : REditBox
    lateinit var qqBox : REditBox
    lateinit var pwdBox : REditBox
    lateinit var cpwdBox : REditBox
    override fun init() {
        nameBox= REditBox("昵称", width/2-50, 40, 100).also { registerWidget(it) }
        qqBox= REditBox("QQ号", width/2-50, 80, 100).also { registerWidget(it) }
        pwdBox=  RPasswordEditBox("RDI密码", width/2-50, 120, 100).also { registerWidget(it) }
        cpwdBox=  RPasswordEditBox("确认密码", width/2-50, 160, 100).also { registerWidget(it) }
        super.init()
    }
    override fun onSubmit() {
        val name = nameBox.value
        val qq = qqBox.value
        val pwd = pwdBox.value
        val cpwd = cpwdBox.value
        if(name.isBlank()||qq.isBlank()||pwd.isBlank())
            throw RAccountException("输入框不能为空")
        if(pwd != cpwd)
            throw RAccountException("密码与确认密码不一致")
        if(!qq.isNumber() || qq.length !in 5 .. 10)
            throw RAccountException("QQ号格式错误")
        if(name.length>16)
            throw RAccountException("名称过长，不允许超过16字符")
        if(pwd.length !in 6..16)
            throw RAccountException("密码长度必须是6到16位")
        //开始注册
        mc.prepareForMultiplayer()
        mc.quickPlayLog().setWorldData(QuickPlayLog.Type.MULTIPLAYER, "RDI", Const.VERSION_STR)
        log.info("开始连接")
        connectThread = thread(name = "RDI Server Connector") {
            connection = Connection(PacketFlow.CLIENTBOUND)
            channelFuture = Connection.connect(Const.SERVER_INET_ADDR,true,connection)
            channelFuture?.syncUninterruptibly()
            connection?.setListener(
                ClientHandshakePacketListenerImpl(connection,
                    mc,Const.SERVER_DATA,RTitleScreen(),false,null) {

            })
            connection?.send(ClientIntentionPacket(Const.SERVER_ADDR,Const.SERVER_PORT, ConnectionProtocol.LOGIN))
            connection?.send(RegisterC2SPacket(nameBox.value,qqBox.value,pwdBox.value))
        }
        connectThread?.setUncaughtExceptionHandler { t, e ->
            e.printStackTrace()
            dialogErr("连接服务器失败，请检查网络连接")

        }



    }
    override fun onClose() {
        connectThread?.stop()
        connectThread=null

        if (this.channelFuture != null) {
            channelFuture!!.cancel(true)
            this.channelFuture = null
        }

        if (this.connection != null) {
            connection!!.disconnect(mcText("停止连接"))
        }
        super.onClose()
    }
    override fun tick() {
        this.connection?.let { connection ->
            if (connection.isConnected) {
                connection.tick()
            } else {
                connection.handleDisconnection()
            }
        }
        super.tick()
    }
}