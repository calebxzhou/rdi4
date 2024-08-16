package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.*
import net.dries007.tfc.common.TFCTags
import net.dries007.tfc.common.items.TFCItems
import net.dries007.tfc.util.Metal
import net.minecraft.client.gui.components.toasts.TutorialToast
import net.minecraft.client.tutorial.Tutorial
import net.minecraft.client.tutorial.TutorialStepInstance
import net.minecraft.client.tutorial.TutorialSteps
import net.minecraft.world.item.ItemStack

class KnapRockStep3(val tutorial: Tutorial) : TutorialStepInstance {
    private var toast: TutorialToast? = null
    override fun tick() {
        if (toast == null) {
            TutorialToast(
                TutorialToast.Icons.TREE,
                mcText("制作石斧头部"),
                mcText("拿2石头,对天右键,开始制作"),
                false
            ).let {
                toast = it
                mc.addToast(it)
            }
        }
    }
    override fun onGetItem(pStack: ItemStack) {
        if (pStack.toString().contains("axe_head")) {
            mc.addChatMessage(mcText("成功制作石斧头子"))
            toast?.hide()
            tutorial.goStep(FindTwigTutorialStep4(tutorial))
        }
    }
}