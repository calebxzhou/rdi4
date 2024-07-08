package calebxzhou.rdi.net.protocol.game

import net.minecraft.network.FriendlyByteBuf

/**
 * Created  on 2023-07-21,22:22.
 */
interface CGamePacket{
    //写数据进FriendlyByteBuf
    fun write(buf: FriendlyByteBuf)
}