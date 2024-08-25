package calebxzhou.rdi.tutorial

import calebxzhou.rdi.util.mc
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object TutorialCommand {
    val cmd = Commands.literal("tutorial").then(
        Commands.argument("1", StringArgumentType.string())
            .suggests{
                    _, build ->
                SharedSuggestionProvider.suggest(
                    arrayOf(
                        "prev",
                        "next",
                        "quit",
                        "pause",
                        ), build
                )
            }
            .executes{
            val subCmd = StringArgumentType.getString(it,"1")

            when(subCmd){
                "prev" -> {
                    Tutorial.now?.prevStep(mc.player!!)
                }
                "next" -> {
                    Tutorial.now?.nextStep(mc.player!!)
                }
                "quit" -> {
                    Tutorial.now?.quit()
                }
                "pause" -> {
                    Tutorial.now?.isPaused = true
                }
            }
            return@executes 1
        }
    )
}