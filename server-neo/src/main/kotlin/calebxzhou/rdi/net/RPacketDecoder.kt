package calebxzhou.rdi.net

import calebxzhou.rdi.util.clientIp
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder

class RPacketDecoder : MessageToMessageDecoder<DatagramPacket>() {
    override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any?>) {
        val datas = msg.content()
        ctx.clientIp = msg.sender()
        val packetId = datas.readByte().toInt()
        out += RPacketSet.create(packetId, datas)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}