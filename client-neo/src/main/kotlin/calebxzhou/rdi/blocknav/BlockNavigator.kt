package calebxzhou.rdi.blocknav

import calebxzhou.rdi.util.WHITE
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcUIWidth
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks

//方块导航
object BlockNavigator {
    var posNow: BlockPos? = null
        private set
    operator fun plusAssign(pos: BlockPos){


        posNow = pos
    }

    fun renderGui(guiGraphics: GuiGraphics) {
        posNow?.let { posNow ->
            guiGraphics.fill(0, 64, mcUIWidth, 84, 0x66000000.toInt())
            guiGraphics.drawCenteredString(mcFont, "导航到：${posNow.toShortString()}", mcUIWidth / 2, 70, WHITE)
            val fakeBeaconPos = posNow.mutable()
            fakeBeaconPos.setY(200)
            mc.level!!.setBlock(fakeBeaconPos, Blocks.BEACON.defaultBlockState(),0,0)
        }
    }

}