package calebxzhou.rdi

object Const {

    const val MODID = "rdi"
    //是否为调试模式,本地用
    @JvmStatic
    val DEBUG = System.getProperty("rdi.debug").toBoolean()

    //版本号与协议号
    const val VERSION = 0x460
    const val IHQ_VERSION = 0x460
    val SEED = 11451400L
    //显示版本
    const val VERSION_DISP = "RDI 4E"
    const val VERSION_STR = "4.6"

}
