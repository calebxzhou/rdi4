package calebxzhou.rdi.util

import net.minecraft.client.Minecraft

val mc: () -> Minecraft = {Minecraft.getInstance()}
val ofWindowHandle: () -> Long = {
    Minecraft.getInstance().window.window
}
object McUtils{
    @JvmStatic
    fun getWindowSize(screenW: Int, screenH: Int) : Pair<Int,Int>{
        if (screenW >= 3840 && screenH >= 2160)
            return 1920 to 1080
        if(screenW>=1920 && screenH >=1080)
            return 1600 to 900
        if(screenW>=1600 && screenH>=900)
            return 1280 to 720
        return 800 to 480
    }
}