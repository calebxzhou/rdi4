package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.RDI;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * calebxzhou @ 2024-07-01 16:18
 */
@Mixin(MinecraftProfileTexture.class)
public class mProfileTexture {
    @Shadow @Final private String url;

    @Inject(method = "getHash",at=@At("HEAD"), cancellable = true,remap = false)
    private void RDI$getHash(CallbackInfoReturnable<String> cir){
        try {
            var hash = FilenameUtils.getBaseName(new URL(url).getPath());
            cir.setReturnValue(hash);
        } catch (MalformedURLException e) {
            RDI.log().error("错误：{}是无效的皮肤URL", url);
            cir.setReturnValue("1");
        }
    }
}
