package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import calebxzhou.rdi.ihq.util.clientIp
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder

class RIhqPacketEncoder : MessageToMessageEncoder<ResponseCPacket>() {
    override fun encode(ctx: ChannelHandlerContext, packet: ResponseCPacket, out: MutableList<Any>) {
            val msg = Unpooled.buffer()
            //写请求ID
            //msg.writeByte(ctx.reqId.toInt())
            packet.write(msg)
            val address = ctx.clientIp
            out += DatagramPacket(msg.retain(), address)

    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}