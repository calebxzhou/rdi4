package calebxzhou.rdi.mixin;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * calebxzhou @ 2024-05-26 14:12
 */
//生成空白世界
@Mixin(NoiseBasedChunkGenerator.class)
public class mWorldGen {
    @Overwrite
    public void buildSurface(
            ChunkAccess chunk,
            WorldGenerationContext context,
            RandomState random,
            StructureManager structureManager,
            BiomeManager biomeManager,
            Registry<Biome> biomes,
            Blender blender
    ) {
    }

    @Overwrite
    public void applyCarvers(
            WorldGenRegion level,
            long seed,
            RandomState random,
            BiomeManager biomeManager,
            StructureManager structureManager,
            ChunkAccess chunk,
            GenerationStep.Carving step
    ) {
    }
    @Inject(method = "fillFromNoise",at=@At(value = "HEAD"), cancellable = true)
    private void noFill(Executor executor, Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir){
        cir.setReturnValue(CompletableFuture.completedFuture(chunk));
    }
}
