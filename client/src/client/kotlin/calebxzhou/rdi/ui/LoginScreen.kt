package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.ihq.net.protocol.account.LoginSPacket
import calebxzhou.rdi.ihq.net.protocol.general.OkCPacket
import calebxzhou.rdi.log
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.ROkCancelScreen
import calebxzhou.rdi.ui.component.RPasswordEditBox
import calebxzhou.rdi.util.*
import io.netty.channel.ChannelFuture
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.network.Connection
import net.minecraft.network.chat.Component

import kotlin.concurrent.Volatile

class LoginScreen : ROkCancelScreen(RTitleScreen(), "登录"){
    lateinit var qqBox: REditBox
    lateinit var pwdBox: RPasswordEditBox

    @Volatile
    var connection: Connection? = null
    var channelFuture: ChannelFuture? = null
    var connectThread: Thread? = null

    @Volatile
    var aborted: Boolean = false
    var status: Component = mcText("准备连接...")
    override fun onSubmit() {

        log.info("开始连接")
        IhqClient.sendPacket(LoginSPacket(qqBox.value,pwdBox.value))
        /*
         mc.clearLevel(this)
        mc.prepareForMultiplayer()
        mc.quickPlayLog().setWorldData(QuickPlayLog.Type.MULTIPLAYER, "RDI", Const.VERSION_STR)
        connectThread = thread(name = "RDI Server Connector") {
            connection = Connection(PacketFlow.CLIENTBOUND)
            channelFuture = Connection.connect(Const.SERVER_INET_ADDR, true, connection)
            channelFuture?.syncUninterruptibly()
            connection?.setListener(
                ClientHandshakePacketListenerImpl(
                    connection,
                    mc,
                    Const.SERVER_DATA,
                    RTitleScreen(),
                    false,
                    null
                ) {
                    this.status = it
                })
            connection?.send(ClientIntentionPacket(Const.SERVER_ADDR, Const.SERVER_PORT, ConnectionProtocol.LOGIN))
            LocalStorage += "qq" to qqBox.value
            LocalStorage += "pwd" to pwdBox.value


            connection?.send(LoginC2SPacket(qqBox.value, pwdBox.value))
        }
        connectThread?.setUncaughtExceptionHandler { t, e ->
            e.printStackTrace()
            dialogErr("连接服务器失败，请检查网络连接")

        }*/

    }

    override fun init() {
        qqBox = REditBox("QQ号", width / 2 - 50, 80, 100).also { registerWidget(it) }
        pwdBox = RPasswordEditBox("RDI密码", width / 2 - 50, 120, 100).also { registerWidget(it) }
        qqBox.value = LocalStorage["qq"]
        pwdBox.value = LocalStorage["pwd"]
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
        drawTextAtCenter(guiGraphics, status, height - 40)
        super.render(guiGraphics, mouseX, mouseY, partialTick)
    }

    fun onResponse(packet: OkCPacket) {
        showToast(packet.msg)

        //mc goScreen  IslandScreen()
    }

    private fun stopConnect() {
        this.aborted = true
        connectThread?.stop()
        connectThread = null

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