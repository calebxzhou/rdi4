package calebxzhou.rdi.cmd

import calebxzhou.rdi.util.mcText
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

/*
command("test"){

    exec = {

    }
    stringArg("001"){
        exec = {

        }
        stringArg("002"){
        exec = {

                }
                stringArg("003"){

                }
        }
    }
}
 */
typealias CommandExecution = (CommandContext<CommandSourceStack>) -> Unit

fun command(
    name: String,
    init: RCommandNode.() -> Unit
) {
    val cmd = RCommandNode(name)
    cmd.init()
    val mcCmd = Commands.literal(name).executes{
        cmd.exec(it)
        return@executes 1
    }
}

class RCommandNode(val name: String) {
    private val children = mutableMapOf<String, RCommandNode>()
    var exec: CommandExecution = { it.source.sendSystemMessage(mcText("缺少具体参数")) }

    fun stringArg(value: String, init: RCommandNode.() -> Unit) {
        val cmd = RCommandNode(value)
        cmd.init()
        children += value to cmd
    }
}