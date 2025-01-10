package calebxzhou.rdi.ui.screen

import calebxzhou.rdi.banner.Banner
import calebxzhou.rdi.common.WHITE
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.tutorial.Tutorial
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.general.HAlign
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.ui.layout.gridLayout
import calebxzhou.rdi.ui.screen.RPauseScreen.DisplayMode.*
import calebxzhou.rdi.util.*
import net.dries007.tfc.client.ClimateRenderCache
import net.dries007.tfc.client.screen.NutritionScreen
import net.dries007.tfc.common.capabilities.food.Nutrient
import net.dries007.tfc.common.capabilities.food.NutritionData
import net.dries007.tfc.common.capabilities.food.TFCFoodData
import net.dries007.tfc.config.TFCConfig
import net.dries007.tfc.util.Helpers
import net.dries007.tfc.util.calendar.Calendars
import net.dries007.tfc.util.calendar.ICalendar
import net.dries007.tfc.util.calendar.Month
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.GenericDirtMessageScreen
import net.minecraft.client.resources.language.I18n
import xaero.map.WorldMapSession

class RPauseScreen : RScreen("暂停") {
    override var showTitle = false
    private var displayMode = BASIC_INFO
    private enum class DisplayMode{
        BASIC_INFO,MAP,WIKI
    }
    override fun init() {
        gridLayout(this, hAlign = HAlign.CENTER,y=2) {
            /*imageButton("basic_info","基本信息"){
                displayMode = BASIC_INFO
            }*/
            iconButton("map", text = "地图"){
                mc goScreen xaero.map.gui.GuiMap(this@RPauseScreen,this@RPauseScreen,WorldMapSession.getCurrentSession().mapProcessor,mc.player!!)
            }
            iconButton("camera", text = "视野") {
                mc goScreen FovScreen()
            }
            iconButton("settings",text = "设置"){
                mc goScreen RSettingsScreen(this@RPauseScreen,mc.options)
            }
            iconButton("exit",text = "退出"){
                Banner.reset()
                OmniNavi.reset()
                mc.level?.disconnect()
                if(mc.isLocalServer){
                    mc.clearLevel(GenericDirtMessageScreen(mcText("存档中，请稍候")))
                    Tutorial.now?.quit()
                    mc.goHome()
                } else{
                    mc.clearLevel()
                    RAccount.now?.let {
                        mc goScreen RProfileScreen(it)
                    }?:let {
                        alert("账号信息为空，即将回到主页")
                        mc.goHome()
                    }
                }



            }
        }

        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        /*if(Tutorial.isDoingTutorial){
            drawTextAtCenter(guiGraphics,"入门教程模式")
        }*/
        when(displayMode){
            BASIC_INFO -> {
                renderClimate(guiGraphics)
                renderNutrition(guiGraphics)
                renderCalendar(guiGraphics)
            }
            MAP -> {

            }
            WIKI -> {

            }
        }
    }

    private fun renderCalendar(guiGraphics: GuiGraphics) {
        val startX = 20
        val startY = 20
        val season =  I18n.get(
                Calendars.CLIENT.calendarMonthOfYear.getTranslationKey(Month.Style.SEASON)
            )
        val day =  Calendars.CLIENT.calendarDayOfYear.string
        val ticks = Calendars.CLIENT.calendarTicks
        val monthDayAmount = Calendars.CLIENT.calendarDaysInMonth
        val hh = ICalendar.getHourOfDay(ticks)
        val mm = ICalendar.getMinuteOfHour(ticks)
        val MM = ((ticks / (monthDayAmount * ICalendar.TICKS_IN_DAY)) % ICalendar.MONTHS_IN_YEAR).toInt()+1
        val dd = ICalendar.getDayOfMonth(ticks, monthDayAmount.toLong())
        val yy = ICalendar.getTotalYears(ticks, monthDayAmount.toLong())
        guiGraphics.drawString(mcFont,"${yy}年${MM}月${dd}日 $day ${season} ${hh}:${mm}",startX,startY, WHITE)

    }

    private fun renderNutrition(guiGraphics: GuiGraphics) {
        val startX = 20
        val startY = 42
        val data = mc.player?.foodData
        if (data is TFCFoodData) {
            val nutrition: NutritionData = data.nutrition
            for (nutrient in Nutrient.VALUES) {
                val nutVal = nutrition.getNutrient(nutrient)
                val width = (nutVal * 50).toInt()
                val y = startY + 21 + 13 * nutrient.ordinal
                val text = Helpers.translateEnum(nutrient).withStyle(nutrient.color) + mcText(" ${(nutVal * 100).toInt()}%")
                guiGraphics.drawString(mcFont,text,startX,y, WHITE)
                guiGraphics.blit(NutritionScreen.TEXTURE, startX+47, y+2, 176, 0, width, 5)
            }
        }
    }

    private fun renderClimate(guiGraphics: GuiGraphics) {
        // Climate at the current player
        val startX = 20
        val startY = 32
        val averageTemp = ClimateRenderCache.INSTANCE.averageTemperature
        val rainfall = ClimateRenderCache.INSTANCE.rainfall.toFixed(1)
        val currentTemp = ClimateRenderCache.INSTANCE.temperature
        val style = TFCConfig.CLIENT.climateTooltipStyle.get()
        guiGraphics.drawString(mcFont,
            mcText(
            "温度 当前")+style.format(currentTemp, true)!!+ mcText(" 平均")+style.format(averageTemp, true)!!,startX,startY,
            WHITE
        )
        guiGraphics.drawString(mcFont,
            mcText(
            "降水 ${rainfall}mm"),startX,startY+12,
            WHITE
        )

    }
}