package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.toastOk
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel

object IhqClient {
    val connection = Bootstrap()
        .group(NioEventLoopGroup())
        .channel(NioDatagramChannel::class.java)
        .option(ChannelOption.SO_BROADCAST, true)
        .handler(object : ChannelInitializer<NioDatagramChannel>() {
            override fun initChannel(ch: NioDatagramChannel) {
                ch.pipeline().apply {
                    addLast("decoder", RPacketDecoder())
                    addLast("packet_handler", RPacketReceiver())
                    addLast("encoder", RPacketEncoder())
                }
            }
        })
        .bind(0)
        .syncUninterruptibly()

    var reqId = 0.toByte()
    val responseHandlerMap = hashMapOf<Byte,(ResponseCPacket)->Unit>()
    //成功->前画面 失败->提示
    val OK_CLOSE_ERR_ALERT_HANDLER: (ResponseCPacket) -> Unit = { packet: ResponseCPacket ->
        if(!packet.ok){
            alertErr(packet.data)
        }else{
            toastOk(packet.data)
            mc.screen?.onClose()
        }
    }
    fun send(packet: SPacket, responseHandler: (ResponseCPacket) -> Unit = OK_CLOSE_ERR_ALERT_HANDLER) {
        connection.channel().writeAndFlush(packet)
        responseHandlerMap += reqId to responseHandler
        reqId++
    }

    fun handleResponse(packet: ResponseCPacket) {

        responseHandlerMap[packet.reqId]?.invoke(packet)
    }

}