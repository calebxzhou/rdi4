package calebxzhou.rdi.ihq.net

import calebxzhou.rdi.ihq.net.protocol.CPacket
import calebxzhou.rdi.ihq.net.protocol.SPacket
import calebxzhou.rdi.ihq.net.protocol.account.LoginSPacket
import calebxzhou.rdi.ihq.net.protocol.account.RegisterSPacket
import calebxzhou.rdi.ihq.net.protocol.general.*
import calebxzhou.rdi.ihq.net.protocol.island.*
import calebxzhou.rdi.log
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-14,8:55.
 */
object RPacketSet {
    private var packCount = 0
    //c2s
    private val packetIdReaders = linkedMapOf<Int,(ByteBuf) -> CPacket>()
    //s2c
    private val packetWriterClassIds = linkedMapOf<Class<out SPacket>,Int>()
    init {
        registerPacket(VersionSPacket::class.java)
        registerPacket(::VersionCPacket)
        registerPacket(::OkCPacket)
        registerPacket(::ErrCPacket)
        registerPacket(::DisconnectCPacket)

        registerPacket(LoginSPacket::class.java)
        registerPacket(RegisterSPacket::class.java)

        registerPacket(::ChatMessageCPacket)
        registerPacket(ChattingSPacket::class.java)

        registerPacket(IslandMySPacket::class.java)
        registerPacket(::IslandInfoCPacket )
        registerPacket(IslandCreateSPacket::class.java)
        registerPacket(IslandDeleteSPacket::class.java)
        registerPacket(IslandMemberAddSPacket::class.java)
        registerPacket(IslandMemberRemoveSPacket::class.java)
        registerPacket(IslandQuitSPacket::class.java)


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
    fun create(packetId: Int, data: ByteBuf): CPacket? = packetIdReaders[packetId]?.run {
        val packet = invoke(data)
        data.readerIndex(data.readerIndex() + data.readableBytes())
        return packet
    } ?: run {
        log.error( "找不到ID$packetId 的包" )
        return null
    }
        

    fun getPacketId(packetClass: Class<out SPacket>): Int? = packetWriterClassIds[packetClass]




}