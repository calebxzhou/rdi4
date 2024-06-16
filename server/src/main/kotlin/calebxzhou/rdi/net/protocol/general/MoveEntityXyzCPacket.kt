package calebxzhou.rdi.net.protocol.general

import calebxzhou.rdi.net.protocol.CPacket
import calebxzhou.rdi.net.protocol.SPacket
import io.netty.buffer.ByteBuf
import net.minecraft.server.level.ServerPlayer

data class MoveEntityXyzCPacket (
    val x:Float,val y:Float, val z:Float
): CPacket {
    override fun write(buf: ByteBuf) {
        buf.writeFloat(x).writeFloat(y).writeFloat(z)
    }


}