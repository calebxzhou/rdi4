package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.launcher.SplashScreen
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.ROptionScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.ui.general.confirm
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.systems.RenderSystem
import com.terraformersmc.modmenu.gui.ModsScreen
import io.ktor.client.statement.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.PlayerFaceRenderer
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.client.renderer.CubeMap
import net.minecraft.client.renderer.PanoramaRenderer
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.Difficulty
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraft.world.level.levelgen.presets.WorldPresets

class RTitleScreen : RScreen("主页") {
    override var showTitle = false
    override var showCloseButton = false
    val CUBE_MAP: CubeMap = CubeMap(ResourceLocation("rdi", "textures/gui/title/background/panorama"))

    private
    val PANORAMA_OVERLAY: ResourceLocation =
        ResourceLocation("rdi", "textures/gui/title/background/panorama_overlay.png")
    private val panorama = PanoramaRenderer(CUBE_MAP)
    var shiftMode = false
    var ctrlMode = false

    lateinit var play: RTextButton
    lateinit var settings: RTextButton
    lateinit var about: RTextButton
    lateinit var quitBtn: RTextButton
    lateinit var profileBtn: RTextButton
    private val regScreen = formScreen(this, "注册账号") {
        text("name", "昵称", 16) {
            if (it.value.length in 3..16) {
                REditBoxValidationResult(true)
            } else {
                REditBoxValidationResult(false, "昵称长度必须3~16")

            }
        }
        text("qq", "QQ号", 10, true) {
            if (it.value.length in 5..10) {
                REditBoxValidationResult(true)
            } else {
                REditBoxValidationResult(false, "QQ格式错误")
            }
        }
        pwd("pwd", "密码")
        pwd("cpwd", "确认密码")
        submit { screen,inputs ->
            val pwd = inputs["pwd"]!!
            val cpwd = inputs["cpwd"]!!
            if (pwd != cpwd) {
                alertErr("确认密码与密码不一致",screen)
                return@submit
            }
            val qq = inputs["qq"]!!
            val name = inputs["name"]!!
            IhqClient.post(
                "register", listOf(
                    "pwd" to pwd,
                    "qq" to qq,
                    "name" to name,
                )
            ) {
                toastOk("注册成功")
                LocalStorage += "usr" to qq
                LocalStorage += "pwd" to pwd
                mc goScreen RTitleScreen()
            }
        }

    }.build()
    private val loginScreen = formScreen(this, "登录账号") {
        text("usr", "QQ号/昵称/ID", 16)
        pwd("pwd", "密码")
        submit { screen,inputs ->
            val usr = inputs["usr"]!!
            val pwd = inputs["pwd"]!!
            IhqClient.get(
                "login", listOf(
                    "usr" to usr,
                    "pwd" to pwd
                )
            ) {
                val account = serdesJson.decodeFromString<RAccount>(it.bodyAsText())
                LocalStorage += "usr" to usr
                LocalStorage += "pwd" to pwd
                RAccount.now = account
                toastOk("登录成功")
                mc goScreen RTitleScreen()
            }

        }
    }.build()

    public override fun init() {
        //关闭音乐
        mc.options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0)

        play = RTextButton(width - 90, height - 15, "开始") {
            SplashScreen.hide()
            if (shiftMode) {
                openFlatLevel()
            } else if (ctrlMode) {
                mc goScreen SelectWorldScreen(this)
            } else {
                if (RAccount.now == null) {
                    mc goScreen ROptionScreen(
                        this,
                        "注册新账号" to {
                            mc goScreen regScreen
                        },
                        "登录已有账号" to { mc goScreen loginScreen },
                    )
                } else {
                    RSoundPlayer.stopAll()
                    ConnectScreen.startConnecting(
                        this,
                        mc,
                        ServerAddress(Const.SERVER_ADDR, Const.SERVER_PORT),
                        Const.SERVER_DATA,
                        false
                    )

                }
            }
        }.also { registerWidget(it) }

        settings = RTextButton(width - 60, height - 15, "设置") {
            if (shiftMode) {
                mc goScreen ModsScreen(this)
            } else {
                mc goScreen OptionsScreen(this, mc.options)
            }
        }.also { registerWidget(it) }

        about = RTextButton(width - 30, height - 15, "关于") {
            if (shiftMode) {
                mc goScreen JoinMultiplayerScreen(this)
            } else {
                //mc goScreen AccountScreen()
            }
        }.also { registerWidget(it) }

        quitBtn = RTextButton(width - 25, 5, "登出") {
            RAccount.now?.let {
                confirm( "真的要退出账号${it.name}吗？",this) {
                    RAccount.now = null
                    toastOk("成功退出登录！")
                    mc goScreen RTitleScreen()
                }
            }
        }.also {
            it.visible = RAccount.isLogin
            registerWidget(it)
        }

        profileBtn = RTextButton(width - 70, 5, "个人信息") {
            RAccount.now?.let {
                mc goScreen RProfileScreen(it)
            }
        }.also {
            it.visible = RAccount.isLogin
            registerWidget(it)
        }


        super.init()
    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        panorama.render(partialTick, Mth.clamp(1f, 0.0f, 1.0f))
        RenderSystem.enableBlend()
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        guiGraphics.blit(PANORAMA_OVERLAY, 0, 0, this.width, this.height, 0.0f, 0.0f, 16, 128, 16, 128)
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        //下方的灰条
        guiGraphics.fill(0, height - 20, width, height, 0xAA000000.toInt())
        drawTextAt(guiGraphics, Const.VERSION_STR, 10, height - 15)
//上方的灰条（已登录
        RAccount.now?.let {
            guiGraphics.fill(0, 0, width, 20, 0xAA000000.toInt())
            PlayerFaceRenderer.draw(guiGraphics, it.skinLocation, 4, 2, 15)
            drawTextAt(guiGraphics, it.name, 25, 5)
        }
        //登出 个人信息


    }

    override fun tick() {
        shiftMode = mc pressingKey InputConstants.KEY_LSHIFT
        ctrlMode = mc pressingKey InputConstants.KEY_LCONTROL
        if (mc pressingKey InputConstants.KEY_RETURN) {
            play.onClick(0.0, 0.0)
        }
    }

    private fun openFlatLevel() {
        val levelName = "rdi_creative"
        if (mc.levelSource.levelExists(levelName)) {
            mc.createWorldOpenFlows().loadLevel(this, levelName)
        } else {
            val levelSettings = LevelSettings(
                levelName,
                GameType.CREATIVE,
                false,
                Difficulty.PEACEFUL,
                true,
                GameRules().apply {
                    getRule(GameRules.RULE_DAYLIGHT).set(false, null)
                    getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null)
                },
                WorldDataConfiguration.DEFAULT
            )
            mc.createWorldOpenFlows().createFreshLevel(
                levelName,
                levelSettings,
                WorldOptions(0, false, false)
            ) {
                it.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT)
                    .value().createWorldDimensions();
            }
        }
    }

}