package calebxzhou.rdi.mixin.client.gameplay;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-06-05 19:43
 */
@Mixin(Player.class)
public class mPlayer {
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.11F)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.LUCK);
    }
}
