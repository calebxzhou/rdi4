package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.account.LoginSPacket
import calebxzhou.rdi.ihq.protocol.account.RegisterSPacket
import calebxzhou.rdi.model.Account
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.text.richText
import calebxzhou.rdi.tutorial.Chapter
import calebxzhou.rdi.tutorial.T1_BUILD
import calebxzhou.rdi.tutorial.T1_CERA
import calebxzhou.rdi.ui.component.*
import calebxzhou.rdi.ui.general.*
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Difficulty
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraft.world.level.levelgen.presets.WorldPresets
import java.net.InetAddress

class RTitleScreen : RScreen("主页") {
    override var showTitle = false
    override var closeable = false
    val LOGO: ResourceLocation =
        ResourceLocation("rdi", "textures/logo.png")

    val SCREEN_BG: ResourceLocation =
        ResourceLocation("rdi", "textures/screen_bg.png")
    var shiftMode = false
    var ctrlMode = false

    companion object {
        val optScreen = { prevScreen: RScreen ->
            optionScreen(prevScreen) {
                "注册新账号" to {
                    mc goScreen regScreen(it.mcScreen)
                }
                "登录已有账号" to {
                    mc goScreen loginScreen(it.mcScreen)
                }
            }
        }
        val ipv6Screen = { prevScreen: RScreen ->
            optionScreen(prevScreen, title = "当前网络不支持IPv6，选择网络类型，查看解决方案") {
                "家庭宽带" to {
                    alert("把网线/wifi连在运营商给的光猫上\n或者在路由器设置中启用")
                }
                "校园网、公司" to {
                    alert("问问网管，不行的话拿手机流量开热点")
                }
                "视频教程" to {
                    openLink("https://www.bilibili.com/video/BV1oy4y1Y7Eb")
                }
            }
        }
        val loginScreen =
            { prevScreen: RScreen ->
                formScreen(prevScreen, "登录账号") {
                    text("usr", "QQ号/昵称/ID", 16, defaultValue = LocalStorage["usr"])
                    pwd("pwd", "密码", defaultValue = LocalStorage["pwd"])
                    submit {
                        onLogin(it)
                    }
                }
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

            }
        }

        val lanScreen = { prevScreen: RScreen ->
            formScreen(prevScreen,"输入信息"){
                text("name","你的游戏昵称",16, defaultValue = LocalStorage["guestName"])
                submit {
                    val name = it.formData["name"]!!
                    LocalStorage["guestName"]=name
                    Account.guestLogin(name)
                    mc goScreen JoinMultiplayerScreen(RTitleScreen())
                    alert("0.让你的朋友打开一个存档，输入/lan指令启动联机\n1.然后这个界面，就能搜到他了\n如果搜不到，可以手动输入他的ip跟端口添加")
                }
            }
        }

        fun onRegister(it: RFormScreenSubmitHandler) {
            val pwd = it.formData["pwd"]!!
            val cpwd = it.formData["cpwd"]!!
            if (pwd != cpwd) {
                alertOs("确认密码与密码不一致")
                return
            }
            val qq = it.formData["qq"]!!
            val name = it.formData["name"]!!

            IhqClient.send(RegisterSPacket(name, pwd, qq)) { resp ->
                if (resp.ok) {
                    toastOk("注册成功")
                    LocalStorage += "usr" to qq
                    LocalStorage += "pwd" to pwd
                    mc goScreen RTitleScreen()
                } else {
                    alertOs(resp.data)
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
                    RSoundPlayer.stopAll()
                    mcMainThread {

                    ConnectScreen.startConnecting(
                        it.screen,
                        mc,
                        ServerAddress(Const.SERVER_ADDR, Const.SERVER_PORT),
                        Const.SERVER_DATA,
                        false
                    )
                    }
                } else {
                    alertOs("密码错误")
                }
            }
        }
    }


    public override fun init() {

        Account.logout()
        //关闭音乐
        //mc.options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0)


        /*日后启用
        RPlayerHeadButton(account) {
            if (account == Account.DEFAULT)
                mc goScreen optScreen(this)
            else
                mc goScreen RProfileScreen(account)
        }.apply {
            val accountNameWidth = mcFont.width(Account.now?.name?: Account.DEFAULT.name)
            x = mcUIWidth - (16 + accountNameWidth + 6)
            y = mcUIHeight- 17
        }.also { registerWidget(it) }*/
        gridLayout(this, 10, mcUIHeight - 16) {
            iconButton("smp", text = "多人模式"){
                mc goScreen optionScreen(this.screen, "选择联机类型") {
                    "局域网" to lanScreen(this.mcScreen)
                    //"私服" to { alert("私服模式预计2025.1中开通") }
                    "线上模式" to {

                        if(!supportIPv6){

                            mc goScreen ipv6Screen(this.mcScreen)
                        }else{
                            mc goScreen optScreen(this.mcScreen)
                        }
                    }

                }
            }
            iconButton("ssp", text = "单人模式") {
                mc goScreen SelectWorldScreen(this.screen)
                /*Chapter.ALL.firstOrNull { cpt -> cpt.must && cpt.tutorials.any { !it.isDone } }?.let {
                    alertErr("请先完成教程 ${it.name} 章节")
                    return@imageButton
                }*/
                //start()
            }
            iconButton("tutorial", text = "互动教程") {
                mc goScreen RTutorialScreen(this@RTitleScreen)
                //start()
            }
            iconButton("settings", text = "设置") {
                mc goScreen RSettingsScreen(this@RTitleScreen, mc.options)
            }
            iconButton("partner", text = "关于") {
                mc goScreen AboutScreen()
            }
        }


        super.init()
    }

    fun start() {
        /*if (Account.now == null) {
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
        }*/
        //TUTORIAL_PRIMARY.start()
        /*if (!File("tutorial1_done").exists()) {
            Tutorial.stoneAge[0].start()
        } else
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
            }*/
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
        guiGraphics.blit(LOGO, mcUIWidth / 2 - 60, mcUIHeight / 2 - 25, -0.0625f, 0.0f, 120, 60, 120, 120)
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        //下方的灰条
        guiGraphics.fill(0, height - 20, width, height, 0xAA000000.toInt())
        //richtext.render(guiGraphics)
        //drawTextAt(guiGraphics, Const.VERSION_STR, 10, height - 15)
//上方的灰条（已登录
        /*RAccount.now?.let {
            PlayerFaceRenderer.draw(guiGraphics, it.skinLocation, width/2-40, 2, 15)
        }*/


    }

    override fun tick() {
        shiftMode = mc pressingKey InputConstants.KEY_LSHIFT
        ctrlMode = mc pressingKey InputConstants.KEY_LCONTROL
        /*if (mc pressingKey InputConstants.KEY_Z) {
            mc goScreen object : RScreen("文档测试") {
                override fun init() {

                    val widget = docWidget {
                        h1("一级标题一级标题一级标题")
                        h2("二级标题二级标题二级标题")
                        p("正文啊啊啊啊啊啊啊啊啊啊正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                        spacer(5)
                        img("gui/title/mojangstudios")
                        items(
                            ItemStack(Items.ENCHANTED_BOOK, 16),
                            ItemStack(Items.DARK_OAK_DOOR, 16),
                            ItemStack(Items.GRASS_BLOCK, 16),
                            ItemStack(Items.GRASS_BLOCK, 16),
                            ItemStack(Items.GRASS_BLOCK, 16),
                        )
                        p("正文正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                        p("正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                        p("正文啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦")
                    }.build()
                    registerWidget(widget)
                    super.init()
                }
            }
        }*/
        /* if (mc pressingKey InputConstants.KEY_RETURN || mc pressingKey InputConstants.KEY_NUMPADENTER) {
             start()
         }*/
        if (mc pressingKey InputConstants.KEY_0) {
            mc goScreen SelectWorldScreen(this)
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