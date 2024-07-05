package calebxzhou.rdi.ihq.net.protocol.general

import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-08-13,20:50.
 */
class ChatMessageCPacket(buf: ByteBuf): MessageCPacket(buf) {
    override fun process() {

    }
}