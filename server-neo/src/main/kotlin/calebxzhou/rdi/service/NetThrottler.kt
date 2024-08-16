package calebxzhou.rdi.service

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.resources.ResourceLocation

object NetThrottler {
    //几个tick以后才发一个包
    private val packetTickInterval = hashMapOf<Class<out Packet<*>>,Int>(
        ClientboundMoveEntityPacket.Pos::class.java to 2,
        ClientboundMoveEntityPacket.PosRot::class.java to 2,
        ClientboundMoveEntityPacket.Rot::class.java to 2,
    )
    //tick计数
    private val packetTicks = packetTickInterval.mapValues { 0 }.toMutableMap()
    @JvmStatic
    fun allowSendPacket(packet: Packet<*>): Boolean{
        /*if(packet is ClientboundCustomPayloadPacket){
            if (packet.identifier == ResourceLocation("createutilities","main")) {
                return false
            }
        }
        if(packet is ClientboundLevelChunkWithLightPacket){
            packet.lightData
        }
        packetTickInterval[packet.javaClass]?.let { interval ->
            packetTicks[packet.javaClass]?.let { ticks ->
                if(ticks >= interval){
                    packetTicks[packet.javaClass] = 0
                    return true
                }else{
                    packetTicks[packet.javaClass] = ticks+1
                    return false
                }
            }
        }*/

        return true
    }
}