package calebxzhou.rdi.mixin.client.wthit;

import calebxzhou.rdi.RDI;
import mcp.mobius.waila.util.ModInfo;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * calebxzhou @ 2024-06-29 23:33
 */
@Mixin(ModInfo.class)
public class mModInfo {
    @Mutable
    @Shadow @Final private String name;

    @Shadow @Final private String id;

    @Overwrite(remap = false)
    public String getName() {
        return RDI.loadedModsIdName.getOrDefault(id,name);
    }
}
