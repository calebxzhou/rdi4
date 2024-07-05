package calebxzhou.rdi.ihq.net

import io.ktor.network.sockets.*
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder

class RPacketDecoder : MessageToMessageDecoder<DatagramPacket>() {
    override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any?>) {
        val datas = msg.content()
        val packetId = datas.readByte().toInt()
        out += RPacketSet.create(packetId, datas)
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}