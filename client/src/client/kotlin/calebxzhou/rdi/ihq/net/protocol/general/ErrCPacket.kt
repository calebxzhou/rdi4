package calebxzhou.rdi.ihq.net.protocol.general

import calebxzhou.rdi.log
import calebxzhou.rdi.ui.general.alertErr
import io.netty.buffer.ByteBuf

class ErrCPacket(buf: ByteBuf): MessageCPacket(buf) {
    override fun process() {
        log.error("错误：$msg")
        alertErr(msg)
    }
}