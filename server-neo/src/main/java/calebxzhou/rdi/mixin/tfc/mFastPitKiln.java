package calebxzhou.rdi.mixin.tfc;

import net.dries007.tfc.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-09-09 18:43
 */
@Mixin(ServerConfig.class)
public class mFastPitKiln {
    //烧陶器30s
    @ModifyConstant(method = "<init>",constant=@Constant(intValue = 8000))
    private int pitKiln30sec(int constant){
        return 600;
    }


}
