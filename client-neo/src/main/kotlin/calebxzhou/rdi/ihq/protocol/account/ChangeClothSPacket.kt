package calebxzhou.rdi.ihq.protocol.account

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.model.Account
import calebxzhou.rdi.util.writeString
import io.netty.buffer.ByteBuf

data class ChangeClothSPacket(
    val cloth: Account.Cloth): SPacket{

    override fun write(buf: ByteBuf) {
        buf.writeBoolean(cloth.isSlim).writeString(cloth.skin).writeString(cloth.cape).writeString(cloth.elytra)
    }
}

