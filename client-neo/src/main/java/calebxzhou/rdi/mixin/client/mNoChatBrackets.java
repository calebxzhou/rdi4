package calebxzhou.rdi.mixin.client;


import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-05-23 12:34
 */
//去掉说话的尖括号
@Mixin(TranslatableContents.class)
public class mNoChatBrackets {
    @Mutable
    @Shadow @Final private String key;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modify(String key, String fallback, Object[] args, CallbackInfo ci) {
        switch(key) {
            case "chat.type.text":
            case "chat.type.emote":
            case "chat.type.announcement":
            case "chat.type.admin":
            case "chat.type.team.text":
            case "chat.type.team.sent":
                key = "rdi." + key;
                break;
        }
    }

}