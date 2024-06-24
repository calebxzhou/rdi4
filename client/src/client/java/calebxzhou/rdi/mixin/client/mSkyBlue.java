package calebxzhou.rdi.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-23 12:30
 */
@Mixin(ClientLevel.ClientLevelData.class)
public class mSkyBlue {
    @Shadow
    @Final
    @Mutable
    private boolean isFlat;

    //低于水平面 天空仍然蓝
    @Inject(method = "<init>",at=@At("TAIL"))
    private void asd(Difficulty difficulty, boolean bl, boolean bl2, CallbackInfo ci){
        isFlat=true;
    }
}
