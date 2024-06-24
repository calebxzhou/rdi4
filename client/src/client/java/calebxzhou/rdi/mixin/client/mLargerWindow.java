package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.util.McUtils;
import com.mojang.blaze3d.platform.*;
import kotlin.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

/**
 * calebxzhou @ 2024-05-23 12:12
 */
@Mixin(Window.class)
public class mLargerWindow {

    @Shadow
    private int windowedWidth;

    @Shadow
    private int windowedHeight;

    @Shadow
    private int width;

    @Shadow
    private int height;

    /**
     * 根据屏幕分辨率，扩大窗口尺寸
     */
    @Inject(method = "<init>",locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V"))
    private void max(WindowEventHandler eventHandler, ScreenManager screenManager, DisplayData displayData, String preferredFullscreenVideoMode, String title, CallbackInfo ci, Optional optional, Monitor monitor){
        VideoMode mode = monitor.getCurrentMode();
        Pair<Integer, Integer> wh = McUtils.getWindowSize(mode.getWidth(), mode.getHeight());
        windowedWidth=width=wh.getFirst();
        windowedHeight=height=wh.getSecond();
    }
}
