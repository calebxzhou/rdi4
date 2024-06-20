package calebxzhou.rdi

import net.minecraft.core.BlockPos
import java.net.InetSocketAddress

object Const {
    //mod id
    const val MODID = "rdi-core"
    const val SEED = 1145141919810
    val BASE_POS = BlockPos(0,63,0)
    //是否为调试模式,本地用
    val DEBUG = System.getProperty("rdi.debug").toBoolean()
    const val SERVER_PORT = 38410
    val SERVER_ADDR = if(DEBUG)"127.0.0.1" else "sy6.calebxzhou.cn"
    val SERVER_INET_ADDR = InetSocketAddress(SERVER_ADDR, SERVER_PORT)

    //版本号与协议号
    const val VERSION = 0x440

    //显示版本
    const val VERSION_STR = "RDI Skies 4.4 @ MC1.20.1"
}
