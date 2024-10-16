package calebxzhou.rdi.lang

import appeng.libs.micromark.commonmark.ListConstruct.list
import calebxzhou.rdi.rAsync
import net.minecraft.client.resources.language.ClientLanguage
import net.minecraft.client.resources.language.LanguageManager
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object EnglishStorage {
    lateinit var lang : ClientLanguage

}