package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.SAuthedPacket
import calebxzhou.rdi.ihq.service.PlayerService
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-08-13,16:10.
 */
 class ClearClothSPacket : SAuthedPacket {
    override suspend fun process(ctx: ChannelHandlerContext, account: Account) {
      //  PlayerService.clearCloth(account,ctx)
    }

}