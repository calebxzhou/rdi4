package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.readString
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

data class ChangeQQSPacket(val qq:String): SAuthedPacket{
    constructor(buf: ByteBuf): this(buf.readString())

    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        PlayerService.changeQQ(account,qq, ctx)
    }
}

