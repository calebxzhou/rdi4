package calebxzhou.rdi

import net.minecraft.client.multiplayer.ServerData
import java.net.InetSocketAddress

object Const {

    const val MODID = "rdi"
    //是否为调试模式,本地用
    val DEBUG = System.getProperty("rdi.debug").toBoolean()

    const val SERVER_PORT = 38430
    const val IHQ_PORT = 38411
    val SERVER_ADDR = if(DEBUG)"127.0.0.1" else "rdi.calebxzhou.cn"
    val SERVER_INET_ADDR = InetSocketAddress(SERVER_ADDR, SERVER_PORT)
    val IHQ_INET_ADDR = InetSocketAddress(SERVER_ADDR, IHQ_PORT)
    //版本号与协议号
    const val VERSION = 0x460
    const val IHQ_VERSION = 0x460
    val SEED = 11451400L
    //显示版本
    const val VERSION_STR = "RDI 4R Demo1"
    val SERVER_DATA
        get() = ServerData("RDI", VERSION_STR,false)
}
