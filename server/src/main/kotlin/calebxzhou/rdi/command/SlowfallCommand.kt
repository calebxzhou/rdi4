package calebxzhou.rdi.command

import calebxzhou.rdi.RDI
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object SlowfallCommand {
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("slowfall").executes { cmd ->
            val player = cmd.source.player!!
            player.setPos(player.x,300.0,player.z)
            return@executes 1
        }
}