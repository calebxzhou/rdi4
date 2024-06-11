package calebxzhou.rdi.command

import calebxzhou.rdi.RDI
import calebxzhou.rdi.db
import calebxzhou.rdi.model.Island
import calebxzhou.rdi.model.IslandMember
import calebxzhou.rdi.model.IslandRole
import calebxzhou.rdi.service.IslandService
import calebxzhou.rdi.service.PlayerService
import calebxzhou.rdi.util.*
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import org.bson.types.ObjectId
import java.util.UUID

object IslandCommand {
    private val islandHelp = """
            =====RDI空岛v4管理菜单=====
            /island <...>
            create 建岛 delete 删岛 reset 重置岛  
            home 回岛 sethome 更改传送点 kick 移除成员 invite 邀请成员
            transfer 改变岛主 quit 退岛
            ====================
            """.trimIndent()
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("island").executes { cmd ->
            cmd.source.chat(islandHelp)
            return@executes 1
        }.then(
            Commands.argument("param", StringArgumentType.string())
                .suggests { _, build ->
                    SharedSuggestionProvider.suggest(
                        arrayOf(
                            "create",
                            "delete",
                            "reset",
                            "home",
                            "sethome",
                            "kick",
                            "invite",
                            "transfer",
                            "quit"
                        ), build
                    )
                }.executes { ctx ->
                    ctx.source.player?.let {
                        handleSubCommand(

                            StringArgumentType.getString(ctx, "param"), it,
                        )
                    } ?: 0
                }.then(
                    Commands.argument("player", EntityArgument.player())
                        .executes { ctx ->
                            ctx.source.player?.let {
                                handleSubCommand(
                                    StringArgumentType.getString(ctx, "param"),
                                    it, EntityArgument.getPlayer(ctx, "player")
                                )
                            } ?: 0
                        }
                )
        )

    private fun handleSubCommand(param: String, p1: ServerPlayer): Int {
        GlobalScope.launch {

            when (param) {
                "create" -> IslandService.create(p1)
                "delete" -> IslandService.delete(p1)
                "home" -> IslandService.home(p1)
                "sethome" -> IslandService.sethome(p1)
                "quit" -> IslandService.quit(p1)
                else -> p1.chat(islandHelp)
            }
        }

        return 1
    }

    private fun handleSubCommand(param: String, p1: ServerPlayer, p2: ServerPlayer): Int {
        GlobalScope.launch {
            when (param) {
                "invite" -> IslandService.invite(p1, p2)
                "kick" -> IslandService.kick(p1, p2)
                "transfer" -> IslandService.transfer(p1, p2)
                else -> p1.chat(islandHelp)
            }
        }

        return 1
    }


}