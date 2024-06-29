package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-05-26 10:54
 */
@Mixin(Mob.class)
public abstract class mMobNoDayBurn extends LivingEntity {

    protected mMobNoDayBurn(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    //所有怪物不受白天影响 除了幻翼
    /*@Overwrite
    public boolean isSunBurnTick() {
        return level().isDay() && getType() == EntityType.PHANTOM;
    }*/
}