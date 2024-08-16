package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.*
import net.minecraft.client.gui.components.toasts.TutorialToast
import net.minecraft.client.tutorial.Tutorial
import net.minecraft.client.tutorial.TutorialStepInstance
import net.minecraft.world.item.ItemStack

class CraftAxeStep5(val tutorial: Tutorial) : TutorialStepInstance {
    private var toast: TutorialToast? = null
    override fun tick() {
        if (toast == null) {
            TutorialToast(
                TutorialToast.Icons.TREE,
                mcText("制作石斧"),
                mcText("按E合成,斧头子上,树枝下"),
                false
            ).let {
                toast = it
                mc.addToast(it)
            }
        }
    }
    override fun onGetItem(pStack: ItemStack) {
        if (pStack.toString().contains("axe")) {
            mc.addChatMessage(mcText("成功制作石斧 现在去砍树吧"))
            toast?.hide()
            //tutorial.goStep(FindTwigTutorialStep4(tutorial))
        }
    }
}