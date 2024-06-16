package calebxzhou.rdi.service

import calebxzhou.rdi.db
import calebxzhou.rdi.log
import calebxzhou.rdi.model.Chat
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.util.chat
import calebxzhou.rdi.util.goServerSpawn
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.nickname
import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.server.level.ServerPlayer
import org.bson.types.ObjectId

object PlayerService {
    val onlinePlayers = hashMapOf< ChannelHandlerContext,ServerPlayer>()
    private val chatCol = db.getCollection<Chat>("chat")
    fun onChat(player: ServerPlayer, message: String) {
        val msg = "${player.nickname}: $message"
        log.info(msg)
        mc.playerList.players.forEach { it.chat(msg) }
        GlobalScope.launch {

            chatCol.insertOne(Chat(ObjectId(), player.uuid, message))
        }
    }

    //断开连接后
    fun afterDisconnect(player: ServerPlayer) {

    }
    //加入服务器后
    fun afterJoin(player: ServerPlayer) {
        player.goServerSpawn()

    }


}