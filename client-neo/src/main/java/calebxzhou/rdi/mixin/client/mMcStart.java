package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.RDI;
import cpw.mods.bootstraplauncher.BootstrapLauncher;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-06-09 8:54
 */
@Mixin(BootstrapLauncher.class)
public class mMcStart {
    @Inject(method = "main",at=@At("HEAD"),remap = false)
    private static void RDI_onMcStart(String[] strings, CallbackInfo ci){
        RDI.onMcStart();
    }
}
