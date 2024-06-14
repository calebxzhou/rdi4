package calebxzhou.rdi.mixin;

import calebxzhou.rdi.service.AccountService;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    @Unique
    GameProfile profile;
    ServerboundHelloPacket packet;
    @Inject(method = "handleHello",at= @At(value = "NEW", target = "(Ljava/util/UUID;Ljava/lang/String;)Lcom/mojang/authlib/GameProfile;"), cancellable = true)
    private void onPlayerLogin(ServerboundHelloPacket packet, CallbackInfo ci){
        this.packet=packet;
        var result = AccountService.INSTANCE.onLogin(packet,connection);

        if(result == null){

            ci.cancel();
        }else{
            profile = result;
        }


    }

    @Redirect(method = "handleHello",at= @At(value = "NEW", target = "(Ljava/util/UUID;Ljava/lang/String;)Lcom/mojang/authlib/GameProfile;"))
    private GameProfile createProfile(UUID id, String name){
        return profile;
    }



}

@Mixin(ConnectionProtocol.class)
abstract class mLoginProtocol1 {


}
