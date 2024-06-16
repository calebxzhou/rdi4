package calebxzhou.rdi.net.protocol

import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-21,22:22.
 */
interface CPacket{
    //写数据进FriendlyByteBuf
    fun write(buf: ByteBuf)
}