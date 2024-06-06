package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-05-26 10:06
 */
@Mixin(AbstractSkeleton.class)
public abstract class mSkeleton {
    /*@Redirect(method = "reassessWeaponGoal",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/RangedBowAttackGoal;setMinAttackInterval(I)V"))
    private void setAtkInteval(RangedBowAttackGoal instance, int attackCooldown){
        instance.setMinAttackInterval(1);
    }*/
    /**
     * @author 1
     * @reason 1
     */
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.MAX_HEALTH,50);
    }
}
@Mixin(RangedBowAttackGoal.class)
class mSkeletonGoal{
    /*@ModifyConstant(method = "tick",constant = @Constant(intValue = 20))
    private int getAtkTIck(int constant){
        return 0;
    }*/
}