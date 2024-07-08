package calebxzhou.rdi.net.protocol.game

import io.netty.buffer.ByteBuf
import net.minecraft.server.level.ServerPlayer

/**
 * Created  on 2023-07-06,8:48.
 */
data class MovePlayerXyzSGamePacket (
    val x:Float,val y:Float, val z:Float
): SGamePacket {
    constructor(buf: ByteBuf): this(buf.readFloat(),buf.readFloat(),buf.readFloat())

    override   fun process(player: ServerPlayer) {
        //ServerGamePacketListener.handleMovePlayer
    }


}