package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.ui.RLoadingOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.server.packs.resources.ReloadInstance;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * calebxzhou @ 2024-05-24 8:19
 */
@Mixin(Minecraft.class)
public abstract class mLoadingOverlay {
    @Shadow public abstract void setOverlay(Overlay loadingGui);

    @Inject(method = "setOverlay",at=@At("HEAD"), cancellable = true)
    private void goRdiLoadingOverlay(Overlay loadingGui, CallbackInfo ci){
        if(loadingGui instanceof LoadingOverlay lo){
            var o = (ALoadingOverlay) lo;
            setOverlay(new RLoadingOverlay(o.getMinecraft(),o.getReload(),o.getOnFinish()));
            ci.cancel();
        }
    }
}
@Mixin(LoadingOverlay.class)
interface ALoadingOverlay{
    @Accessor Minecraft getMinecraft();
    @Accessor ReloadInstance getReload();
    @Accessor Consumer<Optional<Throwable>> getOnFinish();

}
