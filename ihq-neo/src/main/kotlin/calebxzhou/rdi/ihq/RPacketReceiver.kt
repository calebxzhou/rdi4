package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.util.account
import calebxzhou.rdi.ihq.util.err
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * Created  on 2023-07-03,9:14.
 */
class RPacketReceiver : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, packet: Any) {
        if (packet is SPacket) {
            GlobalScope.launch {
                if (packet is SAuthedPacket) {
                    ctx.account?.let {

                        packet.process(ctx, it)
                    } ?: let {
                        ctx.err("未登录")
                    }
                } else {

                    packet.process(ctx)
                }
            }
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) { // (4)

        cause.printStackTrace()
        ctx.close()
    }

}