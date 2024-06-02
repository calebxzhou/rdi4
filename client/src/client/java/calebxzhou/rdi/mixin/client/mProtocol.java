package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.net.LoginC2SPacket;
import calebxzhou.rdi.net.RegisterC2SPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * calebxzhou @ 2024-05-31 20:09
 */
public class mProtocol {
}

@Mixin(ConnectionProtocol.PacketSet.class)
abstract class mLoginProtocol0 {
    @Inject(method = "getId",at=@At(value = "HEAD"), cancellable = true)
    private void getPacketIdC2S(Class<?> packetClass, CallbackInfoReturnable<Integer> cir){

        if(packetClass == LoginC2SPacket.class){
            cir.setReturnValue(0xFE);
        }
        if(packetClass == RegisterC2SPacket.class){
            cir.setReturnValue(0xFD);
        }
    }
    @Inject(method = "createPacket",at=@At("HEAD"),cancellable = true)
    private void createPacketS2C(int packetId, FriendlyByteBuf buffer, CallbackInfoReturnable<Packet<?>> cir){

        /*if(packetId == 0xFE){
            cir.setReturnValue(new LoginC2SPacket(buffer));
        }*/

    }

}

@Mixin(ConnectionProtocol.class)
abstract class mLoginProtocol1 {
    @Inject(method = "getProtocolForPacket",at=@At("HEAD"),cancellable = true)
    private static void getProtocolForPacket(Packet<?> packet, CallbackInfoReturnable<ConnectionProtocol> cir) {

        if(packet.getClass() == LoginC2SPacket.class){
            cir.setReturnValue(ConnectionProtocol.LOGIN);
        }
        if(packet.getClass() == RegisterC2SPacket.class){
            cir.setReturnValue(ConnectionProtocol.STATUS);
        }
    }
}
