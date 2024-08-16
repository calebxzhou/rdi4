package calebxzhou.rdi.tutorial

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.mixin.client.ATutorial
import calebxzhou.rdi.util.*
import net.dries007.tfc.common.TFCTags
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.toasts.TutorialToast
import net.minecraft.client.tutorial.Tutorial
import net.minecraft.client.tutorial.TutorialStepInstance
import net.minecraft.world.item.ItemStack

//找碎石头
class FindLooseRocksTutorialStep2(val tutorial: Tutorial) : TutorialStepInstance {
    //tfc:loose_rocks

    private var toast: TutorialToast? = null


    override fun tick() {
        if (toast == null) {
            Banner.textNow = mcText("在地上找两个同类的石子,按右键捡起")
            TutorialToast(
                TutorialToast.Icons.TREE,
                mcText("寻找石子"),
                mcText("在地上找两个同类的石子,按右键捡起"),
                false
            ).let {
                toast = it
                mc.addToast(it)
            }
        }
    }

    override fun onGetItem(pStack: ItemStack) {
        if (pStack.`is`(TFCTags.Items.ROCK_KNAPPING) && pStack.count >= 2) {
            mc.addChatMessage(mcText("恭喜你成功找到石子"))
            toast?.hide()
            (tutorial as ATutorial).setInstance(KnapRockStep3(tutorial))
        }
    }
}