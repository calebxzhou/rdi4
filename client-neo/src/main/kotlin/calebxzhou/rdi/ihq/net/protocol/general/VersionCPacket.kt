package calebxzhou.rdi.ihq.net.protocol.general

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.protocol.CPacket
import calebxzhou.rdi.util.popupInfo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.netty.buffer.ByteBuf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.system.exitProcess

data class VersionCPacket(val version: Int) :  CPacket {
    constructor(buf: ByteBuf) : this(buf.readShort().toInt())

    override fun process() {
       /* if (version != Const.VERSION) {
                popupInfo("RDI核心需要更新，即将开始下载")

            GlobalScope.launch {
                val response = HttpClient().get("http://${Const.SERVER_ADDR}:${Const.IHQ_PORT}/core")
                val fileChannel: ByteWriteChannel = File("mods/rdi-1.0.0.jar").writeChannel()
                response.bodyAsChannel().copyAndClose(fileChannel)
                popupInfo("更新完成，请重启游戏")
                exitProcess(0)

            }
        }*/
    }
}