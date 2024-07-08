package calebxzhou.rdi.ihq

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.log
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder

class RPacketEncoder : MessageToMessageEncoder<SPacket>() {
    override fun encode(ctx: ChannelHandlerContext, packet: SPacket, out: MutableList<Any>) {
        RPacketSet.getPacketId(packet.javaClass)?.let { packetId ->

            val msg: ByteBuf = Unpooled.buffer()
            //写包ID
            msg.writeByte(packetId)
            //写请求ID
            msg.writeByte(IhqClient.reqId.toInt())
            packet.write(msg)
            out += DatagramPacket(msg.retain(), Const.IHQ_INET_ADDR)


        } ?: log.error("找不到包ID" + packet.javaClass)
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}