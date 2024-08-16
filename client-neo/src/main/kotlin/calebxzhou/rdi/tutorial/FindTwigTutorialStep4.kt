package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.*
import net.dries007.tfc.common.TFCTags
import net.minecraft.client.gui.components.toasts.TutorialToast
import net.minecraft.client.tutorial.Tutorial
import net.minecraft.client.tutorial.TutorialStepInstance
import net.minecraft.world.item.ItemStack

class FindTwigTutorialStep4 (val tutorial: Tutorial) : TutorialStepInstance {
    private var toast: TutorialToast? = null


    override fun tick() {
        if (toast == null) {
            TutorialToast(
                TutorialToast.Icons.TREE,
                mcText("寻找树枝"),
                mcText("去树底下捡5个同款树枝"),
                false
            ).let {
                toast = it
                mc.addToast(it)
            }
        }
    }
    override fun onGetItem(pStack: ItemStack) {
        if (pStack.`is`(TFCTags.Items.FIREPIT_STICKS) && pStack.count>=5) {
            toast?.hide()
            tutorial.goStep(CraftAxeStep5(tutorial))
        }
    }
}