package calebxzhou.rdi.net.protocol.general

import calebxzhou.rdi.net.protocol.SPacket
import io.netty.buffer.ByteBuf
import net.minecraft.server.level.ServerPlayer


data class MovePlayerWpSPacket (
    val w:Float,val p:Float
): SPacket {
    constructor(buf: ByteBuf): this(buf.readFloat(),buf.readFloat())

    override   fun process(player: ServerPlayer) {
        //ServerGamePacketListener.handleMovePlayer
    }


}