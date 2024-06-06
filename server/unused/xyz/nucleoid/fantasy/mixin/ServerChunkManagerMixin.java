package xyz.nucleoid.fantasy.mixin;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nucleoid.fantasy.FantasyWorldAccess;

@Mixin(ServerChunkCache.class)
public class ServerChunkManagerMixin {
    @Shadow
    @Final
	ServerLevel level;

    @Inject(method = "pollTask", at = @At("HEAD"), cancellable = true)
    private void pollTask(CallbackInfoReturnable<Boolean> ci) {
        if (!((FantasyWorldAccess) this.level).fantasy$shouldTick()) {
            ci.setReturnValue(false);
        }
    }
}
