package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RIconButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import me.towdium.pinin.PinIn
import me.towdium.pinin.searchers.Searcher
import me.towdium.pinin.searchers.TreeSearcher
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.world.level.block.Block
import org.anti_ad.mc.common.vanilla.alias.ForgeRegistries

class BlockSelectScreen(val prevScreen: Screen) : RScreen("选择方块") {
    val pinyinSearcher = TreeSearcher<Int>(Searcher.Logic.CONTAIN, PinIn())
    val allBlocks = ForgeRegistries.BLOCKS.mapIndexed { i, block ->
        val cn = block.asItem().chineseName.string
        pinyinSearcher.put(cn, i)
        cn to block
    }
    var selectedBlocks = arrayListOf<Block>()
    var resultBlockButtons = arrayListOf<RIconButton>()
    val searchBox = REditBox("中文名/拼音", 2, 20, 100, length = 48).also {
        it.justify()
        registerWidget(it)
    }


    override fun tick() {
        if (mc pressingKey InputConstants.KEY_F5) {
            mc go BlockSelectScreen(prevScreen)
            return
        }
        if (mc.pressingEnter) {
            resultBlockButtons.clear()
            val txt = searchBox.value
            val searchedIndexes = pinyinSearcher.search(txt).take(25)
            var x = 2
            var y = 90
            searchedIndexes.map { allBlocks[it].second }.forEachIndexed { i, b ->
                val item = b.asItem()
                if (x + mcFont.width(item.chineseName)+16 > mcUIWidth) {
                    x = 2
                    y += 20
                }
                val btn = RIconButton(item = item, comp = item.chineseName, x = x, y = y) {}
                resultBlockButtons += btn
                x+=btn.width+5

            }
        }
        super.tick()
    }

    override fun init() {
        title.append(mcText("(共" + allBlocks.size.toString() + "个)"))
        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.drawString(2, 40, "已选择：")
        guiGraphics.drawString(2, 70, "已搜到：（前25个）")
        resultBlockButtons.forEach { it.render(guiGraphics, mouseX, mouseY, partialTick) }
    }
}