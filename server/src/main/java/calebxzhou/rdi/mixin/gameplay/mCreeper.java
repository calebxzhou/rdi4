package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * calebxzhou @ 2024-05-26 9:45
 */
@Mixin(Creeper.class)
public class mCreeper {
    @Shadow
    @Mutable
    private int explosionRadius = 8;

}
