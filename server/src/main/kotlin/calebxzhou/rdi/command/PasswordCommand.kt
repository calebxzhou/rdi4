package calebxzhou.rdi.command

import calebxzhou.rdi.service.AccountService.setPassword
import calebxzhou.rdi.util.chat
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object PasswordCommand {
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("password").executes {
            it.source.chat("用法：/password 要设定的密码")
            return@executes 1
        }.then(
            Commands.argument("pwd",StringArgumentType.string())
                .executes { ctx ->
                    ctx.source.player?.let {
                        val pwd = StringArgumentType.getString(ctx, "pwd")
                        it.setPassword(pwd)
                        return@executes 1
                    }?:0
                }
        )
}