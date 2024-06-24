package calebxzhou.rdi.command

import calebxzhou.rdi.service.IslandService
import calebxzhou.rdi.util.*
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer

object IslandCommand {
    private val islandHelp = """
            =====RDI空岛v4管理菜单=====
            /island <...>
            create 建岛 confirm_island_delete 删岛 
            home 回岛 sethome 更改传送点 kick 移除成员 invite 邀请成员
            fuck-tree 快速长树 rain 开关下雨 
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
                            "home",
                            "sethome",
                            "kick",
                            "invite",
                            "transfer",
                            "quit",
                            "rain",
                            "fuck-tree",

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
                "confirm_island_delete" -> IslandService.delete(p1)
                "home" -> IslandService.home(p1)
                "sethome" -> IslandService.sethome(p1)
                "quit" -> IslandService.quit(p1)
                "fuck-tree" -> IslandService.growTree(p1)
                "rain" -> IslandService.toggleRain(p1)
                //"force-random-tick" -> IslandService.randomTick(p1)
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