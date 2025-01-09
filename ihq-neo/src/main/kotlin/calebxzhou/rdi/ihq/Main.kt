package calebxzhou.rdi.ihq

import calebxzhou.rdi.ihq.exception.AuthError
import calebxzhou.rdi.ihq.exception.ParamError
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.model.AccountSession
import calebxzhou.rdi.ihq.model.Team
import calebxzhou.rdi.ihq.service.PlayerService
import calebxzhou.rdi.ihq.util.e400
import calebxzhou.rdi.ihq.util.e401
import calebxzhou.rdi.ihq.util.e500
import calebxzhou.rdi.ihq.util.ok
import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.coroutines.runBlocking
import org.bson.UuidRepresentation
import java.io.File

val dbHost = System.getProperty("rdi.dbHost") ?: "127.0.0.1"
val dbPort = System.getProperty("rdi.dbPort")?.toIntOrNull() ?: 27017
val hqPort = System.getProperty("rdi.hqPort")?.toIntOrNull() ?: 38411
val log = KotlinLogging.logger {}
val db = MongoClient.create(
    MongoClientSettings.builder()
    .applyToClusterSettings { builder ->
        builder.hosts(listOf(ServerAddress(dbHost, dbPort)))
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
    /*Bootstrap()
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
        .bind(hqPort) // Bind to the same port
        .syncUninterruptibly()*/
    embeddedServer(Netty, host = "::", port = hqPort) {
        install(StatusPages) {
            //参数不全或者有问题
            exception<ParamError> { call, cause ->
                call.e400(cause.message)
            }

            exception<AuthError> { call, cause ->
                call.e401(cause.message)
            }

            //其他内部错误
            exception<Throwable> { call, cause ->
                call.e500(cause.message)
            }
        }
        install(Sessions) {
            header<AccountSession>("rdi-ss", directorySessionStorage(File(".sessions")))
        }
        routing {
            get("/core") {
                val file = File("assets/rdi-1.0.0.jar")
                call.respondFile(file)
            }
            get("/skin") {
                PlayerService.getSkin(call)
            }
            get("/version") {
                call.ok(CORE_VERSION)
            }
            post("/register") {
                PlayerService.register(call)
            }
            post("/login") {
                PlayerService.login(call)
            }

        }

    }.start(wait = true)

}
