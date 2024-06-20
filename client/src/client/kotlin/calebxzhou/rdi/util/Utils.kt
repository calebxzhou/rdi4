package calebxzhou.rdi.util

import calebxzhou.rdi.RDI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.UUID

/**
 * calebxzhou @ 2024-06-02 10:03
 */
val scope = CoroutineScope(Dispatchers.IO)
fun bgTask(block: suspend CoroutineScope.() -> Unit){
    scope.launch { block() }
}
fun String.isNumber(): Boolean {
    return this.isNotEmpty() && this.all { it.isDigit() }
}
fun String.isValidUuid() : Boolean{
    val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$".toRegex()
    return uuidRegex.matches(this)
}
fun getFileInJarUrl(fileInJar: String): String {
    return RDI::class.java
        .classLoader
        .getResource(fileInJar)!!.file.replace("%20", " ")
}
fun getJarResourceStream(path: String): InputStream? {
    return RDI::class.java.classLoader.getResourceAsStream(path)
}
fun getFileInJar(fileInJar: String): File {
    return File(getFileInJarUrl(fileInJar))
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