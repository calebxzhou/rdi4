package calebxzhou.rdi.net

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.login.ServerLoginPacketListener

data class LoginC2SPacket(val qq: String, val pwd: String) : Packet<ServerLoginPacketListener> {
  /*  constructor(buffer: FriendlyByteBuf) : this(buffer.readUtf(10), buffer.readUtf(16))*/

    override fun write(buffer: FriendlyByteBuf) {
        buffer.writeUtf(this.qq, 10)
        buffer.writeUtf(this.pwd, 16)
    }

    override fun handle(handler: ServerLoginPacketListener) {
    }

}