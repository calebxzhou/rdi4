package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.util.writeString
import io.netty.buffer.ByteBuf

data class TeamCreateSPacket(
    val teamName: String
): SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeString(teamName)
    }
}
