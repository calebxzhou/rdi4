package calebxzhou.rdi.mixin.client;

import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.tutorial.TutorialStepInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tutorial.class)
public interface ATutorial {
    @Accessor
    void setInstance(TutorialStepInstance instance);
}
