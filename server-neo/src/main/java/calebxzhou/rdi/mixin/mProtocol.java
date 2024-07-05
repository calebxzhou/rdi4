package calebxzhou.rdi.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

/**
 * calebxzhou @ 2024-05-31 20:09
 */
public class mProtocol {
}

@Mixin(ServerLoginPacketListenerImpl.class)
abstract class mLoginProtocol0 {



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
