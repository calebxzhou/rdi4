package calebxzhou.rdi.mixin;

import calebxzhou.rdi.Const;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.spongepowered.asm.mixin.*;

/**
 * calebxzhou @ 2024-05-26 10:18
 */
@Mixin(MinecraftServer.class)
public abstract class mServerProps  {
    @Overwrite
    public int getPort() {
        return Const.SERVER_PORT;
    }
    @Overwrite
    public boolean isFlightAllowed() {
        return true;
    }
    @Overwrite
    public boolean usesAuthentication() {
        return false;
    }

}
@Mixin(DedicatedServerProperties.class)
class mServerProps2  {
    @Mutable @Shadow @Final
    public final String levelName = Const.LEVEL_NAME;
    @Mutable @Shadow @Final
    public final int serverPort = Const.SERVER_PORT;
}

