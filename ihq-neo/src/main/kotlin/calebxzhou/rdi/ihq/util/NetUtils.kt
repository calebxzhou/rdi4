package calebxzhou.rdi.ihq.util

import calebxzhou.rdi.ihq.exception.AuthError
import calebxzhou.rdi.ihq.exception.ParamError
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.CPacket
import calebxzhou.rdi.ihq.protocol.general.ResponseCPacket
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import io.netty.util.AttributeKey
import org.bson.types.ObjectId
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal

/**
 * calebxzhou @ 2024-06-07 16:54
 */

fun getVarLongSize(input: Long): Int {
    for (i in 1..9) {
        if (input and (-1L shl i * 7) == 0L) {
            return i
        }
    }
    return 10
}

fun getVarIntSize(input: Int): Int {
    for (i in 1..4) {
        if (input and (-1 shl i * 7) == 0) {
            return i
        }
    }
    return 5
}

private fun getMaxEncodedUtfLength(i: Int): Int {
    return i * 3
}

fun ByteBuf.writeObjectId(objectId: ObjectId): ByteBuf {
    writeBytes(objectId.toByteArray())
    return this
}

fun ByteBuf.readObjectId(): ObjectId = ObjectId(
    readBytes(12).nioBuffer()
)

fun ByteBuf.readString(): String {
    return this.readString(32767)
}

fun ByteBuf.readString(i: Int): String {
    val j: Int = getMaxEncodedUtfLength(i)
    val k = readVarInt()
    return if (k > j) {
        throw DecoderException("The received encoded string buffer length is longer than maximum allowed ($k > $j)")
    } else if (k < 0) {
        throw DecoderException("The received encoded string buffer length is less than zero! Weird string!")
    } else {
        val string = this.toString(this.readerIndex(), k, StandardCharsets.UTF_8)
        this.readerIndex(this.readerIndex() + k)
        if (string.length > i) {
            val var10002 = string.length
            throw DecoderException("The received string length is longer than maximum allowed ($var10002 > $i)")
        } else {
            string
        }
    }
}

fun ByteBuf.writeString(string: String): ByteBuf {
    return this.writeString(string, 32767)
}

fun ByteBuf.writeString(string: String, i: Int): ByteBuf {
    return if (string.length > i) {
        val var10002 = string.length
        throw EncoderException("String too big (was $var10002 characters, max $i)")
    } else {
        val bs = string.toByteArray(StandardCharsets.UTF_8)
        val j: Int = getMaxEncodedUtfLength(i)
        if (bs.size > j) {
            throw EncoderException("String too big (was " + bs.size + " bytes encoded, max " + j + ")")
        } else {
            writeVarInt(bs.size)
            this.writeBytes(bs)
            this
        }
    }
}

fun ByteBuf.readVarInt(): Int {
    var i = 0
    var j = 0
    var b: Byte
    do {
        b = readByte()
        i = i or (b.toInt() and 0b1111111 shl j++ * 7)
        if (j > 5) {
            throw RuntimeException("VarInt too big")
        }
    } while (b.toInt() and 0b10000000 == 0b10000000)
    return i
}

fun ByteBuf.writeVarInt(i: Int): ByteBuf {
    var input = i
    while (input and -128 != 0) {
        writeByte(input and 0b01111111 or 0b10000000)
        input = input ushr 7
    }
    writeByte(input)
    return this
}

/**
 * Writes an array of VarInts to the buffer, prefixed by the length of the array (as a VarInt).
 *
 * @see .readVarIntArray
 *
 *
 * @param array the array to write
 */
fun ByteBuf.writeVarIntArray(array: IntArray): ByteBuf {
    writeVarInt(array.size)
    for (i in array) {
        writeVarInt(i)
    }
    return this
}

/**
 * Reads an array of VarInts from this buffer.
 *
 * @see .writeVarIntArray
 */
fun ByteBuf.readVarIntArray(): IntArray {
    return readVarIntArray(readableBytes())
}

fun ByteBuf.readVarIntArray(maxLength: Int): IntArray {
    val i = readVarInt()
    return if (i > maxLength) {
        throw DecoderException("VarIntArray with size $i is bigger than allowed $maxLength")
    } else {
        val `is` = IntArray(i)
        for (j in `is`.indices) {
            `is`[j] = readVarInt()
        }
        `is`
    }
}

fun ChannelHandlerContext.send(packet: CPacket) {
    this.channel().writeAndFlush(packet)
}

fun ChannelHandlerContext.ok(data: String = "") {
    this.channel().writeAndFlush(ResponseCPacket(reqId, true, data))
}

fun ChannelHandlerContext.err(msg: String) {
    this.channel().writeAndFlush(ResponseCPacket(reqId, false, msg))
}

fun <T> ChannelHandlerContext.got(key: String): T? {
    return this.channel().attr<T>(AttributeKey.valueOf<T>(key)).get()
}

operator fun <T> ChannelHandlerContext.set(key: String, value: T) {
    this.channel().attr<T>(AttributeKey.valueOf<T>(key)).set(value)
}

var ChannelHandlerContext.clientIp: InetSocketAddress
    get() = this.got("clientIp")!!
    set(value) = this.set("clientIp", value)

var ChannelHandlerContext.reqId: Byte
    get() = this.got("reqId")!!
    set(value) = this.set("reqId", value)

var ChannelHandlerContext.account: Account?
    get() = this.got("account")
    set(value) = this.set("account", value)

suspend fun ApplicationCall.e400(msg: String? = null) {
    err(HttpStatusCode.BadRequest, msg)
}

suspend fun ApplicationCall.e401(msg: String? = null) {
    err(HttpStatusCode.Unauthorized, msg)
}

suspend fun ApplicationCall.e404(msg: String? = null) {
    err(HttpStatusCode.NotFound, msg)
}

suspend fun ApplicationCall.e500(msg: String? = null) {
    err(HttpStatusCode.InternalServerError, msg)
}

suspend fun ApplicationCall.err(status: HttpStatusCode, msg: String? = null) {
    msg?.run {
        respondText(this, status = status)
    } ?: run {
        respond(status)
    }
}

suspend fun ApplicationCall.ok(msg: String? = null) {
    msg?.run {
        respondText(this, status = HttpStatusCode.OK)
    } ?: run {
        respond(HttpStatusCode.OK)
    }
}

infix fun Parameters.got(param: String): String {
    return this[param] ?: throw ParamError("参数不全")
}

val ApplicationCall.uid
    get() =

        ObjectId(this.principal<UserIdPrincipal>()?.name ?: throw AuthError("无效会话"))

