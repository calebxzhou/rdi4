package calebxzhou.rdi.mixin.aether;

import calebxzhou.rdi.aether.RAetherPortal;
import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.block.portal.AetherPortalShape;
import com.aetherteam.aether.event.hooks.DimensionHooks;
import com.aetherteam.aether.world.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * calebxzhou @ 2025-01-14 14:21
 */
@Mixin(DimensionHooks.class)
public class mFireStartPortal {
    //火启动天境传送门
    @Inject(method = "detectWaterInFrame",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/world/level/material/Fluid;)Z"), cancellable = true)
    private static void RDI$fireStartPortal(LevelAccessor levelAccessor, BlockPos pos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Boolean> cir){
        var level = (Level)levelAccessor;
        RAetherPortal.startPortal(level,pos,blockState,cir);
    }
}
