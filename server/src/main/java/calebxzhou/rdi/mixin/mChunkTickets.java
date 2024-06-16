package calebxzhou.rdi.mixin;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-06-05 11:24
 */

public class mChunkTickets {
}
@Mixin(ServerLevel.class)
//出生点减少常加载
class mSpawnLoad {
    /*@ModifyConstant(method = "setDefaultSpawnPos",constant = @Constant(intValue = 11))
    private int lessSpawnLoad(int constant){
        return 4;
    }*/

}
@Mixin(PortalForcer.class)
//地狱门减少加载
class mPortalLoad{
    /*@Redirect(method = "method_30479",at= @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;addRegionTicket(Lnet/minecraft/server/level/TicketType;Lnet/minecraft/world/level/ChunkPos;ILjava/lang/Object;)V"))
    private void lessPortalLoad(ServerChunkCache instance, TicketType type, ChunkPos pos, int distance, Object value){
        instance.addRegionTicket(type, pos, 1, value);
    }*/

}