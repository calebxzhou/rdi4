package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RIconButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
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
    var selectedBlocks = arrayListOf<Pair<Block,RIconButton>>()
    var resultBlockButtons = arrayListOf<Pair<Block,RIconButton>>()
    var prevSearchValue = ""
    val searchBox = REditBox("拼音/首拼", 2, 20, 100, length = 48).also {
        it.justify()
        registerWidget(it)
    }



    override fun tick() {

        if (prevSearchValue != searchBox.value) {
           search()
        }
        super.tick()
    }
    fun search(){
        resultBlockButtons.forEach { this -= it.second }
        resultBlockButtons.clear()
        val txt = searchBox.value
        prevSearchValue = txt
        val searchedIndexes = pinyinSearcher.search(txt).take(25)
        var x = 2
        var y = 90
        searchedIndexes.map { allBlocks[it].second }.forEachIndexed { i, block ->
            val item = block.asItem()
            if (x + mcFont.width(item.chineseName) + 16 > mcUIWidth) {
                x = 2
                y += 20
            }
            val btn = RIconButton(item = item, comp = item.chineseName + "\n" + item.id.toString(), x = x, y = y) {
                select(block)
            }

            this += btn
            resultBlockButtons += block to btn
            x += btn.width + 5

        }
    }
    fun select(block: Block){
        val item = block.asItem()
        val btn = RIconButton(item = item, comp = item.chineseName + "\n" + item.id.toString(),x=2,y=55) {
            selectedBlocks.remove(block to it)
            this -= it
        }
        val lastBtn = selectedBlocks.lastOrNull()?.second
        btn.x = (lastBtn?.x?:2)+(lastBtn?.width?:0)
        this += btn
        selectedBlocks += block to btn
    }
    override fun init() {
        title.append(mcText("(共" + allBlocks.size.toString() + "个)"))

        gridLayout(this, hAlign = HAlign.CENTER) {
            button("start", text = "开始导航") {
                mc go null
                OmniNavi.navigate { bstate -> selectedBlocks.any { bstate.`is`(it.first) } }
            }
            button("reset", text = "还原") {
                mc go BlockSelectScreen(prevScreen)
            }
        }
        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.drawString(2, 40, "已选择：")
        guiGraphics.drawString(2, 77, "已搜到："+resultBlockButtons.size+"个")
        /*selectedBlocks.forEachIndexed { i,block ->
            RIconButton(
                item = block.asItem(),
                comp = block.asItem().chineseName + "\n",
                x=2,
                y=50

            ) { selectedBlocks.remove(block) }.also { it.x=2+i*it.width }
        }*/
    }
}