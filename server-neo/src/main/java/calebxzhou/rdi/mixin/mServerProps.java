package calebxzhou.rdi.mixin;

import calebxzhou.rdi.Const;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.levelgen.WorldOptions;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalLong;

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
    public final boolean enableRcon = true;
    @Mutable @Shadow @Final
    public final int rconPort=Const.RCON_PORT;
    @Mutable @Shadow @Final
    public final String rconPassword = "1";
    @Mutable @Shadow @Final
    public final int serverPort = Const.SERVER_PORT;
    @Redirect(method = "<init>",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/WorldOptions;parseSeed(Ljava/lang/String;)Ljava/util/OptionalLong;"))
    private OptionalLong rdiSeed(String numberformatexception){

        return WorldOptions.parseSeed(String.valueOf(Const.SEED));
    }
}

