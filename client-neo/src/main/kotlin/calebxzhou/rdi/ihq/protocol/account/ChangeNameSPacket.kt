package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.util.writeString
import io.netty.buffer.ByteBuf

data class ChangeNameSPacket(val name:String): SPacket{
    override fun write(buf: ByteBuf) {
        buf.writeString(name)
    }
}

