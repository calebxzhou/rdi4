package calebxzhou.rdi.mixin.gameplay;

import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * calebxzhou @ 2024-06-21 21:49
 */
@Mixin(BeehiveDecorator.class)
public class mTreeGrow {
    @Mutable
    @Shadow @Final private float probability;

    //长树有一半概率出蜂箱
    @Inject(method = "<init>",at=@At("TAIL"))
    private void beehive(float probbility, CallbackInfo ci){
        probability = 0.5f;
    }
}
