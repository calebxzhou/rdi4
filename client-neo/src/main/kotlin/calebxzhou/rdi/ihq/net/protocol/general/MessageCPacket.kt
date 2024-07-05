package calebxzhou.rdi.ihq.net.protocol.general

import calebxzhou.craftcone.utils.ByteBufUtils.readString
import calebxzhou.rdi.ihq.net.protocol.CPacket
import io.netty.buffer.ByteBuf

abstract class MessageCPacket(val msg:String) : CPacket{
    constructor(buf: ByteBuf): this(buf.readString())

    abstract override fun process()



}
