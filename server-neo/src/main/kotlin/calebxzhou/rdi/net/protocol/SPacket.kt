package calebxzhou.rdi.net.protocol

import net.minecraft.server.level.ServerPlayer

/**
 * Created  on 2023-08-04,20:21.
 */
//适用于房间内的数据包
interface SPacket {
    //处理数据
    fun process(player: ServerPlayer)
}