package calebxzhou.rdi.util

import calebxzhou.rdi.net.protocol.game.CGamePacket
import io.netty.channel.ChannelHandlerContext
import io.netty.util.AttributeKey
import java.math.BigInteger
import java.net.InetSocketAddress

/**
 * calebxzhou @ 2024-06-02 11:55
 */

fun ByteArray.toBase36(): String {
    val base36Chars = "0123456789abcdefghijklmnopqrstuvwxyz"
    val bigInt = BigInteger(1, this)
    var base36Str = ""
    var tempBigInt = bigInt

    while (tempBigInt > BigInteger.ZERO) {
        base36Str = base36Chars[tempBigInt.mod(BigInteger.valueOf(36L)).toInt()] + base36Str
        tempBigInt /= BigInteger.valueOf(36L)
    }

    return base36Str
}
fun humanReadableByteCount(bytes: Long, si: Boolean = false): String {
    val unit = if (si) 1000 else 1024
    if (bytes < unit) return "$bytes B"
    val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
    val prefix = (if (si) "kMGTPE" else "KMGTPE")[exp-1].toString() //+ (if (si) "" else "i")
    val result = bytes / Math.pow(unit.toDouble(), exp.toDouble())
    return String.format("%.2f %s", result, prefix)
}
fun ChannelHandlerContext.sendPacket(packet: CGamePacket){
    this.channel().writeAndFlush(packet)
}
var ChannelHandlerContext.clientIp: InetSocketAddress
    get() = channel().attr<InetSocketAddress>(AttributeKey.valueOf("clientIp")).get()
    set(value) = channel().attr<InetSocketAddress>(AttributeKey.valueOf("clientIp")).set(value)
