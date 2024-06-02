package calebxzhou.rdi.net

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener

data class RegisterC2SPacket(
    val name: String,
    val qq: String,
    val pwd:String,
) : Packet<ServerHandshakePacketListener> {

 /*   constructor(buffer:FriendlyByteBuf) : this(buffer.readUtf(32),buffer.readUtf(10),buffer.readUtf(16))*/

    override fun write(buffer: FriendlyByteBuf) {
        buffer.writeUtf(name,32)
        buffer.writeUtf(qq,10)
        buffer.writeUtf(pwd,16)
    }

    override fun handle(handler: ServerHandshakePacketListener) {
    }
}