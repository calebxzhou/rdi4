package calebxzhou.rdi.ihq.net.protocol


/**
 * Created  on 2023-08-04,20:21.
 */
//发给客户端的包
interface CPacket {
    //处理数据
    fun process()
}