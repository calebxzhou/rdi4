package calebxzhou.rdi.service

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

object IngameNetworking {
    val NET_RL = ResourceLocation("rdi","net")
    fun onReceive(
        minecraft: Minecraft,
        clientPacketListener: ClientPacketListener,
        friendlyByteBuf: FriendlyByteBuf,
        packetSender: PacketSender
    ) {

    }
}