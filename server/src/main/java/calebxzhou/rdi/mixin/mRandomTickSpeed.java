package calebxzhou.rdi.mixin;

import calebxzhou.rdi.RDI;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-06-05 16:56
 */
@Mixin(ServerChunkCache.class)
public class mRandomTickSpeed {
    /*@Redirect(method = "tickChunks",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getInt(Lnet/minecraft/world/level/GameRules$Key;)I"))
    private int randomTickSpeed(GameRules instance, GameRules.Key<GameRules.IntegerValue> key){
        return 30 + RDI.getPlayerAmount()*3;
    }*/
}
