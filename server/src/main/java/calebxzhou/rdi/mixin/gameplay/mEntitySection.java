package calebxzhou.rdi.mixin.gameplay;

import calebxzhou.rdi.service.REntitySection;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-06-21 8:25
 */
@Mixin(EntitySectionStorage.class)
public class mEntitySection {
    /*@Redirect(method = "createSection",at= @At(value = "NEW", target = "(Ljava/lang/Class;Lnet/minecraft/world/level/entity/Visibility;)Lnet/minecraft/world/level/entity/EntitySection;"))
    private EntitySection newREntitySeciton(Class entityClazz, Visibility chunkStatus){

        return new REntitySection(entityClazz,chunkStatus);
    }*/
}
