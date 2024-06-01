package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-05-26 10:59
 */
@Mixin(Phantom.class)
public class mPhantom {
    @ModifyConstant(
            method = "updatePhantomSizeInfo()V"
            ,constant = @Constant(intValue = 6)
    )
    private int changeDamange(int constant){
        return 15;
    }
}
