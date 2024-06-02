package calebxzhou.rdi.util

import org.bson.types.ObjectId
import java.math.BigInteger

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
