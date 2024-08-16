package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.util.Helpers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * calebxzhou @ 2024-08-13 10:23
 */
@Mixin(ItemSizeManager.class)
public class mTfcSizeTooltip {
    //在重量和体积图标后面加上汉字
    @Redirect(method = "addTooltipInfo", at= @At(value = "INVOKE",ordinal = 0, target = "Lnet/dries007/tfc/util/Helpers;translateEnum(Ljava/lang/Enum;)Lnet/minecraft/network/chat/MutableComponent;"))
    private static MutableComponent tooltip(Enum<?> anEnum){
        return Component.literal("重量").append(Helpers.translateEnum(anEnum));
    }
    @Redirect(method = "addTooltipInfo", at= @At(value = "INVOKE",ordinal = 1, target = "Lnet/dries007/tfc/util/Helpers;translateEnum(Ljava/lang/Enum;)Lnet/minecraft/network/chat/MutableComponent;"))
    private static MutableComponent tooltip2(Enum<?> anEnum){
        return Component.literal("体积").append(Helpers.translateEnum(anEnum));
    }
}
