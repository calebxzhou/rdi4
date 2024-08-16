package calebxzhou.rdi.mixin.client.tfc;

import calebxzhou.rdi.tfc.RTfcKnappingScreen;
import net.dries007.tfc.client.screen.KnappingScreen;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-08-13 10:47
 */
@Mixin(KnappingScreen.class)
public class mTfcKnappingScreen {
    @Inject(method = "render",at= @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void render(GuiGraphics poseStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci){
        RTfcKnappingScreen.onRender((KnappingScreen) (Object)this,poseStack,mouseX,mouseY);
    }
}
