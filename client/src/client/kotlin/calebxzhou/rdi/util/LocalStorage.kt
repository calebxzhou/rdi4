package calebxzhou.rdi.util

import calebxzhou.rdi.log
import java.io.*

/**
 * calebxzhou @ 2024-06-02 22:35
 */
object LocalStorage {

    private var kv = hashMapOf<String, String>()
    init {
        val file = File("rdi_kv.ser")
        if (file.exists()) {
            try {
                val ois = ObjectInputStream(FileInputStream(file))
                @Suppress("UNCHECKED_CAST")
                kv = ois.readObject() as HashMap<String, String>
                ois.close()
            } catch (e: Exception) {
                log.error("读取缓存出错：{}",e.toString())
                log.error("重新创建缓存")
            }
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

    operator fun set(key: String, value: String) {
        kv[key] = value
        write()
    }
}
