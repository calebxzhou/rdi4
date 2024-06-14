package calebxzhou.rdi.service

import calebxzhou.rdi.util.chat
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.nickname
import net.minecraft.server.level.ServerPlayer

object PlayerService {
    fun onChat(player: ServerPlayer, message: String){
        mc.playerList.players.forEach { player.chat("${player.nickname}: $message") }
    }
}