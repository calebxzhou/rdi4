package calebxzhou.rdi.ihq.net

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.protocol.CPacket
import calebxzhou.rdi.ihq.net.protocol.SPacket
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.util.bgTask
import calebxzhou.rdi.util.dialogErr
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.URL
import javax.swing.text.html.HTML.Tag.P

object IhqClient {
    val connection = Bootstrap()
        .group(NioEventLoopGroup())
        .channel(NioDatagramChannel::class.java)
        .option(ChannelOption.SO_BROADCAST, true)
        .handler(object : ChannelInitializer<NioDatagramChannel>() {
            override fun initChannel(ch: NioDatagramChannel) {
                ch.pipeline().apply {
                    addLast("decoder", RPacketDecoder())
                    addLast("packet_handler", RPacketReceiver())
                    addLast("encoder", RPacketEncoder())
                }
            }
        })
        .bind(0)
        .syncUninterruptibly()
    val client = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        username = RAccount.now?.id?.toString() ?: "",
                        password = RAccount.now?.pwd ?: ""
                    )
                }
                realm = "Access to the '/' path"
            }
        }
    }

    fun sendPacket(packet: SPacket) {
        connection.channel().writeAndFlush(packet)
    }

    fun post(path: String, params: List<Pair<String, String>>?, onSuccess: suspend (HttpResponse) -> Unit) = bgTask{
        send(HttpMethod.Post, path, params){
            bgTask { onSuccess(it) }
        }
    }

    fun get(path: String, onSuccess: suspend (HttpResponse) -> Unit) {
        send(HttpMethod.Get, path, null){
            bgTask { onSuccess(it) }
        }
    }fun get(path: String, params: List<Pair<String, String>>,onSuccess: suspend (HttpResponse) -> Unit) {
        send(HttpMethod.Get, path, params){
            bgTask { onSuccess(it) }
        }
    }


    fun send(method: HttpMethod, path: String, params: List<Pair<String, String>>?, onSuccess: (HttpResponse) -> Unit) = bgTask {
        try {
            val response = client.request("http://${Const.SERVER_ADDR}:${Const.IHQ_PORT}/$path") {
                this.method = method
                params?.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            if (response.status.isSuccess()) {
                onSuccess(response)
            } else {
                dialogErr("错误${response.status.value}：${response.bodyAsText()}")
            }
            // Handle the response here
        } catch (e: Exception) {
            e.printStackTrace()
            dialogErr(e.localizedMessage)
        }
    }

}