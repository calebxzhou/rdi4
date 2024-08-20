package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.protocol.account.*
import calebxzhou.rdi.ihq.protocol.general.GetVersionSPacket
import calebxzhou.rdi.ihq.protocol.team.*

/**
 * Created  on 2023-07-14,8:55.
 */
object RPacketSet {
    private var packCount = 0.toByte()
    //s2c
    private val packetWriterClassIds = linkedMapOf<Class<out SPacket>,Byte>()
    init {
        registerPacket(GetVersionSPacket::class.java)
        registerPacket(LoginSPacket::class.java)
        registerPacket(RegisterSPacket::class.java)

        registerPacket(ClearClothSPacket::class.java)
        registerPacket(ChangeClothSPacket::class.java)
        registerPacket(ChangeNameSPacket::class.java)
        registerPacket(ChangePwdSPacket::class.java)
        registerPacket(ChangeQQSPacket::class.java)

        registerPacket(TeamCreateSPacket::class.java)
        registerPacket(TeamDeleteSPacket::class.java)
        registerPacket(TeamMemberAddSPacket::class.java)
        registerPacket(TeamMemberRemoveSPacket::class.java)
        registerPacket(TeamMineSPacket::class.java)
        registerPacket(TeamTransferSPacket::class.java)
        registerPacket(TeamQuitSPacket::class.java)


    }



    //写包
    private fun registerPacket(writerClass: Class<out SPacket>) {
        packetWriterClassIds += writerClass to packCount
        packCount++
    }
    

        

    operator fun get(packetClass: Class<out SPacket>): Byte? = packetWriterClassIds[packetClass]




}