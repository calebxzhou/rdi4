package calebxzhou.rdi.net.protocol

import io.netty.buffer.ByteBuf
import net.minecraft.network.FriendlyByteBuf

/**
 * Created  on 2023-07-21,22:22.
 */
interface CPacket{
    //写数据进FriendlyByteBuf
    fun write(buf: FriendlyByteBuf)
}