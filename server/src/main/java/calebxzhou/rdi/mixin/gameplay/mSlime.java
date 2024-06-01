package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-26 11:01
 */
@Mixin(Slime.class)
public abstract class mSlime extends Mob {
    protected mSlime(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setSize(IZ)V",
            at = @At(value = "TAIL"))
    private void setSize(int size, boolean heal, CallbackInfo ci) {
        int i = Mth.clamp((int) size, (int) 1, (int) 127);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(i * i * 2);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.8F + 0.1F * (float) i);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(i * 2);
        setHealth(i * i * 2);
    }
}
