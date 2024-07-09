package calebxzhou.rdi.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-26 9:32
 */
@Mixin(Minecraft.class)
public class mScreenTransition {
    @Shadow public Screen screen;

    @Inject(method = "setScreen",at=@At("HEAD"))
    private void log(Screen guiScreen, CallbackInfo ci){

        /*RDI.log().info("画面迁移：{} -> {}", screen != null ? screen.getClass().getCanonicalName() : "null",guiScreen.getClass()!=null ? guiScreen.getClass().getCanonicalName() : "null");*/
    }
}
