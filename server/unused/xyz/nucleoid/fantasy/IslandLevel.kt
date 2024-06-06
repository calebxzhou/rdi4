package xyz.nucleoid.fantasy

import calebxzhou.rdi.Const
import calebxzhou.rdi.mixin.AMcServer
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.toBase36
import com.google.common.collect.ImmutableList
import net.minecraft.Util
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.storage.DerivedLevelData
import org.bson.types.ObjectId
import xyz.nucleoid.fantasy.util.VoidWorldProgressListener

class IslandLevel internal constructor(
    iid: ObjectId
) : ServerLevel(
    mc,
    Util.backgroundExecutor(),
    (mc as AMcServer).storageSource,
    DerivedLevelData(mc.worldData, mc.worldData.overworldData()),
    ResourceKey.create(Registries.DIMENSION, getResourceLocation(iid)),
    (mc as AMcServer).registries.compositeAccess().registryOrThrow(Registries.LEVEL_STEM).get(LevelStem.OVERWORLD)!!,
    xyz.nucleoid.fantasy.util.VoidWorldProgressListener.INSTANCE,
    false,
    BiomeManager.obfuscateSeed(Const.SEED),
    ImmutableList.of(),
    true, null
) {
    val resLoca = getResourceLocation(iid)
    companion object{
        fun getResourceLocation(iid: ObjectId): ResourceLocation {
            return ResourceLocation("rdi", "i${iid.toByteArray().toBase36()}")
        }
    }
}
