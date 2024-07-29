package calebxzhou.rdi.ihq.protocol.general

import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.ihq.util.writeString
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-08-15,8:47.
 */
//处理成功&数据
data class ResponseCPacket(
    val reqId: Byte,
    val ok: Boolean,
    val data: String
) : CPacket {
    override fun write(buf: ByteBuf) {
        buf.writeByte(reqId.toInt()).writeBoolean(ok).writeString(data)
    }
}