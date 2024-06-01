package calebxzhou.rdi.util

import calebxzhou.rdi.RDI
import java.io.File

/**
 * Created  on 2023-04-07,22:59.
 */
fun getFileInJarUrl(fileInJar: String): String {
    return RDI::class.java
        .classLoader
        .getResource(fileInJar)!!.file.replace("%20", " ")
}

fun getFileInJar(fileInJar: String): File {
    return File(getFileInJarUrl(fileInJar))
}

