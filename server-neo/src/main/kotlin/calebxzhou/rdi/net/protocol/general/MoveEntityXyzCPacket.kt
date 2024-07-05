package calebxzhou.rdi.net.protocol.general

import calebxzhou.rdi.net.protocol.CPacket
import calebxzhou.rdi.net.protocol.SPacket
import io.netty.buffer.ByteBuf
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer

data class MoveEntityXyzCPacket (
    val entityId: Int,
    val xa:Short,
    val ya:Short,
    val za:Short,
    val xRot: Byte,
    val yRot: Byte,
    val onGround: Boolean,
): CPacket {
    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(entityId)
            .writeShort(xa.toInt())
            .writeShort(ya.toInt())
            .writeShort(za.toInt())
            .writeBoolean(onGround)
    }


}