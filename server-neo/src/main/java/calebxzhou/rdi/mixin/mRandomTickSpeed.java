package calebxzhou.rdi.mixin;

import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;

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
