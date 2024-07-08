package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.account
import calebxzhou.rdi.ihq.util.err
import calebxzhou.rdi.ihq.util.ok
import calebxzhou.rdi.ihq.util.readString
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-07-13,17:27.
 */
//玩家登录请求
data class LoginSPacket(
    val usr: String,
    //密码
    val pwd: String,
) : SPacket {
    constructor(buf: ByteBuf) : this(buf.readString(),buf.readString())

    override suspend fun process(ctx: ChannelHandlerContext) {
        PlayerService.validate(usr, pwd)?.let { account ->
            ctx.account=account
            ctx.ok()
        }?:let {
            ctx.err("密码错误")
        }
    }


}