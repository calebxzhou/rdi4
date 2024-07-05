package calebxzhou.rdi.net

import calebxzhou.rdi.net.protocol.CPacket
import calebxzhou.rdi.net.protocol.SPacket
import calebxzhou.rdi.log
import calebxzhou.rdi.net.protocol.general.MoveEntityXyzCPacket
import calebxzhou.rdi.net.protocol.general.MovePlayerXyzSPacket
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-14,8:55.
 */
object RPacketSet {
    private var packCount = 0

    //c2s
    private val packetIdReaders = linkedMapOf<Int, (ByteBuf) -> SPacket>()

    //s2c
    private val packetWriterClassIds = linkedMapOf<Class<out CPacket>, Int>()

    init {
        registerPacket(MoveEntityXyzCPacket::class.java)
        registerPacket(::MovePlayerXyzSPacket)
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
    fun create(packetId: Int, data: ByteBuf): SPacket? = packetIdReaders[packetId]?.run {
        val packet = invoke(data)
        data.readerIndex(data.readerIndex() + data.readableBytes())
        return packet
    } ?: run {
        log.error("找不到ID$packetId 的包")
        return null
    }


    fun getPacketId(packetClass: Class<out CPacket>): Int? = packetWriterClassIds[packetClass]


}