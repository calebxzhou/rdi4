package calebxzhou.rdi

import calebxzhou.rdi.Const.MODID
import calebxzhou.rdi.item.RItems
import calebxzhou.rdi.net.RPacketDecoder
import calebxzhou.rdi.net.RPacketEncoder
import calebxzhou.rdi.net.RPacketReceiver
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcs
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.slf4j.Logger
import org.slf4j.LoggerFactory


val log: Logger = LoggerFactory.getLogger(MODID)

@Mod(MODID)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class RDI {


    init {
        MinecraftForge.EVENT_BUS.addListener(::serverStart)
        MinecraftForge.EVENT_BUS.addListener(::onTick)
        RItems.REGISTER.register(FMLJavaModLoadingContext.get().modEventBus);

    }
    companion object {
        private var tickTime1 = 0L

        //两tick之间的时间 最快50ms
        var msBetweenTick = 0L
        var msPerTick = 0L

        @JvmStatic
        val isLagging
            get() = msPerTick > 50

        @JvmStatic
        fun log(): Logger {
            return log
        }

        @JvmStatic
        fun serverStart(e: ServerStartingEvent) {
            mcs = e.server
            //启动UDP服务器
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

        @JvmStatic
        fun onTick(e: TickEvent.ServerTickEvent) {
            if (e.phase == TickEvent.Phase.START) {
                msBetweenTick = System.currentTimeMillis() - tickTime1
                tickTime1 = System.currentTimeMillis()
            }
            if (e.phase == TickEvent.Phase.END) {
                msPerTick = System.currentTimeMillis() - tickTime1

            }
        }
    }

}