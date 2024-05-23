package calebxzhou.rdi.mixin.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
/**
 * calebxzhou @ 2024-05-23 12:23
 */

@Mixin(PlayerTabOverlay.class)
public class mTabPlayerHud {

    /**
     * @author calebxzhou
     * @reason 旁观模式不显示昵称
     */
    @Overwrite
    private Component decorateName(PlayerInfo entry, MutableComponent name) {
        return name;
    }

    @Redirect(
            method = "render",
            at =@At(
                    value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"
            )
    )
    private int noSpecGray(GuiGraphics instance, Font font, Component text, int x, int y, int color) {
        return 0;
    }

    //离线登录也显示头像
    @Redirect(method = "render",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/network/Connection;isEncrypted()Z"))
    private boolean alwaysDisplayAvatar(Connection instance){
        return true;
    }


}