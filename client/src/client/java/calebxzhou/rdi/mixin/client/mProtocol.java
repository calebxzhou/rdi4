package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.net.RdiLoginC2SPacket;
import calebxzhou.rdi.ui.LoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * calebxzhou @ 2024-05-31 20:09
 */
public class mProtocol {
}

@Mixin(ConnectionProtocol.PacketSet.class)
abstract class mLoginProtocol0 {
    @Inject(method = "getId",at=@At(value = "HEAD"), cancellable = true)
    private void getId(Class<?> packetClass, CallbackInfoReturnable<Integer> cir){

        if(packetClass == RdiLoginC2SPacket.class){
            cir.setReturnValue(0xFE);
        }

    }
    @Inject(method = "createPacket",at=@At("HEAD"),cancellable = true)
    private void createPacket(int packetId, FriendlyByteBuf buffer, CallbackInfoReturnable<Packet<?>> cir){

        if(packetId == 0xFE){
            cir.setReturnValue(new RdiLoginC2SPacket(buffer));
        }

    }

}

@Mixin(ConnectionProtocol.class)
abstract class mLoginProtocol1 {
    @Inject(method = "getProtocolForPacket",at=@At("HEAD"),cancellable = true)
    private static void getProtocolForPacket(Packet<?> packet, CallbackInfoReturnable<ConnectionProtocol> cir) {

        if(packet.getClass() == RdiLoginC2SPacket.class){
            cir.setReturnValue(ConnectionProtocol.LOGIN);
        }

    }
}
