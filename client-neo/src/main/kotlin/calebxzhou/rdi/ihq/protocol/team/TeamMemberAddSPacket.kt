package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.util.ByteBufUtils.writeObjectId
import calebxzhou.rdi.ihq.protocol.SPacket
import io.netty.buffer.ByteBuf
import org.bson.types.ObjectId

/**
 * Created  on 2023-08-11,20:39.
 */
data class TeamMemberAddSPacket(val id: ObjectId): SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeObjectId(id)
    }
}