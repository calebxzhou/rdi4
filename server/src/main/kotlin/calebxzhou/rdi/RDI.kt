package calebxzhou.rdi

import calebxzhou.rdi.Const.DEBUG
import calebxzhou.rdi.Const.isLandMode
import calebxzhou.rdi.command.*
import calebxzhou.rdi.net.RPacketDecoder
import calebxzhou.rdi.model.Island
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.net.RPacketEncoder
import calebxzhou.rdi.net.RPacketReceiver
import calebxzhou.rdi.service.EntityCleaner
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.placeBlock
import com.mojang.brigadier.CommandDispatcher
import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.block.Blocks
import org.bson.UuidRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.nucleoid.fantasy.Fantasy
import xyz.nucleoid.fantasy.FantasyInitializer


val db = MongoClient.create(MongoClientSettings.builder()
    .applyToClusterSettings { builder ->
        builder.hosts(listOf(ServerAddress("127.0.0.1", 27017)))
    }
    .uuidRepresentation(UuidRepresentation.STANDARD)
    .build()).getDatabase("rdi")
val rScope = GlobalScope
val log: Logger = LoggerFactory.getLogger("rdi")
object RDI : ModInitializer {
    @JvmStatic
    val rlog: Logger = log
    private var tickTime1 = 0L

    //两tick之间的时间 最快50ms
    var msBetweenTick = 0L
    var msPerTick = 0L

    @JvmStatic
    val playerAmount
        get() = mc.playerList.playerCount

    @JvmStatic
    val isLagging
        get() = msPerTick > 50

    override fun onInitialize() {
        if(isLandMode){
            log.info("陆地服")
        }
        runBlocking {
            log.info("prepare DB")
            db.getCollection<RAccount>("account")
                .createIndex(Indexes.ascending("uuid"), IndexOptions().unique(true))
            db.getCollection<Island>("island")
                .createIndex(Indexes.ascending("members.pid"), IndexOptions().background(true))
        }


        ServerLifecycleEvents.SERVER_STOPPING.register(ServerStopping { server: MinecraftServer? ->
            val fantasy = Fantasy.get(server)
            fantasy.onServerStopping()
        })
        FantasyInitializer.onInitialize()
        ServerLifecycleEvents.SERVER_STARTING.register { server: MinecraftServer ->
            mc = server
        }
        ServerLifecycleEvents.SERVER_STARTED.register {
            //Timer("TickInverter_EntityCleaner").schedule(EntityCleaner, 0L, 300 * 1000)
            mc.overworld().placeBlock(Const.BASE_POS, Blocks.BEDROCK)

        }
        ServerTickEvents.START_SERVER_TICK.register {
            val fantasy = Fantasy.get(it)
            fantasy.tick()
            msBetweenTick = System.currentTimeMillis() - tickTime1
            tickTime1 = System.currentTimeMillis()
            if (isLagging) {
                //EntityCleaner.cleanMobs()
                //EntityCleaner.cleanRubbish()
                //mc.allLevels.forEach { it.chunkSource.setSimulationDistance(5) }
            } else {
                //mc.allLevels.forEach { it.chunkSource.setSimulationDistance(8) }
            }
        }
        ServerTickEvents.END_SERVER_TICK.register {
            msPerTick = System.currentTimeMillis() - tickTime1
        }
        CommandRegistrationCallback.EVENT.register { commandDispatcher: CommandDispatcher<CommandSourceStack>, commandBuildContext: CommandBuildContext, commandSelection: Commands.CommandSelection ->
            commandDispatcher.register(MsptCommand.builder)
            if(!isLandMode){
            commandDispatcher.register(IslandCommand.builder)
            }
            if(isLandMode){
                commandDispatcher.register(RandomTeleportCommand.builder)
            }
            //commandDispatcher.register(PasswordCommand.builder)
            commandDispatcher.register(SpawnCommand.builder)
            //commandDispatcher.register(SlowfallCommand.builder)

            if (DEBUG) {
                commandDispatcher.register(MobLagTestCommand.builder)
            }
            //commandDispatcher.register(MobLagTestCommand.builder)
        }
        Bootstrap()
            .group(NioEventLoopGroup())
            .channel(NioDatagramChannel::class.java)
            .handler(object : ChannelInitializer<NioDatagramChannel>() {
                override fun initChannel(ch: NioDatagramChannel) {
                    ch.pipeline()
                        .addLast("decoder", RPacketDecoder())
                        .addLast("packet_handler", RPacketReceiver())
                        .addLast("encoder", RPacketEncoder())
                    // Add your other handlers here

                }
            })
            .bind(Const.SERVER_PORT) // Bind to the same port
            .syncUninterruptibly()
    }
}