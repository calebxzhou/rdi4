package calebxzhou.rdi.mixin.client.jech;

import calebxzhou.rdi.Const;
import me.towdium.jecharacters.config.JechConfigForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-11-01 10:57
 */
@Mixin(JechConfigForge.class)
public class mDevPreventConfig {
    @Inject(method = "reload",at=@At("HEAD"),cancellable = true,remap = false)
    private static void RDI$preventReadConfigOnDev(CallbackInfo ci){
        //开发环境不允许jech读取配置文件 否则跑不起来
        if(Const.getDEBUG()){
            ci.cancel();
        }
    }

}
