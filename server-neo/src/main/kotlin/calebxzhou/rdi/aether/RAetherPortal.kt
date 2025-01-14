package calebxzhou.rdi.aether

import com.aetherteam.aether.AetherConfig
import com.aetherteam.aether.block.portal.AetherPortalShape
import com.aetherteam.aether.world.LevelUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object RAetherPortal {
    @JvmStatic
    fun startPortal(level: Level,pos:BlockPos,blockState: BlockState,cir: CallbackInfoReturnable<Boolean>){
        if(blockState.`is`(Blocks.BLUE_ICE)){
            if ((level.dimension() == LevelUtil.returnDimension() || level.dimension() == LevelUtil.destinationDimension())) {
                val optional = AetherPortalShape.findEmptyAetherPortalShape(level, pos, Direction.Axis.X);
                if (optional.isPresent) {
                    optional.get().createPortalBlocks();
                    cir.returnValue = true;
                }
            }
        }

    }
}