package calebxzhou.rdi.ihq

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder

class RPacketDecoder : MessageToMessageDecoder<DatagramPacket>() {
    override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any?>) {
        val datas = msg.content()
        //包id
        val packetId = datas.readByte().toInt()
        //请求id
        val reqId = datas.readByte()
        out += RPacketSet.create(packetId, datas)
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}