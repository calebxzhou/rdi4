package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.TeamService
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-08-13,16:10.
 */
 class TeamMineSPacket : SAuthedPacket {
    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        TeamService.mine(account,ctx)
    }

}