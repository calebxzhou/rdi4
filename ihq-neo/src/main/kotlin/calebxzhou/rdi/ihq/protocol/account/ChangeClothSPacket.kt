package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.readString
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

data class ChangeClothSPacket(
    val cloth: Account.Cloth): SAuthedPacket{
    constructor(buf: ByteBuf): this(Account.Cloth(buf.readBoolean(),buf.readString(),buf.readString(),buf.readString()))

    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        //PlayerService.changeCloth(account,cloth, ctx)
    }
}

