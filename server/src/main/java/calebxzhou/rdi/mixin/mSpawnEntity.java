package calebxzhou.rdi.mixin;

import calebxzhou.rdi.RDI;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-06-05 9:29
 */
@Mixin(ServerChunkCache.class)
public class mSpawnEntity {
    //todo 5秒刷一次
    @Redirect(method = "tickChunks",at= @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isNaturalSpawningAllowed(Lnet/minecraft/world/level/ChunkPos;)Z"))
    private boolean lagNoSpawn(ServerLevel instance, ChunkPos chunkPos){
        return RDI.isLagging();
    }
}
