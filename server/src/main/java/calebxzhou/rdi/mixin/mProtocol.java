package calebxzhou.rdi.mixin;

import calebxzhou.rdi.net.LoginSPacket;
import calebxzhou.rdi.net.RegisterSPacket;
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
    private void getIdC2S(Class<?> packetClass, CallbackInfoReturnable<Integer> cir){
/*
        if(packetClass == RdiLoginC2SPacket.class){
            cir.setReturnValue(0xFE);
        }*/

    }
    @Inject(method = "createPacket",at=@At("HEAD"),cancellable = true)
    private void createPacketS2C(int packetId, FriendlyByteBuf buffer, CallbackInfoReturnable<Packet<?>> cir){

        if(packetId == 0xFE){
            cir.setReturnValue(new LoginSPacket(buffer));
        }
        if(packetId == 0xFD){
            cir.setReturnValue(new RegisterSPacket(buffer));
        }
    }

}

@Mixin(ConnectionProtocol.class)
abstract class mLoginProtocol1 {
    @Inject(method = "getProtocolForPacket",at=@At("HEAD"),cancellable = true)
    private static void getProtocolForPacket(Packet<?> packet, CallbackInfoReturnable<ConnectionProtocol> cir) {

        if(packet.getClass() == LoginSPacket.class){
            cir.setReturnValue(ConnectionProtocol.LOGIN);
        }
        if(packet.getClass() == RegisterSPacket.class){
            cir.setReturnValue(ConnectionProtocol.LOGIN);
        }
    }
}
