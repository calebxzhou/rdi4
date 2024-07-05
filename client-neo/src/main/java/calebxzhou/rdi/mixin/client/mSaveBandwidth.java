package calebxzhou.rdi.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * calebxzhou @ 2024-06-25 8:46
 */
@Mixin(ClientboundLevelChunkWithLightPacket.class)
public class mSaveBandwidth {
     //不接受亮度数据
   /* @Redirect(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",at= @At(value = "NEW", target = "net/minecraft/network/protocol/game/ClientboundLightUpdatePacketData" ) )
    private ClientboundLightUpdatePacketData RDI$noReceiveLightData(FriendlyByteBuf n, int x, int z){
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeBitSet(new BitSet());
        buffer.writeBitSet(new BitSet());
        buffer.writeBitSet(new BitSet());
        buffer.writeBitSet(new BitSet());
        buffer.writeCollection(new ArrayList<>(),FriendlyByteBuf::writeByteArray);
        buffer.writeCollection(new ArrayList<>(),FriendlyByteBuf::writeByteArray);
        return new ClientboundLightUpdatePacketData(buffer,x,z);
    }*/

}
@Mixin(ClientPacketListener.class)
abstract
class mSaveBandwidth2{
    @Shadow private ClientLevel level;

    @Shadow protected abstract void enableChunkLight(LevelChunk chunk, int x, int z);

    //不处理亮度数据
    /*@Inject(method = "handleLevelChunkWithLight",at= @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundLevelChunkWithLightPacket;getLightData()Lnet/minecraft/network/protocol/game/ClientboundLightUpdatePacketData;"),cancellable = true)
    private void RDI$noProcessLightData(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci){
        int i = packet.getX();
        int j = packet.getZ();
        LevelChunk levelChunk = level.getChunkSource().getChunk(i, j, false);
        if (levelChunk != null) {
            enableChunkLight(levelChunk, i, j);
        }
        ci.cancel();
    }*/
}