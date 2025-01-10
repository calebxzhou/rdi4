package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.model.RAccount;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
            buffer.writeUtf(account.getName(), 64);
            buffer.writeUUID(account.getUuid());
        }else{
            throw new IllegalStateException("账号为null,不允许登录");
        }
    }


}
