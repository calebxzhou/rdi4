package calebxzhou.rdi.mixin;

import calebxzhou.rdi.Const;
import com.google.gson.JsonObject;
import net.minecraft.DetectedVersion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-23 12:33
 */
@Mixin(DetectedVersion.class)
public class mProtocolVersion {
    @Shadow
    @Final
    @Mutable
    private int protocolVersion;
    @Mutable
    @Shadow @Final private String name;

    @Inject(
            method = "<init>(Lcom/google/gson/JsonObject;)V",
            at=@At("TAIL")
    )
    private void changeProtocolVersion(JsonObject json, CallbackInfo ci){
        protocolVersion= Const.VERSION;
        name = Const.VERSION_STR;
    }
}
