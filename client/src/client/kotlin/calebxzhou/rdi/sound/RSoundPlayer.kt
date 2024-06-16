package calebxzhou.rdi.sound

import calebxzhou.rdi.log
import calebxzhou.rdi.util.dialogErr
import calebxzhou.rdi.util.getJarResourceStream
import org.lwjgl.openal.AL10
import java.io.InputStream

object RSoundPlayer {
    var playerNow: OggPlayer? = null
    fun play(path:String): OggPlayer? {
        getJarResourceStream(path)?.let {
            return play(it)
        }?:let {
            log.warn("找不到jar内音频$path")
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
        playerNow?.stop()
        playerNow=null
    }
}