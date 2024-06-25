package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.service.NetMetrics;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

/**
 * calebxzhou @ 2024-06-25 8:17
 */
@Mixin(PacketEncoder.class)
public class mNetMetrics {
    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;Lio/netty/buffer/ByteBuf;)V",
    at= @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;write(Lnet/minecraft/network/FriendlyByteBuf;)V",shift = At.Shift.AFTER
    ),locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void RDI_onPacketEncode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci, ConnectionProtocol connectionProtocol, int i, FriendlyByteBuf friendlyByteBuf, int j){
        NetMetrics.onPacketEncode(i,packet, byteBuf);
    }
}
@Mixin(PacketDecoder.class)
 class mNetMetrics2 {
    @Inject(method = "decode",
            at= @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            ),locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void RDI_onPacketEncode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list, CallbackInfo ci, int i, FriendlyByteBuf friendlyByteBuf, int j, Packet packet){
        NetMetrics.onPacketDecode(j,packet, byteBuf);
    }
}