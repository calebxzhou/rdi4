package calebxzhou.rdi.service

import calebxzhou.rdi.Const
import calebxzhou.rdi.util.*
import com.google.gson.Gson
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
import java.io.File
import java.util.*

data class PacketRecord(val packetClass: Class<*>,var amount: Long = 0,var size:Long = 0 )
//网络统计
object NetMetrics {
init {

    Timer("RDI-NetMetrics").schedule(object : TimerTask() {
        var prevTxBytes = 0L
        var prevRxBytes = 0L
        override fun run() {
            prevTxBytes = totalTxBytes
            prevRxBytes = totalRxBytes
            Thread.sleep(1000)
            txKBps = (totalTxBytes - prevTxBytes)/1024f
            rxKBps = (totalRxBytes - prevRxBytes)/1024f
            /*if(true){

            val sortedTx = recordsTx.toList().sortedByDescending { (_, value) -> value.size }.toMap()
            val sortedRx = recordsRx.toList().sortedByDescending { (_, value) -> value.size }.toMap()
            val textTx= sortedTx.map { "${it.key} ${it.value.size} ${it.value.amount} ${it.value.packetClass}" }.joinToString("\n")
            File("logs/packet_tx.log").writeText(textTx)
            val textRx= sortedRx.map { "${it.key} ${it.value.size} ${it.value.amount} ${it.value.packetClass}" }.joinToString("\n")
            File("logs/packet_rx.log").writeText(textRx)
            }*/
        }
    }, 0,2000)
}
    @JvmStatic
    var totalTxBytes = 0L
    @JvmStatic
    var totalRxBytes = 0L

    var txKBps = 0f
    val txKBpsStr
        get() = if(txKBps>0.01f)
            "${String.format("%.2f",txKBps)}K/s"
        else
            "总"+humanReadableByteCount(totalTxBytes)
    var rxKBps = 0f
    val rxKBpsStr
        get() = if(rxKBps>0.01f)"${String.format("%.2f",rxKBps)}K/s" else "总"+humanReadableByteCount(totalRxBytes)

    private val recordsTx = hashMapOf<Int,PacketRecord>()
    private val recordsRx = hashMapOf<Int,PacketRecord>()
    @JvmStatic
    fun onPacketEncode(packetId:Int,packet: Packet<*>,byteBuf: ByteBuf){
        totalTxBytes += byteBuf.writerIndex()
        recordsTx[packetId]?.let { record->
            record.amount++
            record.size += byteBuf.writerIndex()
            recordsTx[packetId] = record
        }?:let {
            val record = PacketRecord(packet.javaClass)
            recordsTx += packetId to record
        }
    }
    @JvmStatic
    fun onPacketDecode(packetId: Int, packet: Packet<*>, byteBuf: ByteBuf) {
        totalRxBytes += byteBuf.readerIndex()
        recordsRx[packetId]?.let { record->
            record.amount++
            record.size += byteBuf.readerIndex()
            recordsRx[packetId] = record
        }?:let {
            val record = PacketRecord(packet.javaClass)
            recordsRx += packetId to record
        }
        /*if(packet is ClientboundCustomPayloadPacket){

        File("logs/packet_rx.log").appendText(packet.identifier.toString()+" "+packet.data.readableBytes())
        }*/
    }
    @JvmStatic
    fun render(guiGraphics: GuiGraphics){
        val tx = mcText(
            "↑ $txKBpsStr"
        ).withStyle(ChatFormatting.GOLD)
        val rx = mcText("↓ $rxKBpsStr")
            .withStyle(ChatFormatting.GREEN)
        val text = tx.append(" ").append(rx)
        guiGraphics.drawString(mcFont,
            text,
            mcUIWidth - mcTextWidthOf(text), mcUIHeight-10,0x000000,true)
    }


}