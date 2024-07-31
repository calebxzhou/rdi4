package calebxzhou.rdi.ui

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.account.LoginSPacket
import calebxzhou.rdi.ihq.protocol.account.RegisterSPacket
import calebxzhou.rdi.model.Account
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.RLoadingOverlay.Companion.LOGO
import calebxzhou.rdi.ui.RTitleScreen.Companion.optScreen
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.ROptionScreen
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.ui.general.alertInfo
import calebxzhou.rdi.ui.general.dialog
import calebxzhou.rdi.ui.layout.RGridLayout
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
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
    val LOGO: ResourceLocation =
        ResourceLocation("rdi", "textures/gui/title/mojangstudios.png")

    val SCREEN_BG: ResourceLocation =
        ResourceLocation("rdi", "textures/screen_bg.png")
    var shiftMode = false
    var ctrlMode = false

    companion object {
        val optScreen = { prevScreen: RScreen ->
            ROptionScreen(
                prevScreen,
                "注册新账号" to {
                    mc goScreen regScreen(it)
                },
                "登录已有账号" to {
                    mc goScreen loginScreen(it)
                },
            )
        }
        val loginScreen =
            { prevScreen: RScreen ->
                formScreen(prevScreen, "登录账号") {
                    text("usr", "QQ号/昵称/ID", 16)
                    pwd("pwd", "密码")
                    submit {

                        onLogin(it)

                    }
                }.build()
            }
        val regScreen = { prevScreen: RScreen ->
            formScreen(prevScreen, "注册账号") {
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
                    onRegister(it)
                }

            }.build()
        }

        fun onRegister(it: RFormScreenSubmitHandler) {
            val pwd = it.formData["pwd"]!!
            val cpwd = it.formData["cpwd"]!!
            if (pwd != cpwd) {
                alertErr("确认密码与密码不一致", it.screen)
                return
            }
            val qq = it.formData["qq"]!!
            val name = it.formData["name"]!!

            IhqClient.send(RegisterSPacket(name, pwd, qq)) { resp ->
                if(resp.ok){

                toastOk("注册成功")
                LocalStorage += "usr" to qq
                LocalStorage += "pwd" to pwd
                mc goScreen RTitleScreen()
                }else{
                    dialog(resp.data,it.screen)
                }
            }
        }

        fun onLogin(it: RFormScreenSubmitHandler) {

            val usr = it.formData["usr"]!!
            val pwd = it.formData["pwd"]!!
            IhqClient.send(LoginSPacket(usr, pwd)) { resp ->
                if (resp.ok) {

                    val account = serdesJson.decodeFromString<Account>(resp.data)
                    LocalStorage += "usr" to usr
                    LocalStorage += "pwd" to pwd
                    Account.now = account
                    toastOk("登录成功")
                    mc goScreen RTitleScreen()
                } else {
                    dialog("密码错误", it.screen)
                }
            }
        }
    }


    public override fun init() {
        //SplashScreen.hide()
        val account = Account.now ?: Account.DEFAULT
        //关闭音乐
        mc.options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0)
        /*FabricLoader.getInstance().allMods.forEach {
            var modName = ClientLanguage.getInstance().getOrDefault("itemGroup.${it.metadata.id}", it.metadata.name)
            if(modName == it.metadata.name){
                modName = ClientLanguage.getInstance().getOrDefault("itemGroup.${it.metadata.id}.base", it.metadata.name)
            }
            if(modName == it.metadata.name){
                modName = ClientLanguage.getInstance().getOrDefault("itemGroup.${it.metadata.id}.${it.metadata.id}", it.metadata.name)
            }
            loadedModsIdName += it.metadata.id to modName
        }
        loadedModsIdName += listOf(
            "minecraft" to "原版",
            "ad_astra" to "星之所向",
            "immersive_aircraft" to "拟真飞行器",
            "vinery" to "葡园酒香",
            "createdeco" to "机械动力：装饰",
            "illagerinvasion" to "灾厄入侵",
            "adorn" to "装饰",
        )
        log.info(loadedModsIdName)*/
        RPlayerHeadButton(account) {
            if (account == Account.DEFAULT)
                mc goScreen optScreen(this)
            else
                mc goScreen RProfileScreen(account)
        }.apply {
            x = mcUIWidth / 2 - width / 2
            y = 2
        }.also { registerWidget(it) }
        RGridLayout(mc.window.guiScaledWidth / 2, mcUIHeight - 17).apply {
            row(
                6,
                RTextButton("多人生存") {
                    if (Account.now == null) {

                        mc goScreen optScreen(this@RTitleScreen)
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
                RTextButton("单人创造") {
                    if (shiftMode)
                        mc goScreen SelectWorldScreen(this@RTitleScreen)
                    else
                        openFlatLevel()
                },
                RTextButton("Mod管理") {
                    mc goScreen net.minecraftforge.client.gui.ModListScreen(this@RTitleScreen)
                },
                RTextButton("设置") {
                    mc goScreen OptionsScreen(this@RTitleScreen, mc.options)
                },

                RTextButton("致谢") {
                    alertInfo("服务器硬件：wuhudsm66\n任务设计：terryaxe\nMod建议：ForiLuSa", this@RTitleScreen)
                },
                RTextButton("QQ群") {
                    copyToClipboard("1095925708")
                    alertInfo("已复制QQ群号：1095925708\n欢迎加入RDI玩家交流群！", this@RTitleScreen)
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
        guiGraphics.blit(SCREEN_BG, 0, 0, 0f, 0f, mcUIWidth, mcUIHeight, mcUIWidth, mcUIHeight)
        guiGraphics.blit(LOGO, mcUIWidth / 2 - 100, mcUIHeight / 2 - 20, -0.0625f, 0.0f, 120, 60, 120, 120)
        guiGraphics.blit(LOGO, mcUIWidth / 2 - 30, mcUIHeight / 2 - 20, 0.0625f, 60.0f, 120, 60, 120, 120)

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        //下方的灰条
        guiGraphics.fill(0, height - 20, width, height, 0xAA000000.toInt())
        //drawTextAt(guiGraphics, Const.VERSION_STR, 10, height - 15)
//上方的灰条（已登录
        guiGraphics.fill(width / 2 - 80, 0, width / 2 + 80, 20, 0xAA000000.toInt())
        /*RAccount.now?.let {
            PlayerFaceRenderer.draw(guiGraphics, it.skinLocation, width/2-40, 2, 15)
        }*/


    }

    override fun tick() {
        shiftMode = mc pressingKey InputConstants.KEY_LSHIFT
        ctrlMode = mc pressingKey InputConstants.KEY_LCONTROL
        if(mc pressingKey InputConstants.KEY_Z){
            mc goScreen object : RScreen("文档测试"){
                override fun init() {
                    val widget = docWidget {
                        h1("一级标题一级标题一级标题")
                        h2("二级标题二级标题二级标题")
                        p("正文啊啊啊啊啊啊啊啊啊啊正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                        spacer(5)
                        img("screen_bg")
                        p("正文正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                        p("正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                        p("正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                    }.build()
                    registerWidget(widget)
                    super.init()
                }
            }
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