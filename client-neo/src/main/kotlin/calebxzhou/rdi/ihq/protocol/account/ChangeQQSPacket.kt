package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.util.writeString
import io.netty.buffer.ByteBuf

data class ChangeQQSPacket(val qq:String): SPacket{
    override fun write(buf: ByteBuf) {
        buf.writeString(qq)
    }
}

