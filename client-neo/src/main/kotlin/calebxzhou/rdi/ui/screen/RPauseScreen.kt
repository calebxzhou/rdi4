package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.lang.MultiLangStorage
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.model.RServer
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.tutorial.Tutorial
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.ui.screen.RPauseScreen.DisplayMode.BASIC_INFO
import calebxzhou.rdi.util.*
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import xaero.map.WorldMapSession
import xaero.map.gui.GuiMap

class RPauseScreen : RScreen("暂停") {
    override var showTitle = false
    private var displayMode = BASIC_INFO

    private enum class DisplayMode {
        BASIC_INFO, MAP, WIKI
    }

    val lookingBlockState = mc.player?.lookingAtBlock

    lateinit var xaeroMap: GuiMap
    override fun init() {
        if (mc.player == null) return
        xaeroMap =
            GuiMap(this@RPauseScreen, this@RPauseScreen, WorldMapSession.getCurrentSession().mapProcessor, mc.player!!)
        // xaeroMap.init(mc,600,600)
        gridLayout(this, hAlign = HAlign.CENTER, y=mcUIHeight-20) {
            button("navi",text="方块导航"){
                mc go BlockSelectScreen(this@RPauseScreen)
            }
        }
        gridLayout(this, hAlign = HAlign.CENTER, y = 2) {
            /*imageButton("basic_info","基本信息"){
                displayMode = BASIC_INFO
            }*/
            button("map", text = "地图") {
                mc go xaeroMap
            }
            button("camera", text = "视野") {
                mc go FovScreen()
            }
            button("settings", text = "设置") {
                mc go RSettingsScreen(this@RPauseScreen, mc.options)
            }
            button("exit", text = "退出") {
                Banner.reset()
                OmniNavi.reset()
                mc.level?.disconnect()
                if (mc.isLocalServer) {
                    mc.clearLevel(GenericDirtMessageScreen(mcText("存档中，请稍候")))
                    Tutorial.now?.quit()
                    mc.goHome()
                } else {
                    mc.clearLevel()
                    RAccount.now?.let { ac ->
                        RServer.now?.let { sv ->
                            mc go RProfileScreen(ac, sv)
                        }
                    } ?: let {
                        alert("账号信息为空，即将回到主页")
                        mc.goHome()
                    }
                }


            }
        }

        lookingBlockState?.let { blockState ->
            val block = blockState.block
            val item = block.asItem()
            val itemStack = item.defaultInstance
            val ofLangKey = { key: String, lang: String -> MultiLangStorage[key]?.get(lang) }
            gridLayout(this, x = 10, y = 40, colSpacing = -20) {
                button(
                    item = item, comp =
                        item.chineseName
                                + " "
                                + mcText(item.id.toString())
                )
                listOf(
                    "en_us" to "英",
                    "zh_tw" to "繁中",
                    "ja_jp" to "日",
                    "ko_kr" to "韩",
                    "fr_fr" to "法",
                    "de_de" to "德",
                    "ru_ru" to "俄",
                    "it_it" to "意"
                )
                    .forEach {
                        ofLangKey(item.descriptionId, it.first)?.let { langVal ->
                            button(
                                hoverText = it.second,
                                comp = langVal.toMcText().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
                            )
                        }
                    }

            }
        }
        super.init()
    }

    //   iconButton(item = Items.NAME_TAG, text = "标签 ${blockState.tags.toArray().size}x方/${item.builtInRegistryHolder().tags().toArray().size}x物", hoverText = "方块标签/物品标签")
    override fun doRender(gg: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        var x = 10
        var y = 40

        /*lookingBlockState?.let { blockState ->
            val block = blockState.block
            val item = block.asItem()
            val itemStack = item.defaultInstance
            gg.renderItemStack(x,y,itemStack)
            x += 20
            gg.drawString(x, y, )
            y+=16
            gg.renderItemStack(x,y, .defaultInstance)
            x+=18
            val tags  = arrayListOf<String>()
            blockState.tags.map { "#"+it.location.toString() }.forEach{tags += it}
            val tagWidget = MultiLineTextWidget(x, y, tags.joinToString("  ").toMcText(), mcFont)
            tagWidget.setMaxWidth(mcUIWidth/2)
            tagWidget.render(gg,mouseX,mouseY,partialTick)

        }*/
        /*if(Tutorial.isDoingTutorial){
            drawTextAtCenter(guiGraphics,"入门教程模式")
        }*/
        /*guiGraphics.matrixOp {
            scale(0.5f,0.5f,1f)
            xaeroMap.render(guiGraphics,mouseX,mouseY,partialTick)
        }*/
        /* when(displayMode){
             BASIC_INFO -> {
             }
             MAP -> {

             }
             WIKI -> {

             }
         }*/
    }



}