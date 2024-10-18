package calebxzhou.rdi.chunkstats

import calebxzhou.rdi.ui.component.RDocWidget
import calebxzhou.rdi.ui.general.RDialog
import calebxzhou.rdi.ui.general.dialog
import calebxzhou.rdi.util.addChatMessage
import calebxzhou.rdi.util.forEachBlock
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.toFixed
import net.minecraft.commands.Commands
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

object ChunkStats {
    val cmd = Commands.literal("chunkstats").executes {
        val player = mc.player!!
        val level = player.level()
        val chunk = level.getChunkAt(player.blockPosition())
        val blocks = linkedMapOf<Block, Int>()
        chunk.sections.forEach { section ->
            section.forEachBlock {
                val blk = it.block
                blocks[blk] = blocks.getOrDefault(blk, 0) + 1
            }
        }
        val totalBlocks = blocks.values.sum()
        val sortedBlocks = blocks.toList().sortedByDescending { (_, v) -> v }
        sortedBlocks.map {
            "${it.first.name.string} ${ForgeRegistries. ITEMS.getKey( it.first.asItem())}  ${it.second}x  ${
                ((it.second.toFloat() / totalBlocks) * 100).toFixed(
                    2
                )+"%"
            }"
        }
            .forEach {

                mc.addChatMessage(it)
            }
        1
    }
}