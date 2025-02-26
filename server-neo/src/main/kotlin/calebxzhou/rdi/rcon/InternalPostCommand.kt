package calebxzhou.rdi.rcon

import calebxzhou.rdi.log
import calebxzhou.rdi.util.mcs
import calebxzhou.rdi.util.pop
import calebxzhou.rdi.util.toMcText
import calebxzhou.rdi.util.toUUID
import calebxzhou.rdi.util.teleport
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer
import org.bson.types.ObjectId
import kotlin.reflect.full.declaredMemberFunctions

//hq向mcs发送数据，执行操作
object InternalPostCommand {
    val CMD = Commands.literal("__rdi_internal_post")
        .then(
            Commands.argument("params", StringArgumentType.string())
                .executes{
                    try {
                        exec(it)
                    } catch (e: Exception) {
                        log.error("执行rcon指令出错！")
                        it.source.sendSystemMessage(Status.INTERNAL_ERR.code.toString().toMcText())
                        e.printStackTrace()
                    }
                    1
                }
        )
    private enum class Status(val code: Int){
        PLAYER_OFFLINE(404),
        OK(200),
        INVALID_ARG(400),
        INTERNAL_ERR(500)

    }
    private fun findPlayerByUid(uid: String): ServerPlayer?{
        if(!ObjectId.isValid(uid))
            throw IllegalArgumentException("uid格式错误")
        return ObjectId(uid).toUUID().let { mcs.playerList.getPlayer(it) }
    }
    private fun exec(ctx: CommandContext<CommandSourceStack>) {
        if(ctx.source.isPlayer)
            return
        val params = StringArgumentType.getString(ctx,"params")
        log.info("rcon 执行：${params}")
        val tokens = params.split(" ").toMutableList()
        val function = tokens.pop()
        //action 按名字调用函数
        val method = this::class.declaredMemberFunctions.find { it.name == function }
        val status = method?.call(this, tokens) as? Status ?: throw IllegalArgumentException("找不到函数: $function")
        log.info("执行结果：${params}")
        ctx.source.sendSystemMessage(status.code.toString().toMcText())
    }
    // uid 内容
    private fun tell(tokens: MutableList<String>): Status{
        val p1 = findPlayerByUid(tokens.pop())?:return Status.PLAYER_OFFLINE
        p1.sendSystemMessage(tokens.joinToString(" ").toMcText())
        return Status.OK
    }
    //玩家传送： uid1 mode(player) uid2
    //坐标传送： uid1 mode(pos) dim,x,y,z,w,p
    //包装坐标传送： uid1 mode(posL) dim,posL
    private fun tp(tokens: MutableList<String>):Status{
        val p1 = findPlayerByUid(tokens.pop())?:return Status.PLAYER_OFFLINE
        val mode = tokens.pop()
        when(mode){
            "player" -> {
                val p2 = findPlayerByUid(tokens.pop())?:return Status.PLAYER_OFFLINE
                p1 teleport p2
                return Status.OK
            }
            "pos" -> {
                val subtokens = tokens.pop().split(",").toMutableList()
                if(subtokens.size!=6)
                    return Status.INVALID_ARG
                val dim = subtokens.pop()
                val x = subtokens.pop().toDouble()
                val y = subtokens.pop().toDouble()
                val z = subtokens.pop().toDouble()
                val w = subtokens.pop().toFloat()
                val p = subtokens.pop().toFloat()
                p1.teleport(dim,x,y,z,w,p)
            }
            "posL" -> {
                val subtokens = tokens.pop().split(",").toMutableList()
                if(subtokens.size!=2)
                    return Status.INVALID_ARG
                val dim = subtokens.pop()
                val posL = subtokens.pop().toLong()
                p1.teleport(dim,posL)
            }
            else -> {
                return Status.INVALID_ARG
            }
        }
        return Status.INVALID_ARG
    }
    // uid1 32 minecraft:dirt nbt?
    private fun giveItem(tokens: MutableList<String>){

    }
    //iid
    private fun createIsland(tokens: MutableList<String>){}
    //uid1
    private fun resetPlayer(tokens: MutableList<String>){}
    //iid
    private fun deleteIsland(tokens: MutableList<String>){}
}