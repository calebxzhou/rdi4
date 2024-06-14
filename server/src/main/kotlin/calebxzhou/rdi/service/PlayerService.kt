package calebxzhou.rdi.service

import calebxzhou.rdi.db
import calebxzhou.rdi.model.Chat
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.util.chat
import calebxzhou.rdi.util.goServerSpawn
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.nickname
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.server.level.ServerPlayer
import org.bson.types.ObjectId

object PlayerService {
    private val chatCol = db.getCollection<Chat>("chat")
    fun onChat(player: ServerPlayer, message: String) {

        mc.playerList.players.forEach { it.chat("${player.nickname}: $message") }
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