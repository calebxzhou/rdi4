package calebxzhou.rdi.ihq.net.protocol.general

import calebxzhou.rdi.log
import calebxzhou.rdi.util.dialogErr
import io.netty.buffer.ByteBuf

class ErrCPacket(buf: ByteBuf): MessageCPacket(buf) {
    override fun process() {
        log.error("错误：$msg")
        dialogErr(msg)
    }
}