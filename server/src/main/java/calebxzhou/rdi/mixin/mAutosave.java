package calebxzhou.rdi.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-06-15 13:48
 */
@Mixin(MinecraftServer.class)
public class mAutosave {
    @ModifyConstant(method = "tickServer",constant = @Constant(intValue = 6000))
    private int saveTick(int a){
        return 20*30;
    }
}
