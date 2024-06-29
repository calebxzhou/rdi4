package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.ui.component.RTooltip;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-06-29 21:44
 */
@Mixin(TooltipRenderUtil.class)
public class mTooltip {
    @Inject(method = "renderTooltipBackground",at=@At("HEAD"),cancellable = true)
    private static void RDI$renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, CallbackInfo ci) {
        RTooltip.renderTooltipBackground(guiGraphics, x, y, width, height, z);
        ci.cancel();
    }
}
