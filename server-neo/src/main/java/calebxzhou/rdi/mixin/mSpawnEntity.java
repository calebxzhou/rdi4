package calebxzhou.rdi.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * calebxzhou @ 2024-06-05 9:29
 */
@Mixin(NaturalSpawner.class)
public class mSpawnEntity {

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
    /*@Redirect(method = "checkMonsterSpawnRules",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Monster;checkMobSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z"))
    private static boolean noCheckSolidBlock(EntityType entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource){
        return true;

    }*/
}
