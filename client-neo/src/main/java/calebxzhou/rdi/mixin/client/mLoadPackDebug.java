package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.Const;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-11-03 19:53
 */
@Mixin(ReloadableResourceManager.class)
public class mLoadPackDebug {
    @Redirect(method = "createReload",at= @At(value = "INVOKE", target = "Lorg/slf4j/Logger;isDebugEnabled()Z"))
    private boolean R$dev(Logger instance){
        return Const.getDEBUG();
    }
}
