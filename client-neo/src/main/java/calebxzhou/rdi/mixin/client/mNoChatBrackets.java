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


}