package calebxzhou.rdi.net

import calebxzhou.rdi.net.protocol.game.CGamePacket
import calebxzhou.rdi.util.clientIp
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder
import net.minecraft.network.FriendlyByteBuf

class RPacketEncoder : MessageToMessageEncoder<CGamePacket>() {
    override fun encode(ctx: ChannelHandlerContext, packet: CGamePacket, out: MutableList<Any>) {
        RPacketSet.getPacketId(packet.javaClass)?.let { packetId->
            val msg = FriendlyByteBuf(Unpooled.buffer())
            //写包ID
            msg.writeByte(packetId)
            packet.write(msg)
            val address = ctx.clientIp
            out += DatagramPacket(msg.retain(), address)
        }
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}