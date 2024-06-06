package calebxzhou.rdi.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerLevel.class)
public interface AServerLevel {
    @Invoker
    void invokeTickPassenger(Entity ridingEntity, Entity passengerEntity);
}
