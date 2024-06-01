package calebxzhou.rdi.net

import calebxzhou.rdi.mixin.APacketListener
import calebxzhou.rdi.rScope
import calebxzhou.rdi.service.PlayerService
import calebxzhou.rdi.util.mcText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket
import net.minecraft.network.protocol.login.ServerLoginPacketListener

data class RdiLoginC2SPacket(val qq: String, val pwd: String) : Packet<ServerLoginPacketListener> {
    constructor(buffer: FriendlyByteBuf) : this(buffer.readUtf(10), buffer.readUtf(16))

    override fun write(buffer: FriendlyByteBuf) {
        throw IllegalStateException("服务端不需要处理此数据包")
    }

    override fun handle(handler: ServerLoginPacketListener) {
        val connection = (handler as APacketListener).connection
        rScope.launch {


            PlayerService.getByQQ(qq)?.let { player ->
                if (player.pwd != pwd) {
                    connection.send(ClientboundLoginDisconnectPacket(mcText("密码错误")))
                }
            }?:let {
                connection.send(ClientboundLoginDisconnectPacket(mcText("密码错误")))
            }
        }
    }

}