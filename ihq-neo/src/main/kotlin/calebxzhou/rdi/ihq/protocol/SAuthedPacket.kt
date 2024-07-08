package calebxzhou.rdi.ihq.protocol

import calebxzhou.rdi.ihq.model.Account
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-07-21,22:22.
 */
//发给服务器的包（登录了）
interface SAuthedPacket : SPacket{
    //处理数据
    suspend fun process(ctx: ChannelHandlerContext,account: Account){}
}