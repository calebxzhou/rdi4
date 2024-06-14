package calebxzhou.rdi.util

import java.nio.charset.StandardCharsets
import java.util.UUID

/**
 * calebxzhou @ 2024-06-02 10:03
 */
fun String.isNumber(): Boolean {
    return this.isNotEmpty() && this.all { it.isDigit() }
}
fun String.isValidUuid() : Boolean{
    val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$".toRegex()
    return uuidRegex.matches(this)
}
object Utils {
    @JvmStatic
    fun createUuid(name: String): UUID {
        return UUID.nameUUIDFromBytes(name.toByteArray(StandardCharsets.UTF_8));
    }
}