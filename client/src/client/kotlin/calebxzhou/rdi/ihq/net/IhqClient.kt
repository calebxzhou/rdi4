package calebxzhou.rdi.ihq.net

import calebxzhou.rdi.ihq.net.protocol.CPacket
import calebxzhou.rdi.ihq.net.protocol.SPacket
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

    fun sendPacket(packet: SPacket){
        connection.channel().writeAndFlush(packet)
    }
}