package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.ui.component.RTooltip;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-06-29 21:44
 */
@Mixin(TooltipRenderUtil.class)
public class mTooltip {
    @Overwrite
    public static void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z) {
        RTooltip.renderTooltipBackground(guiGraphics, x, y, width, height, z);
    }
}
