package calebxzhou.rdi.ihq.protocol.general

import calebxzhou.rdi.util.ByteBufUtils.readString
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.ihq.protocol.CPacket
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
    constructor(buf: ByteBuf) : this(buf.readByte(),buf.readBoolean(),buf.readString())
    override fun process() {
        IhqClient.handleResponse(this)
    }
}