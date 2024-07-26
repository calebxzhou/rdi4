package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.util.writeString
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-08-11,20:39.
 */
data class TeamTransferSPacket(val qq: String) : SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeString(qq)
    }
}