package calebxzhou.rdi.command

import calebxzhou.rdi.RDI
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object MsptCommand {
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("mspt").executes { cmd ->
            cmd.source.sendSystemMessage(Component.literal("mspt: ${RDI.msPerTick} msbt: ${RDI.msBetweenTick}"))
            return@executes 1
        }
}