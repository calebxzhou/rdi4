package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.launcher.SplashScreen
import calebxzhou.rdi.launcher.SplashScreen.height
import calebxzhou.rdi.launcher.SplashScreen.width
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.RLoadingOverlay.Companion
import calebxzhou.rdi.ui.RLoadingOverlay.Companion.LOGO
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.ROptionScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.ui.general.alertInfo
import calebxzhou.rdi.ui.general.confirm
import calebxzhou.rdi.ui.layout.RGridLayout
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
    val LOGO: ResourceLocation =
        ResourceLocation("rdi", "textures/gui/title/mojangstudios.png")
    private
    val PANORAMA_OVERLAY: ResourceLocation =
        ResourceLocation("rdi", "textures/gui/title/background/panorama_overlay.png")
    val SCREEN_BG: ResourceLocation =
        ResourceLocation("rdi", "textures/screen_bg.png")
    private val panorama = PanoramaRenderer(CUBE_MAP)
    var shiftMode = false
    var ctrlMode = false

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
        submit {
            val pwd = it.formData["pwd"]!!
            val cpwd = it.formData["cpwd"]!!
            if (pwd != cpwd) {
                alertErr("确认密码与密码不一致", it.screen)
                return@submit
            }
            val qq = it.formData["qq"]!!
            val name = it.formData["name"]!!
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
    val loginScreen = formScreen(this, "登录账号") {
        text("usr", "QQ号/昵称/ID", 16)
        pwd("pwd", "密码")
        submit {
            val usr = it.formData["usr"]!!
            val pwd = it.formData["pwd"]!!
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
    val optScreen = ROptionScreen(
        this@RTitleScreen,
        "注册新账号" to {
            mc goScreen regScreen
        },
        "登录已有账号" to {
            mc goScreen loginScreen
        },
    )
    public override fun init() {
        SplashScreen.hide()
        val account = RAccount.now?:RAccount.DEFAULT
        //关闭音乐
        mc.options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0)
        RPlayerHeadButton(account){
            if(account==RAccount.DEFAULT)
                mc goScreen optScreen
            else
            mc goScreen RProfileScreen(account)
        }.apply {
            x = mcUIWidth/2-width/2
            y=2
        }.also { registerWidget(it) }
        RGridLayout(mc.window.guiScaledWidth / 2, mcUIHeight - 17).apply {
            row(
                5,
                RTextButton("进入服务器") {
                    if (RAccount.now == null) {

                        mc goScreen optScreen
                    } else {
                        RSoundPlayer.stopAll()
                        ConnectScreen.startConnecting(
                            this@RTitleScreen,
                            mc,
                            ServerAddress(Const.SERVER_ADDR, Const.SERVER_PORT),
                            Const.SERVER_DATA,
                            false
                        )

                    }


                },
                RTextButton("Mod列表") {
                    mc goScreen ModsScreen(this@RTitleScreen)
                },
                RTextButton("设置") {
                    mc goScreen OptionsScreen(this@RTitleScreen, mc.options)
                },
                RTextButton("单人模式") {
                    if (shiftMode)
                        mc goScreen SelectWorldScreen(this@RTitleScreen)
                    else
                        openFlatLevel()
                },
                RTextButton("致谢") {
                    alertInfo("服务器硬件：wuhudsm66\n任务设计：terryaxe",this@RTitleScreen)
                },
            )
        }.visitWidgets { registerWidget(it) }


        super.init()
    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        //panorama.render(partialTick, Mth.clamp(1f, 0.0f, 1.0f))
        RenderSystem.enableBlend()
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        //guiGraphics.blit(PANORAMA_OVERLAY, 0, 0, this.width, this.height, 0.0f, 0.0f, 16, 128, 16, 128)
        guiGraphics.blit(SCREEN_BG, 0, 0, 0f,0f, mcUIWidth,mcUIHeight, mcUIWidth, mcUIHeight)
        guiGraphics.blit(LOGO, mcUIWidth/2-100, mcUIHeight/2-20, -0.0625f, 0.0f, 120, 60, 120, 120)
        guiGraphics.blit(LOGO, mcUIWidth/2 -30, mcUIHeight/2-20, 0.0625f, 60.0f, 120, 60, 120, 120)

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        //下方的灰条
        guiGraphics.fill(0, height - 20, width, height, 0xAA000000.toInt())
        //drawTextAt(guiGraphics, Const.VERSION_STR, 10, height - 15)
//上方的灰条（已登录
        guiGraphics.fill(width/2-80, 0, width/2+80, 20, 0xAA000000.toInt())
        /*RAccount.now?.let {
            PlayerFaceRenderer.draw(guiGraphics, it.skinLocation, width/2-40, 2, 15)
        }*/


    }

    override fun tick() {
        shiftMode = mc pressingKey InputConstants.KEY_LSHIFT
        ctrlMode = mc pressingKey InputConstants.KEY_LCONTROL
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