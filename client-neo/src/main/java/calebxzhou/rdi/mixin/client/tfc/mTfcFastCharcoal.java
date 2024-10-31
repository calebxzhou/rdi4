package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.common.blockentities.BurningLogPileBlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-10-31 10:53
 */
@Mixin(BurningLogPileBlockEntity.class)
public class mTfcFastCharcoal {
    //快速制作木炭
    @Redirect(remap = false, method = "serverTick",at= @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeConfigSpec$IntValue;get()Ljava/lang/Object;"))
    private static Object fastcharcoal(ForgeConfigSpec.IntValue instance){

        return 50;
    }
}
