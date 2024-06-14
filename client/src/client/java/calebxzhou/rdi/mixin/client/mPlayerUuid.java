package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.util.Utils;
import net.minecraft.core.UUIDUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * calebxzhou @ 2024-06-13 15:41
 */
@Mixin(UUIDUtil.class)
public class mPlayerUuid {
    @Overwrite
    public static UUID createOfflinePlayerUUID(String username) {
        return Utils.createUuid(username);
    }
}
