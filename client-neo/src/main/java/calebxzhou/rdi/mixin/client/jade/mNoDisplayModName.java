package calebxzhou.rdi.mixin.client.jade;

import calebxzhou.rdi.lang.EnglishStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.addon.core.CorePlugin;
import snownee.jade.addon.core.ModNameProvider;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

/**
 * calebxzhou @ 2024-10-16 21:49
 */
//jade不显示mod名称
@Mixin(CorePlugin.class)
public class mNoDisplayModName {
    @Redirect(method = "registerClient",at= @At( value = "INVOKE", target = "Lsnownee/jade/api/IWailaClientRegistration;registerBlockComponent(Lsnownee/jade/api/IBlockComponentProvider;Ljava/lang/Class;)V"))
    private void no1(IWailaClientRegistration registration, IBlockComponentProvider prov, Class<? extends Block> aClass){
        if(prov instanceof ModNameProvider){
            return;
        }else{
            registration.registerBlockComponent(prov,aClass);
        }
    }
    @Redirect(method = "registerClient",at= @At( value = "INVOKE", target = "Lsnownee/jade/api/IWailaClientRegistration;registerEntityComponent(Lsnownee/jade/api/IEntityComponentProvider;Ljava/lang/Class;)V" ))
    private void no2(IWailaClientRegistration registration, IEntityComponentProvider prov, Class<? extends Entity> aClass){
        if(prov instanceof ModNameProvider){
            return;
        }else{
            registration.registerEntityComponent(prov,aClass);
        }
    }
}
