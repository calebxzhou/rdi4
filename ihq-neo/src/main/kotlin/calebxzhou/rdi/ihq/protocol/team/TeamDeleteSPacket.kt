package calebxzhou.rdi.ihq.protocol.team

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.TeamService
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-08-11,20:39.
 */
class TeamDeleteSPacket : SAuthedPacket {

    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
        TeamService.delete(account,ctx)
    }
}