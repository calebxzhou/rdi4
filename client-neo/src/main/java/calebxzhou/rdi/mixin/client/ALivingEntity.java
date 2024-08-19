package calebxzhou.rdi.mixin.client;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface ALivingEntity {
    @Accessor
    boolean getJumping();
}
