package calebxzhou.rdi.mixin.client.railway;

import com.railwayteam.railways.util.DevCapeUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * calebxzhou @ 2024-11-01 21:33
 */
@Mixin(DevCapeUtils.class)
public class mRailNoDevCape {
    @Overwrite(remap = false)
    public void refresh() {}
}
