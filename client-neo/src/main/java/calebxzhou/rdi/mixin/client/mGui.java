package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.service.NetMetrics;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-06-20 22:49
 */
@Mixin(Gui.class)
public abstract class mGui {
    @Shadow public abstract Font getFont();

    /*@Redirect(method = "renderPlayerHealth",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"))
    private int RDI$noDisplayVanillaArmor(Player instance){
        return 0;
    }*/
}
