package calebxzhou.rdi.ihq.net

import calebxzhou.rdi.ihq.net.protocol.CPacket
import calebxzhou.rdi.util.mc
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


/**
 * Created  on 2023-07-03,9:14.
 */
class RPacketReceiver : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, packet: Any) {
        when (packet) {

            is CPacket -> {
                packet.process()
            }


        }

    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

}