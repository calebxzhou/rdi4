package calebxzhou.rdi.ihq.protocol.general

import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.logger
import calebxzhou.rdi.serdes.serdesJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
//处理成功&数据
data class ResponseCPacket(
    val reqId: Byte,
    val ok: Boolean,
    val data: String
) : CPacket {
    override fun process() {
        logger.info(serdesJson.encodeToString(this))
        IhqClient.handleResponse(this)
    }
}