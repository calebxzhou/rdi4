package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.util.LocalStorage;
import net.fabricmc.fabric.impl.networking.client.ClientNetworkingImpl;
import net.minecraft.client.User;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.UUID;

/**
 * calebxzhou @ 2024-05-31 20:09
 */
public class mProtocol {
}
//把密码插入到登录包的name里
@Mixin(targets = {"net.minecraft.client.gui.screens.ConnectScreen$1"})
abstract class mLoginProtocol0 {
    @Redirect(method = "run",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/User;getName()Ljava/lang/String;"))
    private String addPwdToName(User instance){
        return instance.getName()+ "@"+LocalStorage.INSTANCE.get("pwd");
    }
}

@Mixin(ServerboundHelloPacket.class)
abstract class mLoginProtocol1 {
    @ModifyConstant(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",constant = @Constant(intValue = 16))
    private static int changeNameLen(int constant){
        //给密码留出空间
        return 64;
    }
    @ModifyConstant(method = "write",constant = @Constant(intValue = 16))
    private int changeNameLen2(int constant){
        //给密码留出空间
        return 64;
    }

    //写UUID
    @Redirect(method = "write",at= @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeOptional(Ljava/util/Optional;Lnet/minecraft/network/FriendlyByteBuf$Writer;)V"))
    private void writeUUID(FriendlyByteBuf instance, Optional<Object> optional, FriendlyByteBuf.Writer<Object> writer){
        instance.writeBoolean(true);
        instance.writeUUID(UUID.fromString(LocalStorage.INSTANCE.get("uuid")));
    }
}

@Mixin(ClientNetworkingImpl.class)
abstract class mFabricNet1 {
    /*@Inject(method = "getLoginConnection", at = @At("RETURN"), cancellable = true)
    private static void returnConn(CallbackInfoReturnable<Connection> cir) {
        if (cir.getReturnValue() == null) {
            if (Minecraft.getInstance().screen instanceof LoginScreen login)
                cir.setReturnValue(login.getConnection());
        }
    }*/
}