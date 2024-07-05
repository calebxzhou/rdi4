package calebxzhou.rdi.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-06-14 17:10
 */
@Mixin(ClientPacketListener.class)
public class mChat {

    //不显示“无法验证聊天消息“
    @Redirect(method = "handleServerData",at= @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;enforcesSecureChat()Z"))
    private boolean secure(ClientboundServerDataPacket instance){
        return true;
    }
}
