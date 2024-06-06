package calebxzhou.rdi.util

import calebxzhou.rdi.log
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

/**
 * calebxzhou @ 2024-06-01 21:36
 */
val mcText: (String) -> MutableComponent = {
    Component.literal(it)
}
lateinit var mc: MinecraftServer
fun forEachEntity(todo: (ServerLevel, Entity)->Unit){

    try {
        mc.allLevels.forEach { it.allEntities.forEach { ent -> if(ent!=null) todo(it,ent) } }
    } catch (e: Exception) {
        log.warn("移除实体出现错误：{}",e.toString())
    }
}

object McUtils {

}