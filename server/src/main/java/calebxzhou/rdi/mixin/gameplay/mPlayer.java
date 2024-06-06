package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * calebxzhou @ 2024-06-05 19:43
 */
@Mixin(Player.class)
public class mPlayer{
    @Overwrite
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.11F)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.LUCK);
    }
    @Inject(method = "canBeSeenAsEnemy" ,at=@At("HEAD"), cancellable = true)
    public void canBeSeenAsEnemy(CallbackInfoReturnable<Boolean> cir) {
        if((Object)this instanceof ServerPlayer sp){
            boolean result = (sp.gameMode.getGameModeForPlayer() == GameType.SURVIVAL || sp.isCreative()) && sp.canBeSeenByAnyone();
            cir.setReturnValue(result);
        }

    }
}
