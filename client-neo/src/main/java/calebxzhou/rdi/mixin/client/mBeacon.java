package calebxzhou.rdi.mixin.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

/**
 * calebxzhou @ 2024-08-16 21:58
 */
@Mixin(BeaconBlockEntity.class)
public class mBeacon {
    //信标永远都有效
    @ModifyConstant(method = "updateBase",constant = @Constant(intValue = 0))
    private static int beaconAlwaysHasBase(int constant){
        return 1;
    }
    @Overwrite
    public List<BeaconBlockEntity.BeaconBeamSection> getBeamSections() {
        return ImmutableList.of( new BeaconBlockEntity.BeaconBeamSection(new float[]{1.0f,1.0f,1.0f}));
    }
}
@Mixin(BeaconRenderer.class)
class mBeaconSection{
    @ModifyConstant(method = "render(Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",constant = @Constant(intValue = 0,ordinal = 0))
    private int yoffset(int constant){
        return -300;
    }
}