package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.log
import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.*
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import kotlinx.serialization.encodeToString

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
            log.info { "${usr}登录成功" }
            ctx.account=account
            ctx.ok(serdesJson.encodeToString(account))
        }?:let {
            log.info { "${usr}登录失败" }
            ctx.err("密码错误")
        }
    }


}