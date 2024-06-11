package calebxzhou.rdi.ihq.net.protocol.general

import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-08-15,8:53.
 */
//断开与玩家的连接
class DisconnectCPacket(buf: ByteBuf): MessageCPacket(buf) {
    override fun process() {

    }
}