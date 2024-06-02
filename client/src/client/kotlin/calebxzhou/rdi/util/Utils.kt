package calebxzhou.rdi.util

/**
 * calebxzhou @ 2024-06-02 10:03
 */
fun String.isNumber(): Boolean {
    return this.isNotEmpty() && this.all { it.isDigit() }
}