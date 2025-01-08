package calebxzhou.rdi.mixin.client;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.Options.genericValueLabel;

/**
 * calebxzhou @ 1/8/2025 18:11
 */
@Mixin(Options.class)
public class mOptions {
    @Shadow @Final @Mutable
    private   OptionInstance<Integer> fov = new OptionInstance<>(
            "options.fov",
            OptionInstance.noTooltip(),
            Options::genericValueLabel,
            new OptionInstance.IntRange(1, 175), Codec.DOUBLE.xmap((p_232007_) -> {
        return (int)(p_232007_ * 40.0D + 70.0D);
    }, (p_232009_) -> {
        return ((double) p_232009_ - 70.0D) / 40.0D;
    }), 70, (p_231951_) -> {
        Minecraft.getInstance().levelRenderer.needsUpdate();
    });
}
