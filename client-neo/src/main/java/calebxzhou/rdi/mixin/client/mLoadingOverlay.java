package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.ui.RLoadingOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.jetbrains.annotations.Nullable;
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
    @Shadow public abstract void setOverlay(@Nullable Overlay loadingGui);

    /*@Shadow public abstract void setOverlay(@Nullable Overlay loadingGui);

        @Shadow @Final private ReloadableResourceManager resourceManager;

        @Shadow @Final private static CompletableFuture<Unit> RESOURCE_RELOAD_INITIAL_TASK;

        @Shadow @Final private PackRepository resourcePackRepository;

        @Redirect(method = "<init>",at= @At(value = "NEW", target = "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/server/packs/resources/ReloadInstance;Ljava/util/function/Consumer;Z)Lnet/minecraft/client/gui/screens/LoadingOverlay;"))
        private LoadingOverlay goRdiLoadingOverlay(Minecraft minecraft, ReloadInstance reload, Consumer onFinish, boolean fadeIn){
            return new RLoadingOverlay(minecraft,reload,onFinish);
        }
        @Redirect(method = "reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"))
        private void goRdiLoadingOverlay2(Minecraft instance, Overlay loadingGui){
            setOverlay(new RLoadingOverlay(instance,resourceManager.createReload(
                    Util.backgroundExecutor(),
                    instance,
                    RESOURCE_RELOAD_INITIAL_TASK, resourcePackRepository.openAllSelected()),optional -> Util.ifElse(optional, throwable -> {
                if (err) {
                    this.abortResourcePackRecovery();
                } else {
                    this.rollbackResourcePacks(throwable);
                }
            }, () -> {
                this.levelRenderer.allChanged();
                this.reloadStateTracker.finishReload();
                completableFuture.complete(null);
            }),));
        }*/
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
