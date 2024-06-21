package calebxzhou.rdi.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-06-20 22:49
 */
@Mixin(Gui.class)
public abstract class mGui {
    @Shadow public abstract Font getFont();

    @Shadow protected abstract void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean renderHighlight, boolean halfHeart);

    /*@ModifyConstant(method = "renderHearts",constant = @Constant(intValue = 8))
            private int heartW(int constant){
                return 2;
            }
            @ModifyConstant(method = "renderHearts",constant = @Constant(intValue = 10))
            private int heartHeight(int constant){
                return 50;
            }*/
    @Overwrite
    private void renderHearts(
            GuiGraphics guiGraphics,
            Player player,
            int x,
            int y,
            int height,
            int offsetHeartIndex,
            float maxHealth,
            int currentHealth,
            int displayHealth,
            int absorptionAmount,
            boolean renderHighlight
    ) {
        renderHeart(guiGraphics,Gui.HeartType.forPlayer(player),x,y,0,false,false);
        guiGraphics.drawString(getFont(), Component.literal(currentHealth+"").withStyle(ChatFormatting.RED),x+10,y,0xffffff);
    }
}
