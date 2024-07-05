package calebxzhou.rdi.net.protocol.general

import calebxzhou.rdi.net.protocol.CPacket
import calebxzhou.rdi.net.protocol.SPacket
import io.netty.buffer.ByteBuf
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer

data class MoveEntityWpCPacket (
    val entityId: Int,
    val xRot: Byte,
    val yRot: Byte,
): CPacket {
    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(entityId)
            .writeByte(xRot.toInt())
            .writeByte(yRot.toInt())
    }


}