package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-26 10:33
 */
@Mixin(Ghast.class)
public class mGhast {

    //50血
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.FOLLOW_RANGE, 128.0);
    }
    //生成概率x4
    @Overwrite
    public static boolean checkGhastSpawnRules(EntityType<Ghast> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return randomSource.nextInt(3) == 0 && Ghast.checkMobSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
    }

    //一次生成16个
    @Overwrite
    public int getMaxSpawnClusterSize() {
        return 16;
    }
}
@Mixin(Ghast.GhastMoveControl.class)
class mGhastMove{
    @ModifyConstant(
            method = "tick"
            ,constant = @Constant(doubleValue = 0.1D)
    )
    private double change(double d){
        return 0.5D;
    }
}
@Mixin(Ghast.RandomFloatAroundGoal.class)
class mGhastFly{
    @ModifyConstant(
            method = "start"
            ,constant = @Constant(doubleValue = 1.0D)
    )
    private double change(double d){
        return 2.0D;
    }
}
@Mixin(Ghast.GhastShootFireballGoal.class)
class mGhastShoot{
    @Shadow
    @Mutable
    public int chargeTime;
    @Shadow @Final
    private Ghast ghast;

    /*@ModifyConstant(
            method = "tick"
            ,constant = @Constant(intValue = 20)
    )
    private int changeCD(int constant){
        return 11;
    }*/
    /*@ModifyConstant(
            method = "tick"
            ,constant = @Constant(doubleValue = 4.0D)
    )
    private double changeSped(double constant){
        return 6.0D;
    }*/

    //只有地狱才发火球
    /*@Inject(
            method = "tick",
            at=@At("HEAD"),
            cancellable = true)
    private void netherFireballOnly(CallbackInfo ci){
        if (ghast.level().dimensionTypeId() != BuiltinDimensionTypes.NETHER) {
            ci.cancel();
        }
    }*/
    /*@Inject(
            method = "tick",
            at=@At("TAIL")
    )
    private void changeCd2(CallbackInfo ci){
        if(chargeTime<=-40){
            chargeTime=9;
            this.ghast.setCharging(true);
        }
    }*/
}
