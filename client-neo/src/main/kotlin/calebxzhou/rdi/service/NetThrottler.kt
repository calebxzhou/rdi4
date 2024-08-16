package calebxzhou.rdi.service

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

//节约带宽
object NetThrottler {
    //几个tick以后才发一个包
    private val packetTickInterval = hashMapOf<Class<out Packet<*>>,Int>(
        ServerboundMovePlayerPacket.Pos::class.java to 4,
        ServerboundMovePlayerPacket.PosRot::class.java to 5,
        ServerboundMovePlayerPacket.Rot::class.java to 5
    )
    //tick计数
    private val packetTicks = packetTickInterval.mapValues { 0 }.toMutableMap()
    @JvmStatic
    fun allowSendPacket(packet: Packet<*>): Boolean{
        /*packetTickInterval[packet.javaClass]?.let { interval ->
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