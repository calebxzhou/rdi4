package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-06-21 16:47
 */
@Mixin(ItemEntity.class)
public class mItemEntity {
    //一分钟掉落物消失
    @Shadow @Final @Mutable
    private static final int LIFETIME = 20*60;

}
