package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.ui.component.REditBox
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import me.towdium.pinin.PinIn
import me.towdium.pinin.searchers.Searcher
import me.towdium.pinin.searchers.TreeSearcher
import net.dries007.tfc.common.blocks.TFCBlocks
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.world.level.block.Block
import org.anti_ad.mc.common.vanilla.alias.ForgeRegistries

class BlockSelectScreen(val prevScreen: Screen) : RScreen("选择方块") {
    val pinyinSearcher = TreeSearcher<Int>(Searcher.Logic.CONTAIN,PinIn())
    val allBlocks = ForgeRegistries.BLOCKS.mapIndexed { i,block ->
        val cn = block.asItem().chineseName.string
        pinyinSearcher.put(cn,i)
        cn to block
    }
    var selectedBlocks = arrayListOf<Block>()
    var resultBlocks = listOf<Block>()
    val searchBox = REditBox("中文名/拼音",2,20,100, length = 48).also {
        it.justify()
        registerWidget(it) }

    var resultGrid = gridLayout(this, hAlign = HAlign.LEFT,x=2,y=90, colSpacing = -10, maxColumns = 5) {
        TFCBlocks.BLOCKS.entries.take(25).forEach {
            val item = it.get().asItem()
            button(item = item, text = item.chineseName.string, hoverText = item.englishName.string+"\n"+item.id)
        }
    }

    override fun tick() {
        if(mc pressingKey InputConstants.KEY_F5){
            mc go BlockSelectScreen(prevScreen)
            return
        }
        if(mc.pressingEnter){
            val txt = searchBox.value
            val searchedIndexes = pinyinSearcher.search(txt)
            resultBlocks = searchedIndexes.map { allBlocks[it].second }
        }
        super.tick()
    }
    override fun init() {
        title.append(mcText("(共"+allBlocks.size.toString()+"个)"))
        super.init()
    }
    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        guiGraphics.drawString(2,40,"已选择：")
        guiGraphics.drawString(2,70,"已搜到：（前25个）")
    }
}