package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.readString
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

data class ChangePwdSPacket(val pwd:String): SAuthedPacket{
    constructor(buf: ByteBuf): this(buf.readString())

    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        PlayerService.changePwd(account,pwd, ctx)
    }
}

