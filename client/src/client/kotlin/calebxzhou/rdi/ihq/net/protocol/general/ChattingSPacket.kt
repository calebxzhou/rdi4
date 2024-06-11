package calebxzhou.rdi.ihq.net.protocol.general

import calebxzhou.craftcone.utils.ByteBufUtils.writeString
import calebxzhou.rdi.ihq.net.protocol.SPacket
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-06,8:48.
 */
data class ChattingSPacket (
    val msg: String,
):  SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeString(msg)
    }

}