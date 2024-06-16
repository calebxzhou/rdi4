package calebxzhou.rdi.net

import calebxzhou.rdi.net.protocol.CPacket
import calebxzhou.rdi.util.clientIp
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder

class RPacketEncoder : MessageToMessageEncoder<CPacket>() {
    override fun encode(ctx: ChannelHandlerContext, packet: CPacket, out: MutableList<Any>) {
        RPacketSet.getPacketId(packet.javaClass)?.let { packetId->
            val msg = Unpooled.buffer()
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