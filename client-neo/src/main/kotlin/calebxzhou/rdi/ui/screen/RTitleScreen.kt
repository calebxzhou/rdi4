package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.model.RServer
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Difficulty
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.WorldDataConfiguration
import net.minecraft.world.level.levelgen.WorldOptions
import net.minecraft.world.level.levelgen.presets.WorldPresets

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

       /* val ipv6Screen = { prevScreen: RScreen ->
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
        }*/




    }


    public override fun init() {

        mc.level?.let {
            it.disconnect()
            mc.clearLevel()
        }
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
                mc go RServer.serverSelectScreen
            }
            iconButton("ssp", text = "单人模式") {
                mc go SelectWorldScreen(this.screen)
                /*Chapter.ALL.firstOrNull { cpt -> cpt.must && cpt.tutorials.any { !it.isDone } }?.let {
                    alertErr("请先完成教程 ${it.name} 章节")
                    return@imageButton
                }*/
                //start()
            }
            iconButton("tutorial", text = "互动教程") {
                mc go LoadingScreen(this@RTitleScreen)
            //mc go RTutorialScreen(this@RTitleScreen)
                //start()
            }
            iconButton("settings", text = "设置") {
                mc go RSettingsScreen(this@RTitleScreen, mc.options)
            }
            iconButton("partner", text = "关于") {
                mc go AboutScreen()
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
        guiGraphics.blit(LOGO, mcUIWidth / 2 - 60, mcUIHeight / 2 - 25, -0.0625f, 0.0f, 128, 64, 128,64)
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
            mc go SelectWorldScreen(this)
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