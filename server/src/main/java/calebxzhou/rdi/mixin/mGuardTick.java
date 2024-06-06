package calebxzhou.rdi.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * calebxzhou @ 2024-06-05 9:51
 */
public class mGuardTick {
}
@Mixin(MinecraftServer.class)
abstract
class mTickInvertServer {
    @Shadow
    @Final
    private List<Runnable> tickables;

    @Shadow
    public abstract void tickChildren(BooleanSupplier hasTimeLeft);


    @Redirect(method = "tickServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"))
    private void tickServerChildrenNoCrash(MinecraftServer instance, BooleanSupplier bs) {
        try {
            tickChildren(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




@Mixin(ServerLevel.class)
abstract
class mGuardServerLevelTick {

    @Redirect(method = "tickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"))
    private void tickBlock(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        try {
            blockState.tick(serverLevel, blockPos, serverLevel.random);
        } catch (Exception e) {
            serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(),0);

            e.printStackTrace();
        }

    }

    @Redirect(method = "tickFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void tickFluid(FluidState fluidState, Level level, BlockPos blockPos) {
        try {
            fluidState.tick(level, blockPos);
        } catch (Exception e) {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(),0);
            e.printStackTrace();
        }

    }


}

@Mixin(ServerChunkCache.class)
class mGuardChunkTick {
    @Shadow
    @Final
    private DistanceManager distanceManager;
    @Shadow
    @Final
    public ChunkMap chunkMap;

    @Redirect(method = "runDistanceManagerUpdates()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/DistanceManager;runAllUpdates(Lnet/minecraft/server/level/ChunkMap;)Z"))
    private boolean nocrashTick(DistanceManager instance, ChunkMap chunkStorage) {
        //return true;
        try {
            return this.distanceManager.runAllUpdates(this.chunkMap);
        } catch (Exception t) {
            t.printStackTrace();
        }
        return true;
    }

}
