package calebxzhou.rdi.net

import calebxzhou.rdi.exception.RAccountException
import calebxzhou.rdi.mixin.ASLoginPacketListener
import calebxzhou.rdi.rScope
import calebxzhou.rdi.service.PlayerService
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.toRdid
import kotlinx.coroutines.launch
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket
import net.minecraft.network.protocol.login.ServerLoginPacketListener
import net.minecraft.network.protocol.login.ServerboundHelloPacket
import java.util.*

data class LoginSPacket(val qq: String, val pwd: String) : Packet<ServerLoginPacketListener> {
    constructor(buffer: FriendlyByteBuf) : this(buffer.readUtf(10), buffer.readUtf(16))

    override fun write(buffer: FriendlyByteBuf) {

    }

    override fun handle(handler: ServerLoginPacketListener) {
        val connection = (handler as ASLoginPacketListener).connection
        val pack = this
        rScope.launch {
            try {
                val player = PlayerService.checkPassword(pack)
                handler.handleHello(ServerboundHelloPacket(player.qq, Optional.of(UUID.nameUUIDFromBytes(player.id.toByteArray()))))
            } catch (e: RAccountException) {
                connection.send(ClientboundLoginDisconnectPacket(mcText(e.localizedMessage)))
            }
        }

    }

}