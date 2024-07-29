package calebxzhou.rdi.ihq.protocol.general

import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.log
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.util.readString
import io.netty.buffer.ByteBuf
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

/**
 * Created  on 2023-08-15,8:47.
 */
@Serializable
//处理成功&数据
data class ResponseCPacket(
    val reqId: Byte,
    val ok: Boolean,
    val data: String
) : CPacket {
    override fun process() {
        log.info(serdesJson.encodeToString(this))
        IhqClient.handleResponse(this)
    }
}