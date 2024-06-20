package calebxzhou.rdi.mixin;

import calebxzhou.rdi.service.AccountService;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;
import java.util.UUID;

/**
 * calebxzhou @ 2024-05-31 20:09
 */
public class mProtocol {
}

@Mixin(ServerLoginPacketListenerImpl.class)
abstract class mLoginProtocol0 {
    @Shadow @Final
    Connection connection;

    @Shadow
    ServerLoginPacketListenerImpl.State state;
    @Shadow
    @Nullable
    GameProfile gameProfile;

    @Overwrite
    public void handleHello(ServerboundHelloPacket packet) {
        var profile = AccountService.INSTANCE.onLogin(packet,connection);
        if(profile == null){
            return;
        }
        this.gameProfile = profile;
        state=ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
    }




}

@Mixin(ServerboundHelloPacket.class)
abstract class mLoginProtocol1 {

    @ModifyConstant(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",constant = @Constant(intValue = 16))
    private static int readNameWithPwd(int constant){
        return 64;
    }
    @Redirect(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readOptional(Lnet/minecraft/network/FriendlyByteBuf$Reader;)Ljava/util/Optional;"))
    private static Optional readUUID(FriendlyByteBuf instance, FriendlyByteBuf.Reader reader){
        return Optional.of(instance.readUUID());
    }

}
