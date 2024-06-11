package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.RDI;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-06-09 8:54
 */
@Mixin(Main.class)
public class mMcStart {
    @Inject(method = "main",at=@At("HEAD"),remap = false)
    private static void load(String[] strings, CallbackInfo ci){
        RDI.onMcStart();
    }
}
