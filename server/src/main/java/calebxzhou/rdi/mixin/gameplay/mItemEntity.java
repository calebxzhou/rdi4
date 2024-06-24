package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-06-21 16:47
 */
@Mixin(ItemEntity.class)
public class mItemEntity {
    @Shadow private int pickupDelay;

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 6000))
    private int oneMinDispose(int constant){
        //1分钟掉落物自动消失
        return 20*60;
    }
    //捡起0延迟
    /*@Overwrite
    public void setDefaultPickUpDelay() {
        pickupDelay = 0;
    }*/
}
