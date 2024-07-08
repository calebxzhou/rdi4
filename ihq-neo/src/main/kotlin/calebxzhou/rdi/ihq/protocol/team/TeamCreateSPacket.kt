package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.TeamService
import io.netty.channel.ChannelHandlerContext

class TeamCreateSPacket: SAuthedPacket {

    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        TeamService.create(account,ctx)
    }

}
