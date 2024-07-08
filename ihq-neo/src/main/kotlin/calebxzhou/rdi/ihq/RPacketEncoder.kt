package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.ihq.util.clientIp
import calebxzhou.rdi.ihq.util.reqId
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder

class RPacketEncoder : MessageToMessageEncoder<CPacket>() {
    override fun encode(ctx: ChannelHandlerContext, packet: CPacket, out: MutableList<Any>) {
        RPacketSet[packet.javaClass]?.let { packetId->
            val msg = Unpooled.buffer()
            //写包ID
            msg.writeByte(packetId.toInt())
            //写请求ID
            msg.writeByte(ctx.reqId.toInt())
            packet.write(msg)
            val address = ctx.clientIp
            out += DatagramPacket(msg.retain(), address)
        }?: run {
            log.error { "找不到${packet.javaClass} 的包" }
        }
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}