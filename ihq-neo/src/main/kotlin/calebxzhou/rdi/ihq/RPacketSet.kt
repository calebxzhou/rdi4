package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.ihq.protocol.SPacket
import calebxzhou.rdi.ihq.protocol.account.*
import calebxzhou.rdi.ihq.protocol.general.GetVersionSPacket
import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import calebxzhou.rdi.ihq.protocol.team.*
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-14,8:55.
 */
object RPacketSet {
    private var packCount = 0.toByte()

    //c2s
    private val packetIdReaders = linkedMapOf<Byte, (ByteBuf) -> SPacket>()

    //s2c
    private val packetWriterClassIds = linkedMapOf<Class<out CPacket>, Byte>()

    init {
        registerPacket { GetVersionSPacket() }
        registerPacket(ResponseCPacket::class.java)

        registerPacket { LoginSPacket(it) }
        registerPacket { RegisterSPacket(it) }

        registerPacket { ClearClothSPacket() }
        registerPacket { ChangeClothSPacket(it) }
        registerPacket { ChangeNameSPacket(it) }
        registerPacket { ChangePwdSPacket(it) }
        registerPacket { ChangeQQSPacket(it) }

        registerPacket { TeamCreateSPacket() }
        registerPacket { TeamDeleteSPacket() }
        registerPacket { TeamMemberAddSPacket(it) }
        registerPacket { TeamMemberRemoveSPacket(it) }
        registerPacket { TeamMineSPacket() }
        registerPacket { TeamQuitSPacket() }
    }


    //注册S包
    private fun registerPacket(reader: (ByteBuf) -> SPacket) {
        packetIdReaders += packCount to reader
        packCount++
    }

    //注册C包
    private fun registerPacket(writerClass: Class<out CPacket>) {
        packetWriterClassIds += writerClass to packCount
        packCount++
    }


    //客户端传入S包
    fun create(packetId: Byte, data: ByteBuf): SPacket? = packetIdReaders[packetId]?.run {
        val packet = invoke(data)
        data.readerIndex(data.readerIndex() + data.readableBytes())
        return packet
    } ?: run {
        log.error { "找不到ID$packetId 的包" }
        return null
    }


    operator fun get(packetClass: Class<out CPacket>): Byte? = packetWriterClassIds[packetClass]


}