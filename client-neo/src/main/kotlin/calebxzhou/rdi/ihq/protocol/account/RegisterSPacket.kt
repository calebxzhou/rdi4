package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.util.writeString
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-21,10:37.
 */
data class RegisterSPacket(
    val name:String,
    val pwd : String,
    val qq:String,
): SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeString(name).writeString(pwd).writeString(qq)
    }

}