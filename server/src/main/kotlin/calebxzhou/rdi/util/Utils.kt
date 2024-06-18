package calebxzhou.rdi.util

import calebxzhou.rdi.net.protocol.CPacket
import io.netty.channel.ChannelHandlerContext
import io.netty.util.AttributeKey
import org.bson.types.ObjectId
import java.math.BigInteger
import java.net.InetSocketAddress

/**
 * calebxzhou @ 2024-06-02 11:55
 */
fun ObjectId.toRdid():String{
    return this.toByteArray().toBase36()
}

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
fun ChannelHandlerContext.sendPacket(packet: CPacket){
    this.channel().writeAndFlush(packet)
}
var ChannelHandlerContext.clientIp: InetSocketAddress
    get() = channel().attr<InetSocketAddress>(AttributeKey.valueOf("clientIp")).get()
    set(value) = channel().attr<InetSocketAddress>(AttributeKey.valueOf("clientIp")).set(value)
