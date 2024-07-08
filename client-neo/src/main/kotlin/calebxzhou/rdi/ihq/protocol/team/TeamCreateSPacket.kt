package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.util.ByteBufUtils.writeString
import calebxzhou.rdi.ihq.protocol.SPacket
import io.netty.buffer.ByteBuf

data class TeamCreateSPacket(
    val teamName: String
): SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeString(teamName)
    }
}
