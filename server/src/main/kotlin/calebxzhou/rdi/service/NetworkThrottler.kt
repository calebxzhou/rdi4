package calebxzhou.rdi.service

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket

object NetworkThrottler {
    @JvmStatic
    fun onSendToClient(packet: Packet<*>): Boolean{
        /*if(packet is ClientboundMoveEntityPacket){

        }*/
        return true
    }
}