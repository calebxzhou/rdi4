package calebxzhou.rdi.util

import java.io.*

/**
 * calebxzhou @ 2024-06-02 22:35
 */
object LocalStorage {

    private var kv = hashMapOf<String, String>()
    init {
        val file = File("rdi_kv.ser")
        if (file.exists()) {
            val ois = ObjectInputStream(FileInputStream(file))
            @Suppress("UNCHECKED_CAST")
            kv = ois.readObject() as HashMap<String, String>
            ois.close()
        }
    }
    operator fun plusAssign(pair: Pair<String,String>){
        kv += pair
        write()
    }
    operator fun get(key: String): String?{
        return kv[key]
    }
    private fun write() {
        val oos = ObjectOutputStream(FileOutputStream("rdi_kv.ser"))
        oos.writeObject(kv)
        oos.close()
    }
}
