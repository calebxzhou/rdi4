package calebxzhou.rdi.mixin.aether;

import com.aetherteam.aether.block.portal.AetherPortalShape;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2025-01-14 14:40
 */
@Mixin(AetherPortalShape.class)
public class mAetherPortalShape {
    @Redirect(  method = "isEmpty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"))
    private static boolean RDI$AetherPortalBlueIceOK(BlockState instance) {
        return instance.isAir() || instance.is(Blocks.BLUE_ICE);
    }
}
