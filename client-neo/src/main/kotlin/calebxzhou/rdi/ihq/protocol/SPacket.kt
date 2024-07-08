package calebxzhou.rdi.ihq.protocol

import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-21,22:22.
 */
//发给服务器的包
interface SPacket{
    //写数据进FriendlyByteBuf
    fun write(buf: ByteBuf){}
}