package calebxzhou.rdi.mixin.client.railway;

import com.railwayteam.railways.Railways;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-10-30 13:41
 */
@Mixin(Railways.class)
public class mRailNoDevCheck {
    //开发环境跑不起来
    @Redirect(remap = false, method = "init",at= @At(value = "INVOKE", target = "Lcom/railwayteam/railways/util/Utils;isDevEnv()Z"))
    private static boolean noDEV(){
        return false;
    }
}
