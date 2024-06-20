package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.RDI;
import calebxzhou.rdi.model.RAccount;
import calebxzhou.rdi.util.LocalStorage;
import net.fabricmc.fabric.impl.networking.client.ClientNetworkingImpl;
import net.minecraft.client.User;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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

@Mixin(ServerboundHelloPacket.class)
abstract class mLoginProtocol1 {

    @Overwrite
    public void write(FriendlyByteBuf buffer) {
        var account = RAccount.getNow();
        if (account != null) {
            buffer.writeUtf(account.getName()+"\n"+account.getPwd(), 64);
            buffer.writeUUID(account.getUuid());
        }else{
            RDI.log().warn("account is null");
            buffer.writeUtf("", 64);
            buffer.writeUUID(UUID.randomUUID());
        }
    }


}
