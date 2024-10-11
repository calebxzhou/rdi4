package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.common.blockentities.PitKilnBlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-09-09 18:43
 */
@Mixin(PitKilnBlockEntity.class)
public class mPitKiln {
    @Redirect(method = "cookContents",remap = false, at= @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeConfigSpec$IntValue;get()Ljava/lang/Object;"))
    private Object fastklin(ForgeConfigSpec.IntValue instance){

        return 20;
    }
}
