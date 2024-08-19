package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.ui.screen.RTitleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class mTitleScreen {

    @Inject(method = "init",at=@At("HEAD"), cancellable = true)
    private void alwaysGoNew(CallbackInfo ci){
        Minecraft.getInstance().setScreen(new RTitleScreen());
        ci.cancel();
    }
 
}
