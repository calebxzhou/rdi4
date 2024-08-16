package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.RDI;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Deque;

/**
 * calebxzhou @ 2024-08-15 9:49
 */
@Mixin(ToastComponent.class)
public class mForceAddToast {
    @Shadow @Final private Deque<Toast> queued;

   /* @Overwrite
    public void addToast(Toast pToast) {
        if (net.minecraftforge.client.ForgeHooksClient.onToastAdd(pToast)) {
            RDI.log().warn("toast取消:"+pToast.toString());
        }
        this.queued.add(pToast);
    }*/
}
