package calebxzhou.rdi.mixin;

import calebxzhou.rdi.RDI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * calebxzhou @ 2024-06-05 9:29
 */
@Mixin(NaturalSpawner.class)
public class mSpawnEntity {
    /*
    ServerChunkCache
    @Redirect(method = "tickChunks",at= @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isNaturalSpawningAllowed(Lnet/minecraft/world/level/ChunkPos;)Z"))
    private boolean lagNoSpawn(ServerLevel instance, ChunkPos chunkPos){
        return RDI.isLagging();
    }*/
    //到处都刷

   /* @Redirect(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/NaturalSpawner;isValidSpawnPostitionForType(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;Lnet/minecraft/core/BlockPos$MutableBlockPos;D)Z"))
    private static boolean alwaysValid(ServerLevel level, MobCategory category, StructureManager structureManager, ChunkGenerator generator, MobSpawnSettings.SpawnerData data, BlockPos.MutableBlockPos pos, double distance){
        if (level.getBrightness(LightLayer.BLOCK, pos) < 6
                && level.dayTime()>13000) {
            return true;
        }else{
            return false;
        }
    }
    @Redirect(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;test(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/ChunkAccess;)Z"))
    private static boolean alwaysValid2(NaturalSpawner.SpawnPredicate instance, EntityType<?> entityType, BlockPos blockPos, ChunkAccess chunkAccess){
        return true;
    }
    //防止虚空刷怪
    @Inject(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V"
    ,at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;moveTo(DDDFF)V",shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void noVoid(MobCategory category, ServerLevel level, ChunkAccess chunk, BlockPos pos, NaturalSpawner.SpawnPredicate filter, NaturalSpawner.AfterSpawnCallback callback, CallbackInfo ci, StructureManager structureManager, ChunkGenerator chunkGenerator, int i, BlockState blockState, BlockPos.MutableBlockPos mutableBlockPos, int j, int k, int l, int m, int n, MobSpawnSettings.SpawnerData spawnerData, SpawnGroupData spawnGroupData, int o, int p, int q, double d, double e, Player player, double f, Mob mob){
        //往上移动两格
        mob.moveTo(mob.getX(),mob.getY()+2,mob.getZ());
    }
    @Redirect(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/NaturalSpawner;isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z"))
    private static boolean alwaysValid3(ServerLevel level, Mob mob, double distance){

        if(mob.getBlockStateOn().getBlock()== Blocks.AIR){
            return false;
        }else{
            return true;
        }
    }*/
}
@Mixin(Monster.class)
class mSpawnEntity2{
    //方块亮度<6 直接刷
    @Inject(method = "isDarkEnoughToSpawn",at=@At("HEAD"), cancellable = true)
    private static void judgeBlockLight(ServerLevelAccessor level, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir){
        if (level.getBrightness(LightLayer.BLOCK, pos) < 6) {
            cir.setReturnValue(true);
        }
    }
    //不检测是否实体方块
    @Redirect(method = "checkMonsterSpawnRules",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Monster;checkMobSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z"))
    private static boolean noCheckSolidBlock(EntityType entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource){
        return true;
        /* if(levelAccessor.getBlockState(blockPos).getBlock()== Blocks.AIR){
            return false;
        }else{
            return true;
        }*/
    }
}
