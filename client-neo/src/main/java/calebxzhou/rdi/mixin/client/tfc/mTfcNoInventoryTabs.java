package calebxzhou.rdi.mixin.client.tfc;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.dries007.tfc.compat.jei.JEIIntegration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-10-18 19:23
 */
//关闭背包界面的气候 营养菜单 因为esc菜单已经有了
@Mixin(JEIIntegration.class)
public class mTfcNoInventoryTabs {
    @Redirect(remap = false, method = "registerGuiHandlers",at= @At(value = "INVOKE", target = "Lmezz/jei/api/registration/IGuiHandlerRegistration;addGuiContainerHandler(Ljava/lang/Class;Lmezz/jei/api/gui/handlers/IGuiContainerHandler;)V"))
    private void noTabs(IGuiHandlerRegistration instance, Class aClass, IGuiContainerHandler tiGuiContainerHandler){

    }
}
