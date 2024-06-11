package calebxzhou.rdi.ihq.net.protocol.island

import calebxzhou.craftcone.utils.ByteBufUtils.writeObjectId
import calebxzhou.rdi.ihq.net.protocol.SPacket
import io.netty.buffer.ByteBuf
import org.bson.types.ObjectId

/**
 * Created  on 2023-08-11,20:39.
 */
data class IslandMemberAddSPacket(val id: ObjectId): SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeObjectId(id)
    }

}