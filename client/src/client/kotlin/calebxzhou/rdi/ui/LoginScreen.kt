package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.log
import calebxzhou.rdi.net.RdiLoginC2SPacket
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.ROkCancelScreen
import calebxzhou.rdi.util.dialogErr
import calebxzhou.rdi.util.drawTextAtCenter
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import io.netty.channel.ChannelFuture
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl
import net.minecraft.client.quickplay.QuickPlayLog
import net.minecraft.network.Connection
import net.minecraft.network.ConnectionProtocol
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.handshake.ClientIntentionPacket

import kotlin.concurrent.Volatile
import kotlin.concurrent.thread

class LoginScreen() : ROkCancelScreen(RTitleScreen(), "登录") {
    lateinit var qqBox : REditBox
    lateinit var pwdBox : REditBox

    @Volatile
    var connection: Connection? = null
    var channelFuture: ChannelFuture? = null
    var connectThread : Thread? = null
    @Volatile
    var aborted: Boolean = false
    var status: Component = mcText("准备连接...")
    override fun onSubmit() {
        //mc.clearLevel()
        mc.prepareForMultiplayer()
        mc.quickPlayLog().setWorldData(QuickPlayLog.Type.MULTIPLAYER, "RDI", Const.VERSION_STR)
        log.info("开始连接")
        connectThread = thread(name = "RDI Server Connector") {
            connection = Connection(PacketFlow.CLIENTBOUND)
            channelFuture = Connection.connect(Const.SERVER_INET_ADDR,true,connection)
            channelFuture?.syncUninterruptibly()
            connection?.setListener(ClientHandshakePacketListenerImpl(connection,mc,Const.SERVER_DATA,RTitleScreen(),false,null) {
                this.status = it
            })
            connection?.send(ClientIntentionPacket(Const.SERVER_ADDR,Const.SERVER_PORT,ConnectionProtocol.LOGIN))
            connection?.send(RdiLoginC2SPacket(qqBox.value,pwdBox.value))
        }
        connectThread?.setUncaughtExceptionHandler { t, e ->
            e.printStackTrace()
            dialogErr("连接服务器失败，请检查网络连接")

        }

    }

    override fun init() {
        qqBox= REditBox("RDI账号", width/2-50, 80, 100).also { registerWidget(it) }
        pwdBox=  REditBox("密码", width/2-50, 150, 100).also { registerWidget(it) }
        super.init()
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

    override fun onClose() {
        stopConnect()
        super.onClose()
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        drawTextAtCenter(guiGraphics,status,height-40)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }
    private fun stopConnect() {
        this.aborted = true
        connectThread?.stop()
        connectThread=null

        if (this.channelFuture != null) {
            channelFuture!!.cancel(true)
            this.channelFuture = null
        }

        if (this.connection != null) {
            connection!!.disconnect(mcText("停止连接"))
        }
        status = mcText("已取消登录")
    }
}