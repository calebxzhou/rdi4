package calebxzhou.rdi.mixin.client.jade;

import calebxzhou.rdi.lang.EnglishStorage;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.addon.core.ModNameProvider;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * calebxzhou @ 2024-10-16 21:49
 */
@Mixin(ModNameProvider.class)
public class mDisplayObjectEnglish {
    @Inject(method = "appendTooltip(Lsnownee/jade/api/ITooltip;Lsnownee/jade/api/BlockAccessor;Lsnownee/jade/api/config/IPluginConfig;)V",at=@At("TAIL"))
    private void block(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config, CallbackInfo ci){
        tooltip.add(Component.literal(EnglishStorage.lang.getOrDefault(accessor.getBlock().getDescriptionId())));
    }
    @Inject(method = "appendTooltip(Lsnownee/jade/api/ITooltip;Lsnownee/jade/api/EntityAccessor;Lsnownee/jade/api/config/IPluginConfig;)V",at=@At("TAIL"))
    private void entity(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config, CallbackInfo ci){
        tooltip.add(Component.literal(EnglishStorage.lang.getOrDefault(accessor.getEntity().getName().getString())));
    }
}
