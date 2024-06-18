package calebxzhou.rdi.util

import calebxzhou.rdi.RDI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.nio.charset.StandardCharsets
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


object Utils {
    @JvmStatic
    fun createUuid(name: String): UUID {
        return UUID.nameUUIDFromBytes(name.toByteArray(StandardCharsets.UTF_8));
    }
}