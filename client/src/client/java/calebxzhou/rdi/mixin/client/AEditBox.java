package calebxzhou.rdi.mixin.client;


import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiFunction;

@Mixin(EditBox.class)
public interface AEditBox {
    @Accessor
    int getCursorPos();
    @Accessor
    int getDisplayPos();
    @Accessor
    int getHighlightPos();
    @Accessor
    int getFrame();
    @Accessor
    Font getFont();
    @Accessor
    Component getHint();
    @Accessor
    BiFunction<String, Integer, FormattedCharSequence> getFormatter();
    @Invoker
    void invokeRenderHighlight(GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY);
    @Invoker
    int invokeGetMaxLength();
}
