package calebxzhou.rdi.mixin.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * calebxzhou @ 2024-06-20 22:49
 */
@Mixin(Gui.class)
public class mGui {
    @ModifyConstant(method = "renderHearts",constant = @Constant(intValue = 8))
    private int heartW(int constant){
        return 2;
    }
    @ModifyConstant(method = "renderHearts",constant = @Constant(intValue = 10))
    private int heartHeight(int constant){
        return 50;
    }
}
