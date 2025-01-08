package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.readString
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

/**
 * Created  on 2023-07-21,10:37.
 */
data class RegisterSPacket(
    val name:String,
    val pwd : String,
    val qq:String,
): SPacket {
    constructor(buf: ByteBuf): this(buf.readString(32),buf.readString(16),buf.readString(10))
    override suspend fun process(ctx: ChannelHandlerContext) {
        //PlayerService.register(this,ctx)
    }


}