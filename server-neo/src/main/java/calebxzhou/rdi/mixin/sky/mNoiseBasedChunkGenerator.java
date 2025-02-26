package calebxzhou.rdi.mixin.sky;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * calebxzhou @ 2025-02-25 22:09
 */
@Mixin(NoiseBasedChunkGenerator.class)
public class mNoiseBasedChunkGenerator {
    @Inject(method = "buildSurface(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/levelgen/blending/Blender;)V", at = @At("HEAD"), cancellable = true)
    public void RDI$buildSurface(
            ChunkAccess pChunk, WorldGenerationContext pContext, RandomState pRandom, StructureManager pStructureManager, BiomeManager pBiomeManager, Registry<Biome> pBiomes, Blender pBlender, CallbackInfo ci
    ) {
        //空岛模式 取消地形生成
            ci.cancel();
    }

    @Inject(method = "applyCarvers", at = @At("HEAD"), cancellable = true)
    public void RDI$applyCarvers(
            WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step, CallbackInfo ci
    ) {
        //空岛模式 取消地形生成
            ci.cancel();
    }

    @Inject(method = "fillFromNoise", at = @At(value = "HEAD"), cancellable = true)
    private void RDI$noFill(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        //空岛模式 取消地形生成
            cir.setReturnValue(CompletableFuture.completedFuture(pChunk));
    }
}
