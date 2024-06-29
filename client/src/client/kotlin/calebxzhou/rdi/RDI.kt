package calebxzhou.rdi

import calebxzhou.rdi.RDI.arrowCursor
import calebxzhou.rdi.RDI.handCursor
import calebxzhou.rdi.RDI.homeKey
import calebxzhou.rdi.RDI.ibeamCursor
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.launcher.SplashScreen
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.atomicfu.locks.synchronized
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.Util
import net.minecraft.client.KeyMapping
import net.minecraft.client.User
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.resources.language.ClientLanguage
import net.minecraft.locale.Language
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW
import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.system.exitProcess

val log: Logger = LogManager.getLogger("RDI")

object RDI : ModInitializer {
    @JvmStatic
    val user: User
    val loadedModsIdName = hashMapOf<String,String>()
    lateinit var homeKey: KeyMapping
    var handCursor: Long = 0
    var ibeamCursor: Long = 0
    var arrowCursor: Long = 0

    init {
        val defaultName = "rdiplayer" + Random.nextInt(10000)
        user = User(
            LocalStorage["name"] ?: defaultName,
            LocalStorage["uuid"] ?: Utils.createUuid(defaultName).toString(),
            "",
            Optional.empty(),
            Optional.empty(),
            User.Type.LEGACY
        )
    }


    @JvmStatic
    fun onMcStart() {

        if (Util.getPlatform() == Util.OS.WINDOWS) {
            log.info("是windows系统，启动RDI载入画面与系统托盘")
            System.setProperty("java.awt.headless", "false")
            SplashScreen
        }
        try {
            Class.forName("net.wurstclient.WurstClient")
            exitProcess(0)
        }catch (e: Exception){
            log.info("ok")
        }
        val usr = LocalStorage["usr"]
        val pwd = LocalStorage["pwd"]
        if (!usr.isNullOrBlank() && !pwd.isNullOrBlank()) {
            IhqClient.get(
                "login", listOf(
                    "usr" to usr,
                    "pwd" to pwd
                )
            ) {
                val account = serdesJson.decodeFromString<RAccount>(it.bodyAsText())
                RAccount.now = account
                popupInfo("${periodOfDay}好，${account.name}")
            }
        }
        IhqClient.get("version") {
            checkUpdate(it)
        }
        RSoundPlayer.play("assets/rdi/sounds/info.ogg")
    }

    private suspend fun checkUpdate(response: HttpResponse) {
        if (response.bodyAsText().toInt() != Const.IHQ_VERSION) {
            popupInfo("RDI核心需要更新，即将开始下载")
            val fileChannel: ByteWriteChannel = File("mods/rdi-1.0.0.jar").writeChannel()
            val fileResponse = HttpClient().get("http://${Const.SERVER_ADDR}:${Const.IHQ_PORT}/core")
            fileResponse.bodyAsChannel().copyAndClose(fileChannel)
            popupInfo("更新完成，请重启游戏")
            exitProcess(0)
        }
    }

    override fun onInitialize() {
        log.info(Const.VERSION_STR)

        homeKey = KeyBindingHelper.registerKeyBinding(KeyMapping("回岛", InputConstants.KEY_H, Const.VERSION_STR))
        ClientTickEvents.START_CLIENT_TICK.register {
            if (homeKey.consumeClick()) {
                mc.connection?.sendCommand("island home")
            }
            mc.player?.let {
                if (it.y < -120) {
                    mc.connection?.sendCommand("spawn")
                }
            } ?: let {

            }
        }
        ClientLifecycleEvents.CLIENT_STARTED.register {
            handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_POINTING_HAND_CURSOR)
            ibeamCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)
            arrowCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
            SplashScreen.splash.isAlwaysOnTop = false
        }
    }

    @JvmStatic
    fun log(): Logger {
        return log
    }

    var cursorTypeChanged = false

    @JvmStatic
    fun onRenderWidget(widget: AbstractWidget, isHovered: Boolean) {

        if (isHovered) {
            if (!cursorTypeChanged) {
                synchronized(this) {

                    cursorTypeChanged = true
                    //鼠标悬浮 光标形状改变
                    GLFW.glfwSetCursor(
                        mcWindowHandle, when (widget) {
                            is Button -> handCursor
                            is EditBox -> ibeamCursor
                            else -> arrowCursor
                        }
                    )
                }

            }
        } else {
            if (cursorTypeChanged) {
                cursorTypeChanged = false
                GLFW.glfwSetCursor(mcWindowHandle, arrowCursor)

            }
        }

    }
}