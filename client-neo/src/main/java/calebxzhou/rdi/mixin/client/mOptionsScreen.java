package calebxzhou.rdi.mixin.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.ModListScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

/**
 * calebxzhou @ 2024-08-04 16:18
 */
@Mixin(OptionsScreen.class)
public abstract class mOptionsScreen {
    @Shadow
    protected abstract Button openScreenButton(Component pText, Supplier<Screen> pScreenSupplier);

    //把遥测数据替换成mod列表
    @Redirect(method = "init", at = @At(ordinal = 10, value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
    private <T extends LayoutElement> T displayModList(GridLayout.RowHelper instance, T pChild) {

        Button btn = openScreenButton(Component.literal("Mod管理"), () -> new ModListScreen((Screen) (Object) this));
        instance.addChild(btn);
        return (T) btn;
    }
}
