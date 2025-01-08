package calebxzhou.rdi

import net.minecraft.core.BlockPos
import java.net.InetSocketAddress

object Const {
    //mod id
    const val MODID = "rdi"
    const val SEED = 11451400
    val BASE_POS = BlockPos(0,63,0)
    //是否为调试模式,本地用
    val DEBUG = System.getProperty("rdi.debug").toBoolean()

    @JvmField
    val SERVER_PORT =  System.getProperty("rdi.port").toIntOrNull()?:38430
    @JvmField
    val LEVEL_NAME = "world"

    //版本号与协议号
    const val VERSION = 0x460

    //显示版本
    const val VERSION_STR = "RDI 4R Beta"
}
