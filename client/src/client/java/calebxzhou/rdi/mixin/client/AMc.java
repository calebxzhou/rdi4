package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.model.RAccount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public class AMc {
    @Overwrite
    public User getUser(){
        return RAccount.getMcUserNow();
    }

}
