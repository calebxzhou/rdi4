package calebxzhou.rdi.mixin.client.tfc;

import calebxzhou.rdi.ui.RGui;
import net.dries007.tfc.client.IngameOverlays;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-08-03 10:22
 */
@Mixin(IngameOverlays.class)
public class mTfcOverlay {
    @Inject(method = "renderHealthBar",at=@At("HEAD"),cancellable = true,remap = false)
    private static void hp(LivingEntity entity, ForgeGui gui, GuiGraphics graphics, int width, int height, CallbackInfo ci){
        RGui.renderHealthBar(entity, gui, graphics, width, height);
        ci.cancel();
    }
    @Inject(method = "renderFood",at=@At("HEAD"),cancellable = true,remap = false)
    private static void food(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height, CallbackInfo ci){
        RGui.renderFood(gui, graphics, partialTicks, width, height);
        ci.cancel();
    }
    @Inject(method = "renderThirst",at=@At("HEAD"),cancellable = true,remap = false)
    private static void thirst(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height, CallbackInfo ci){
        RGui.renderThirst(gui, graphics, partialTicks, width, height);
        ci.cancel();
    }

}
