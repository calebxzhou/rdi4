package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.protocol.account.*
import calebxzhou.rdi.ihq.protocol.general.GetVersionSPacket
import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import calebxzhou.rdi.ihq.protocol.team.*
import calebxzhou.rdi.log
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-14,8:55.
 */
object RPacketSet {
    private var packCount = 0.toByte()
    //c2s
    private val packetIdReaders = linkedMapOf<Byte,(ByteBuf) -> CPacket>()
    //s2c
    private val packetWriterClassIds = linkedMapOf<Class<out SPacket>,Byte>()
    init {
        registerPacket(GetVersionSPacket::class.java)
        registerPacket(::ResponseCPacket)

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


    //注册S包
    private fun registerPacket(reader: (ByteBuf) -> CPacket) {
        packetIdReaders += packCount to reader
        packCount++
    }

    //注册C包
    private fun registerPacket(writerClass: Class<out SPacket>) {
        packetWriterClassIds += writerClass to packCount
        packCount++
    }
    
    
    //服务器传入C包
    fun create(packetId: Byte, data: ByteBuf): CPacket? = packetIdReaders[packetId]?.run {
        val packet = invoke(data)
        data.readerIndex(data.readerIndex() + data.readableBytes())
        return packet
    } ?: run {
        log.error( "找不到ID$packetId 的包" )
        return null
    }
        

    operator fun get(packetClass: Class<out SPacket>): Byte? = packetWriterClassIds[packetClass]




}