package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.model.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class AMc {
    @Overwrite
    public User getUser(){
        return Account.getMcUserNow();
    }

}
