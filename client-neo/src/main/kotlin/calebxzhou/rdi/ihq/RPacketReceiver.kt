package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.CPacket
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


/**
 * Created  on 2023-07-03,9:14.
 */
class RPacketReceiver : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, packet: Any) {
        if(packet is CPacket){
            packet.process()
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

}