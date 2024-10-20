package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.config.ServerConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

/**
 * calebxzhou @ 2024-09-09 18:43
 */
@Mixin(ServerConfig.class)
public class mFastPitKiln {

    @ModifyConstant(method = "<init>",constant=@Constant(intValue = 8000))
    private int pitKiln30sec(int constant){
        return 600;
    }


}
