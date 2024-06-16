package calebxzhou.rdi.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-06-15 16:30
 */
@Mixin(BaseFireBlock.class)
public class mNetherPortal {

    //到处都能创建地狱门
    @Overwrite
    private static boolean inPortalDimension(Level level) {
        return true;
    }
}

