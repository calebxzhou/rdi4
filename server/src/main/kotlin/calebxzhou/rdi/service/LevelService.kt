package calebxzhou.rdi.service

import calebxzhou.rdi.mixin.AMcServer
import calebxzhou.rdi.util.mc
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.server.RegistryLayer
import net.minecraft.server.level.ServerLevel
import org.bson.types.ObjectId

object LevelService {
    fun load(iid: ObjectId){
        /*val level = IslandLevel(iid)
        //val rKey = ResourceKey.createRegistryKey<ServerLevel>(level.resLoca)
        (mc as AMcServer).levels += level.dimension() to level

        ServerWorldEvents.LOAD.invoker().onWorldLoad(mc,level)
        level.tick { true }*/
    }
    fun remove(iid: ObjectId){
        
    }
}