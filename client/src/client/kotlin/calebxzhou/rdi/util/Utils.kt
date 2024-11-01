package calebxzhou.rdi.util

import calebxzhou.rdi.RDI
import calebxzhou.rdi.log
import calebxzhou.rdi.ui.general.alertErr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.*

/**
 * calebxzhou @ 2024-06-02 10:03
 */
val scope = CoroutineScope(Dispatchers.IO)
fun bgTask(block: suspend CoroutineScope.() -> Unit){
    scope.launch { try {
        block()
    } catch (e: Exception) {
        log.error("bgtask error: $e")
        alertErr("${e.localizedMessage}")
        e.printStackTrace()
    }
    }
}
fun String.isNumber(): Boolean {
    return this.isNotEmpty() && this.all { it.isDigit() }
}
fun String.isValidUuid() : Boolean{
    val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$".toRegex()
    return uuidRegex.matches(this)
}
fun String.extractDomain(): String{
    val uri = java.net.URI(this)
    return "${uri.scheme}://${uri.host}/"
}
fun getFileInJarUrl(fileInJar: String): URL? {
    return RDI::class.java
        .classLoader
        .getResource(fileInJar)
}
fun getFileInJarUrlString(fileInJar: String): String {
    return RDI::class.java
        .classLoader
        .getResource(fileInJar)!!.file.replace("%20", " ")
}
fun getJarResourceStream(path: String): InputStream? {
    return RDI::class.java.classLoader.getResourceAsStream(path)
}
fun getFileInJar(fileInJar: String): File {
    return File(getFileInJarUrlString(fileInJar))
}
fun String.decodeBase64(): String {
    // Decode the Base64 string to a byte array
    val decodedBytes = Base64.getDecoder().decode(this)
    // Convert the byte array to a String
    return String(decodedBytes, Charsets.UTF_8)
}
fun humanReadableByteCount(bytes: Long, si: Boolean = false): String {
    val unit = if (si) 1000 else 1024
    if (bytes < unit) return "$bytes B"
    val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
    val prefix = (if (si) "kMGTPE" else "KMGTPE")[exp-1].toString() //+ (if (si) "" else "i")
    val result = bytes / Math.pow(unit.toDouble(), exp.toDouble())
    return String.format("%.2f %s", result, prefix)
}
fun String.isValidHttpUrl(): Boolean {
    val urlRegex = "^(http://|https://).+".toRegex()
    return this.matches(urlRegex)
}
val periodOfDay: String = when (LocalDateTime.now().hour) {
    in 0..5 -> "凌晨"
    in 6..8 -> "早上"
    in 9..10 -> "上午"
    in 11..12 -> "中午"
    in 13..17 -> "下午"
    in 18..23 -> "晚上"
    else -> "您好"
}
object Utils {
    @JvmStatic
    fun createUuid(name: String): UUID {
        return UUID.nameUUIDFromBytes(name.toByteArray(StandardCharsets.UTF_8));
    }
}