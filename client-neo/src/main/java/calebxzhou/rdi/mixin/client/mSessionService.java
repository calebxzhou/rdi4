package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.service.RAuthService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Proxy;

/**
 * calebxzhou @ 2024-05-23 12:08
 */
@Mixin(Minecraft.class)
public class mSessionService {
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService"), remap = false)
    private YggdrasilAuthenticationService a(Proxy proxy) {
        return new RAuthService();
    }

    @Overwrite
    private UserApiService createUserApiService(YggdrasilAuthenticationService authenticationService, GameConfig gameConfig) {
        return UserApiService.OFFLINE;
    }
}