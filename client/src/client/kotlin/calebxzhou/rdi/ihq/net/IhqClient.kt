package calebxzhou.rdi.ihq.net

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.protocol.SPacket
import calebxzhou.rdi.log
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.util.bgTask
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel

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
    val client
    get() = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    RAccount.ihqCredential
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
    fun put(path: String, params: List<Pair<String, String>>?, onSuccess: suspend (HttpResponse) -> Unit) = bgTask{
        send(HttpMethod.Put, path, params){
            bgTask { onSuccess(it) }
        }
    }
    fun get(path: String, onSuccess: suspend (HttpResponse) -> Unit) {
        send(HttpMethod.Get, path, null){
            bgTask { onSuccess(it) }
        }
    }
    fun get(path: String, params: List<Pair<String, String>>, onSuccess: suspend (HttpResponse) -> Unit) {
        send(HttpMethod.Get, path, params){
            bgTask { onSuccess(it) }
        }
    }

    suspend fun send(method: HttpMethod, path: String, params: List<Pair<String, String>>?,): HttpResponse{
        return client.request("http://${Const.SERVER_ADDR}:${Const.IHQ_PORT}/$path") {
            this.method = method
            params?.forEach { (key, value) ->
                parameter(key, value)
            }
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
                val errText = when(response.status){
                    HttpStatusCode.Unauthorized -> "未登录/用户名密码错误"
                    HttpStatusCode.Conflict -> "已存在"
                    HttpStatusCode.NotFound -> "找不到请求"
                    HttpStatusCode.BadRequest -> "请求格式错误，请更新客户端"
                    else -> "未知错误"
                }
                    alertErr("错误：${errText}，${response.bodyAsText()}")
                    log.error("${response.status}")

            }
            // Handle the response here
        } catch (e: Exception) {
            e.printStackTrace()
            alertErr(e.localizedMessage)
        }
    }

}