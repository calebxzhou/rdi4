package calebxzhou.rdi.mixin.client.gameplay;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-05-26 9:49
 */
@Mixin(FireBlock.class)
public class mFire {
    /**
     * @author calebxzhou
     * @reason 1
     */
    @Overwrite
    private static int getFireTickDelay(RandomSource randomSource){
        return randomSource.nextInt(20);
    }
}
