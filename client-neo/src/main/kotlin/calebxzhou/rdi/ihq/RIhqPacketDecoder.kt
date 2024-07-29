package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.IhqClient.reqId
import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import calebxzhou.rdi.util.readString
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder

class RIhqPacketDecoder : MessageToMessageDecoder<DatagramPacket>() {
    override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any?>) {
        val datas = msg.content()
        out += ResponseCPacket(datas.readByte(),datas.readBoolean() ,datas.readString())
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}