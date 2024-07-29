package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.model.Team
import calebxzhou.rdi.ihq.service.PlayerService
import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import kotlinx.coroutines.runBlocking
import org.bson.UuidRepresentation
import java.io.File

val log = KotlinLogging.logger {}
val db = MongoClient.create(MongoClientSettings.builder()
    .applyToClusterSettings { builder ->
        builder.hosts(listOf(ServerAddress("127.0.0.1", 27017)))
    }
    .uuidRepresentation(UuidRepresentation.STANDARD)
    .build()).getDatabase("rdi_neo")
val accountCol = db.getCollection<Account>("account")
val teamCol = db.getCollection<Team>("team")
fun main() {
    runBlocking {
        log.info { "init db" }
        accountCol.createIndex(Indexes.ascending("qq"), IndexOptions().unique(true))
        accountCol.createIndex(Indexes.ascending("name"), IndexOptions().unique(true))
        teamCol
            .createIndex(Indexes.ascending("members.pid"), IndexOptions().background(true))
    }
    Bootstrap()
        .group(NioEventLoopGroup())
        .channel(NioDatagramChannel::class.java)
        .handler(object : ChannelInitializer<NioDatagramChannel>() {
            override fun initChannel(ch: NioDatagramChannel) {
                ch.pipeline()
                    .addLast("decoder", RIhqPacketDecoder())
                    .addLast("packet_handler", RPacketReceiver())
                    .addLast("encoder", RIhqPacketEncoder())
                // Add your other handlers here

            }
        })
        .bind(PORT) // Bind to the same port
        .syncUninterruptibly()
    embeddedServer(Netty, host = "0.0.0.0", port = PORT) {
        routing {
            get("/core") {
                val file = File("assets/rdi-1.0.0.jar")
                call.respondFile(file)
            }
            get("/skin") {
                PlayerService.getSkin(call)
            }

        }

    }.start(wait = true)

}
