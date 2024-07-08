package calebxzhou.rdi.net.protocol.game

import io.netty.buffer.ByteBuf
import net.minecraft.server.level.ServerPlayer


data class MovePlayerWpSGamePacket (
    val w:Float,val p:Float
): SGamePacket {
    constructor(buf: ByteBuf): this(buf.readFloat(),buf.readFloat())

    override   fun process(player: ServerPlayer) {
        //ServerGamePacketListener.handleMovePlayer
    }


}