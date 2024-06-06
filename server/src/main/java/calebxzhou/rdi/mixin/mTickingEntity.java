package calebxzhou.rdi.mixin;

import calebxzhou.rdi.service.EntityTicker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;

/**
 * calebxzhou @ 2024-06-04 22:54
 */@Mixin(Level.class)
public class mTickingEntity {
    @Overwrite
    public <T extends Entity> void guardEntityTick(Consumer<T> consumerEntity, T entity) {
        EntityTicker.tick(consumerEntity, entity);
    }
}