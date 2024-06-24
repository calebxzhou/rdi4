package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.Const;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * calebxzhou @ 2024-05-23 12:21
 */

@Mixin(Minecraft.class)
public class mWindowTitle {

    @Inject(method = "createTitle",at=@At("HEAD"), cancellable = true)
    private void createTitle(CallbackInfoReturnable<String> cir){
        cir.setReturnValue(Const.VERSION_STR);
    }

}