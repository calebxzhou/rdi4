package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.TeamService
import calebxzhou.rdi.ihq.util.readString
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-08-11,20:39.
 */
data class TeamMemberAddSPacket(val qq: String) : SAuthedPacket {
    constructor(buf: ByteBuf) : this(buf.readString())

    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        TeamService.invite(account, qq, ctx)
    }
}