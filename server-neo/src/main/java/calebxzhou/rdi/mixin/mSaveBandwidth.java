package calebxzhou.rdi.mixin;

import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-06-23 22:07
 */

@Mixin(ChunkMap.class)
public class mSaveBandwidth {
    @Unique
    int tickAmount = 0;
    //3秒发一次掉落物的
    @Unique
    int sendTickAmount = 60;

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;sendChanges()V"))
    private void thottle(ServerEntity instance) {
        if(((AServerEntity)instance).getEntity() instanceof ItemEntity){

            if (tickAmount >= sendTickAmount) {

                instance.sendChanges();
                tickAmount = 0;
            } else {
                tickAmount++;
            }
        }else{
            instance.sendChanges();
        }
    }
}
@Mixin(ClientboundLightUpdatePacketData.class)
class mNoSendLightData{
    //不发送亮度数据给客户端 自己算
    /*@Overwrite
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBitSet(new BitSet());
        buffer.writeBitSet(new BitSet());
        buffer.writeBitSet(new BitSet());
        buffer.writeBitSet(new BitSet());
        buffer.writeCollection(new ArrayList<>(), FriendlyByteBuf::writeByteArray);
        buffer.writeCollection(new ArrayList<>(), FriendlyByteBuf::writeByteArray);
    }*/
}