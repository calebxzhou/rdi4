package calebxzhou.rdi.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * calebxzhou @ 2025-02-21 0:32
 */
@Mixin(ServerLoginPacketListenerImpl.class)
public class mServerLoginPacketListener {
    @Shadow @Nullable @Mutable public GameProfile gameProfile;

    //允许用户名中文
    @Overwrite
    public static boolean isValidUsername(String pUsername) {
        return pUsername.chars().filter((p_203791_) -> (p_203791_ <= 32 || p_203791_ >= 127) && (p_203791_ < 0x4E00 || p_203791_ > 0x9FFF)).findAny().isEmpty();
    }

    //注入uuid
    @Inject(method = "handleHello",at= @At(value = "TAIL"))
    private void injectUUID(ServerboundHelloPacket pPacket, CallbackInfo ci){
        var uuid = pPacket.profileId();
        if(uuid.isEmpty()){
            throw new IllegalStateException("无效uuid，请升级rdi核心");
        }
        gameProfile = new GameProfile(pPacket.profileId().get(),pPacket.name());
    }
}
