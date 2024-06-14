package calebxzhou.rdi.mixin.client;

import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(User.class)
public interface AMcUser {
    @Accessor
    void setName(String name);

    @Accessor
    void setUuid(String uuid);
}
