package calebxzhou.rdi.sound

import calebxzhou.rdi.logger
import calebxzhou.rdi.util.getJarResourceStream
import java.io.InputStream

object RSoundPlayer {
    var playerNow: OggPlayer? = null
    fun play(path:String): OggPlayer? {
        getJarResourceStream("assets/rdi/sounds/$path")?.let {
            return play(it)
        }?:let {
            logger.warn("找不到jar内音频$path")
            return null
        }
    }
    fun play(stream: InputStream): OggPlayer {
        stopAll()
        val player = OggPlayer(stream)
        player.start()
        playerNow = player
        return player
    }
    fun stopAll(){
        try {
            playerNow?.interrupt()
            playerNow=null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun info(){
        play("info.ogg")
    }
}