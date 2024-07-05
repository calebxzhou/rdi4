package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.ui.RPauseScreen;
import calebxzhou.rdi.ui.RTitleScreen;
import calebxzhou.rdi.util.McUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-26 14:22
 */
@Mixin(PauseScreen.class)
public class mPauseScreen {
    @Inject(method = "init",at=@At("TAIL"))
    private void RDI$goPauseScreen(CallbackInfo ci){
        Minecraft.getInstance().setScreen(new RPauseScreen());
    }
   /* @Redirect(method = "onDisconnect",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V",ordinal = 2))
    private void goTitle(Minecraft instance, Screen guiScreen){
        Minecraft.getInstance().setScreen(new RTitleScreen());
    }
    //不显示反馈按钮
    @Redirect(method = "createPauseMenu",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",ordinal = 2) )
    private  <T extends LayoutElement> T noFeedback(GridLayout.RowHelper instance, T child){
        return (T) McUtils.getEmptyButton();
    }
    //不显示bug
    @Redirect(method = "createPauseMenu",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",ordinal = 3) )
    private  <T extends LayoutElement> T noReport(GridLayout.RowHelper instance, T child){
        return (T) McUtils.getEmptyButton();
    }
    @Redirect(method = "createPauseMenu",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",ordinal = 6) )
    private  <T extends LayoutElement> T noReport2(GridLayout.RowHelper instance, T child){
        return (T) McUtils.getEmptyButton();
    }*/


}
