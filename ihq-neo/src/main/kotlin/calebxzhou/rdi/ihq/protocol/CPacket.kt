package calebxzhou.rdi.ihq.protocol

import io.netty.buffer.ByteBuf


/**
 * Created  on 2023-08-04,20:21.
 */
//发给客户端的包
interface CPacket {

    //写数据进FriendlyByteBuf
    fun write(buf: ByteBuf){}
}