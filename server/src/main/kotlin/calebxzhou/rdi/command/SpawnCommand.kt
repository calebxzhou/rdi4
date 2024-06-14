package calebxzhou.rdi.command

import calebxzhou.rdi.RDI
import calebxzhou.rdi.service.IslandService
import calebxzhou.rdi.util.goServerSpawn
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.teleportTo
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object SpawnCommand {
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("spawn").executes { cmd ->
            cmd.source.player?.goServerSpawn()
            return@executes 1
        }
}