package calebxzhou.rdi.net

import calebxzhou.rdi.exception.RAccountException
import calebxzhou.rdi.mixin.ASLoginPacketListener
import calebxzhou.rdi.rScope
import calebxzhou.rdi.service.PlayerService
import calebxzhou.rdi.util.mcText
import kotlinx.coroutines.launch
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket
import net.minecraft.network.protocol.login.ServerLoginPacketListener

data class RegisterSPacket(
    val name: String,
    val qq: String,
    val pwd: String,
) : Packet<ServerLoginPacketListener> {

    constructor(buffer: FriendlyByteBuf) : this(buffer.readUtf(32), buffer.readUtf(10), buffer.readUtf(16))

    override fun write(buffer: FriendlyByteBuf) {

    }

    override fun handle(handler: ServerLoginPacketListener) {
        val connection = (handler as ASLoginPacketListener).connection
        val pack = this

        rScope.launch {
            try {
                PlayerService.register(pack)
            } catch (e: RAccountException) {
                connection.send(ClientboundLoginDisconnectPacket(mcText(e.localizedMessage)))
            }
        }


    }
}