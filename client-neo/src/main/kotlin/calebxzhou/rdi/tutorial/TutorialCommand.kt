package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.addChatMessage
import calebxzhou.rdi.util.mc
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object TutorialCommand {
    val cmd = Commands.literal("tutorial").then(
        Commands.argument("1", StringArgumentType.string())
            .suggests { _, build ->
                SharedSuggestionProvider.suggest(
                    arrayOf(
                        "prev",
                        "next",
                        "quit",
                        "pause",
                        "index"
                    ), build
                )
            }
            .executes {
                val subCmd = StringArgumentType.getString(it, "1")
                it.source.player?.let { serverPlayer ->
                    when (subCmd) {
                        "prev" -> {
                            Tutorial.now?.prevStep(serverPlayer)
                            mc.addChatMessage("上一步")
                        }

                        "next" -> {
                            Tutorial.now?.nextStep(serverPlayer)
                            mc.addChatMessage("下一步")
                        }

                        "quit" -> {
                            Tutorial.now?.quit()
                            mc.addChatMessage("退出")
                        }

                        "pause" -> {
                            Tutorial.now?.isPaused = true
                            mc.addChatMessage("暂停")
                        }
                    }
                }
                1
            }
            .then(
                Commands.argument("2", IntegerArgumentType.integer())
                    .executes {
                        val idx = IntegerArgumentType.getInteger(it, "2")
                        mc.addChatMessage("去第${idx}步")
                        Tutorial.now?.stepIndex = idx
                        return@executes 1
                    }
            )
    )
}