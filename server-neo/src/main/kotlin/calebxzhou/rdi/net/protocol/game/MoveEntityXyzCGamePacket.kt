package calebxzhou.rdi.net.protocol.game

import net.minecraft.network.FriendlyByteBuf

data class MoveEntityXyzCGamePacket (
    val entityId: Int,
    val xa:Short,
    val ya:Short,
    val za:Short,
    val xRot: Byte,
    val yRot: Byte,
    val onGround: Boolean,
): CGamePacket {
    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(entityId)
            .writeShort(xa.toInt())
            .writeShort(ya.toInt())
            .writeShort(za.toInt())
            .writeBoolean(onGround)
    }


}