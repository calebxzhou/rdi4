package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.util.DialogUtils;
import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-05-23 12:08
 */
@Mixin(Window.class)
public class mAskStopClient {

    /**
     * @author calebxzhou
     * @reason 关闭客户端前询问
     */
    @Overwrite
    public boolean shouldClose() {
        return DialogUtils.shouldClose((Window) (Object)this);
    }
}