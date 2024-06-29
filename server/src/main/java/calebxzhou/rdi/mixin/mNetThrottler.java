package calebxzhou.rdi.mixin;

import calebxzhou.rdi.service.NetThrottler;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-06-27 19:28
 */

@Mixin(Connection.class)
class mNetThrottler {
    @Inject(method = "doSendPacket",at=@At("HEAD"),cancellable = true)
    private void RDI$onSendPacket(Packet<?> packet, @Nullable PacketSendListener sendListener, ConnectionProtocol newProtocol, ConnectionProtocol currentProtocol, CallbackInfo ci){
        if(!NetThrottler.allowSendPacket(packet)){
            ci.cancel();
        }
    }
}