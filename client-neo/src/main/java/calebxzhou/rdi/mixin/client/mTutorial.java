package calebxzhou.rdi.mixin.client;

import calebxzhou.rdi.tutorial.FindLooseRocksTutorialStep2;
import net.minecraft.client.tutorial.MovementTutorialStepInstance;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.tutorial.TutorialSteps;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-08-15 11:56
 */
@Mixin(MovementTutorialStepInstance.class)
public class mTutorial {
    @Shadow @Final private Tutorial tutorial;

    @Redirect(method = "tick",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;setStep(Lnet/minecraft/client/tutorial/TutorialSteps;)V",ordinal = 0))
    private void noFindTree(Tutorial instance, TutorialSteps pStep){
        ((ATutorial)tutorial).setInstance(new FindLooseRocksTutorialStep2(instance));
    }
    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100))
    private int displayToastNoDelay(int constant){
        return 1;
    }
}
