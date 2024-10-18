package calebxzhou.rdi.mixin.client.tfc;

import calebxzhou.rdi.tfc.RTfcItemSizeManager;
import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
   @Overwrite
   public static void addTooltipInfo(ItemStack stack, List<Component> text)
   {
       RTfcItemSizeManager.addTooltipInfo(stack, text);
   }
}
