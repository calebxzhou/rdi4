package calebxzhou.rdi.net

import calebxzhou.rdi.net.protocol.game.SGamePacket
import calebxzhou.rdi.service.PlayerService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler



/**
 * Created  on 2023-07-03,9:14.
 */
class RPacketReceiver : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, packet: Any){
        if(packet is SGamePacket){
            PlayerService.onlinePlayers[ctx]?.let { player ->
                packet.process(player)
            }
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) { // (4)

        cause.printStackTrace()
        ctx.close()
    }

}