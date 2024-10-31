package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.common.blocks.devices.LogPileBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * calebxzhou @ 2024-10-31 16:33
 */
@Mixin(LogPileBlock.class)
public class mTfcLogPileBlock {
    //手持起火器打不开木堆画面
    @Inject(remap = false,
            method = "use",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntityType;)Ljava/util/Optional;"),locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void RDI$handingFireStarterNoOpenScreen(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir, ItemStack stack){
        if (stack.is(TFCItems.FIRESTARTER.get())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
