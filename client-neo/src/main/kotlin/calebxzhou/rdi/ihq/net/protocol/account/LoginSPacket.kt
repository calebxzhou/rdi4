package calebxzhou.rdi.ihq.net.protocol.account

import calebxzhou.craftcone.utils.ByteBufUtils.writeString
import calebxzhou.rdi.ihq.net.protocol.SPacket
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-13,17:27.
 */
//玩家登录请求
data class LoginSPacket(
    val qq: String,
    //密码
    val pwd: String,
) : SPacket {
    override fun write(buf: ByteBuf) {
        buf.writeString(qq).writeString(pwd)
    }

}