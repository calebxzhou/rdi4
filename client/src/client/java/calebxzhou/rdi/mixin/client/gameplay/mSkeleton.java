package calebxzhou.rdi.mixin.client.gameplay;

import kotlin.random.URandomKt;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-05-26 10:06
 */
@Mixin(AbstractSkeleton.class)
public abstract class mSkeleton {
    @ModifyConstant(
            method = "reassessWeaponGoal()V",
            constant = @Constant(intValue = 20)
    )
    private int changeAtkSpeed(int spd){
        return 2;
    }
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