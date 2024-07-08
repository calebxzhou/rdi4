package calebxzhou.rdi.ihq.protocol.general

import calebxzhou.rdi.ihq.CORE_VERSION
import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.util.ok
import io.netty.channel.ChannelHandlerContext

class GetVersionSPacket : SPacket {

    override suspend fun process(ctx: ChannelHandlerContext) {
        ctx.ok(CORE_VERSION.toString())
    }
}