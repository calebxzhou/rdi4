package calebxzhou.rdi.command

import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcText
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.SectionPos
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.level.levelgen.Heightmap
import kotlin.random.Random

object RandomTeleportCommand {
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("random-teleport").executes { cmd ->
            if (cmd.source.isPlayer) {
                val player = cmd.source.player!!
                if(player.health != player.maxHealth){
                    player.sendSystemMessage(mcText("必须满血才能随机传送"))
                    return@executes 1
                }
                player.health=1F
                player.foodData.foodLevel=1
                val x =Random.nextDouble(-5000.0, 5000.0)
                val z =Random.nextDouble(-5000.0, 5000.0)
                val levelChunk: LevelChunk =
                    player.level().getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))
                val y = levelChunk.getHeight(
                    Heightmap.Types.WORLD_SURFACE,
                    x.toInt() and 15,
                    z.toInt() and 15
                )
                player.teleportTo(
                    mc.overworld(),
                    x, y.toDouble(),z,0f,0f

                    )
            }
            return@executes 1
        }
}