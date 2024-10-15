package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.util.clientIp
import calebxzhou.rdi.ihq.util.reqId
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder

class RIhqPacketDecoder : MessageToMessageDecoder<DatagramPacket>() {
    override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any>) {
        val datas = msg.content()
        ctx.clientIp = msg.sender()
        //读包ID
        val packetId = datas.readByte()
        //读请求ID
        val reqId = datas.readByte()
        //临时保存id到ctx里面
        ctx.reqId=reqId
        out += RPacketSet.create(packetId, datas) ?: let {
            log.error { "找不到包ID${packetId}" }
        }
    }


    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }
}