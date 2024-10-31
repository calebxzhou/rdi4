package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-10-29 23:21
 */
@Mixin(DryingBricksBlock.class)
public class mTfcDryingBricks {
    @Redirect(remap = false, method = "lambda$tick$0",at= @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeConfigSpec$IntValue;get()Ljava/lang/Object;"))
    private static Object RDI$fastDryBricks(ForgeConfigSpec.IntValue instance){
        return 50;
    }
}
@Mixin(BlockEntityTooltips.class)
class mDryingBricksTooltip{
    @Redirect(remap = false, method = "lambda$static$26",at= @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeConfigSpec$IntValue;get()Ljava/lang/Object;"))
    private static Object RDI$fastDryBricks(ForgeConfigSpec.IntValue instance){
        return 50;
    }
}
