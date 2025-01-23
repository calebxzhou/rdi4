package calebxzhou.rdi.lang

import calebxzhou.rdi.logger
import calebxzhou.rdi.mixin.client.AClientLanguage
import calebxzhou.rdi.rAsync
import calebxzhou.rdi.util.mc
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.minecraft.client.resources.language.ClientLanguage

object MultiLangStorage {
    //语言key-语言名称-值
    //item.block - zh_cn - 方块
    private val keyLangValue = hashMapOf<String, MutableMap<String, String>>()
    operator fun get(key: String): MutableMap<String, String>? = keyLangValue[key]
    fun load() = rAsync {
        logger.info("载入多语言")
        //语言名称-语言key-值
        val langKeyValue = hashMapOf<String, MutableMap<String, String>>()
        runBlocking {
            mc.languageManager.languages.forEach { langName, _ ->
                async {
                    val clientLang = ClientLanguage.loadFrom(mc.resourceManager, listOf(langName), false) as AClientLanguage
                    langKeyValue += langName to clientLang.storage
                }
            }

        }
        langKeyValue.forEach { (langName, keyValueMap) ->
            keyValueMap.forEach { (key, value) ->
                keyLangValue.computeIfAbsent(key) { hashMapOf() }.put(langName, value)
            }
        }
        logger.info("载入完成 ${langKeyValue.size}个语言 ${keyLangValue.size} 个条目")
    }
}