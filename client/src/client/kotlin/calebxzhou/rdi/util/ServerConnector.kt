package calebxzhou.rdi.util

import calebxzhou.rdi.Const.SERVER_ADDR
import calebxzhou.rdi.Const.SERVER_PORT
import calebxzhou.rdi.Const.VERSION_STR
import calebxzhou.rdi.log
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.network.Connection
import java.net.InetSocketAddress

object ServerConnector {
    fun connect(screen: Screen){
        ConnectScreen.startConnecting(
            screen,
            mc,
            ServerAddress(SERVER_ADDR,SERVER_PORT),
            ServerData(VERSION_STR, SERVER_ADDR,false),
            false
        )
    }
    fun ping(){
        try {
            Connection.connectToServer(InetSocketAddress(SERVER_ADDR, SERVER_PORT),true)
        } catch (e: Exception) {
            log.warn("无法ping服务器：${e.localizedMessage}")
        }
    }
}