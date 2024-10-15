package calebxzhou.rdi.service

import calebxzhou.rdi.log
import calebxzhou.rdi.util.chat
import calebxzhou.rdi.util.mcs
import calebxzhou.rdi.util.nickname
import io.netty.channel.ChannelHandlerContext
import net.minecraft.server.level.ServerPlayer

object PlayerService {
    val onlinePlayers = hashMapOf< ChannelHandlerContext,ServerPlayer>()
    fun onChat(player: ServerPlayer, message: String) {
        val msg = "${player.nickname}: $message"
        log.info(msg)
        mcs.playerList.players.forEach { it.chat(msg) }
    }

    //断开连接后
    fun afterDisconnect(player: ServerPlayer) {

    }
    //加入服务器后
    fun afterJoin(player: ServerPlayer) {

        //player.goServerSpawn()

    }


}