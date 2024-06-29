package calebxzhou.rdi.mixin;

import calebxzhou.rdi.Const;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * calebxzhou @ 2024-05-26 14:12
 */
//生成空白世界
@Mixin(NoiseBasedChunkGenerator.class)
public class mWorldGen {
    @Inject(method = "buildSurface(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/levelgen/blending/Blender;)V", at = @At("HEAD"), cancellable = true)
    public void RDI$buildSurface(
            ChunkAccess chunk, WorldGenerationContext context, RandomState random, StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> biomes, Blender blender, CallbackInfo ci
    ) {
        //空岛模式 取消地形生成
        if (!Const.INSTANCE.isLandMode()) {
            ci.cancel();
        }
    }

    @Inject(method = "applyCarvers", at = @At("HEAD"), cancellable = true)
    public void RDI$applyCarvers(
            WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step, CallbackInfo ci
    ) {
        //空岛模式 取消地形生成
        if (!Const.INSTANCE.isLandMode()) {
            ci.cancel();
        }
    }

    @Inject(method = "fillFromNoise", at = @At(value = "HEAD"), cancellable = true)
    private void noFill(Executor executor, Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
//空岛模式 取消地形生成
        if (!Const.INSTANCE.isLandMode()) {
            cir.setReturnValue(CompletableFuture.completedFuture(chunk));
        }
    }
}
