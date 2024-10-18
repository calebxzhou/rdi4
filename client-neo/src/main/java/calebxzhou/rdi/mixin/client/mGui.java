package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.lang.EnglishStorage;
import calebxzhou.rdi.service.NetMetrics;
import calebxzhou.rdi.ui.RGui;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * calebxzhou @ 2024-06-20 22:49
 */
@Mixin(Gui.class)
public abstract class mGui {
    @Shadow public abstract Font getFont();

    @Shadow protected ItemStack lastToolHighlight;

    @Shadow protected int screenWidth;

    @Inject(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V",locals = LocalCapture.CAPTURE_FAILHARD, at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"))
    private void RDI_renderSelectedItemEnglishName(GuiGraphics pGuiGraphics, int yShift, CallbackInfo ci, MutableComponent mutablecomponent, Component highlightTip, int i, int j, int k, int l, Font font){
        String id = lastToolHighlight.getDescriptionId();
        RGui.renderSelectedItemEnglishName(pGuiGraphics,id,j,k,l);
    }
    /*@Redirect(method = "renderPlayerHealth",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"))
    private int RDI$noDisplayVanillaArmor(Player instance){
        return 0;
    }*/
}
