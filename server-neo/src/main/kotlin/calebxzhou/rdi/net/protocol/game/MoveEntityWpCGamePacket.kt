package calebxzhou.rdi.net.protocol.game

import net.minecraft.network.FriendlyByteBuf

data class MoveEntityWpCGamePacket (
    val entityId: Int,
    val xRot: Byte,
    val yRot: Byte,
): CGamePacket {
    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(entityId)
            .writeByte(xRot.toInt())
            .writeByte(yRot.toInt())
    }


}