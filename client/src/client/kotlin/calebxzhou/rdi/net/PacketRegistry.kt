package calebxzhou.rdi.net

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet

data class PacketRegistry(val id: Int,
                          val clazz: Class<out Packet<out PacketListener>>,
                          val ctor: (FriendlyByteBuf)-> Packet<out PacketListener>)
