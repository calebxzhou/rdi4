package calebxzhou.rdi.ui

import calebxzhou.rdi.MOD_ID
import calebxzhou.rdi.lang.EnglishStorage
import calebxzhou.rdi.service.NetMetrics
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.dries007.tfc.client.IngameOverlays
import net.dries007.tfc.common.capabilities.food.TFCFoodData
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.client.gui.overlay.ForgeGui
import net.minecraftforge.client.gui.overlay.IGuiOverlay
import java.awt.Color
import kotlin.math.roundToInt

object RGui {
    val MC_ICONS = ResourceLocation("textures/gui/icons.png")
    val THIRST_ICONS = ResourceLocation(MOD_ID, "textures/gui/thirst_icons.png")
    private fun setupForSurvival(gui: ForgeGui, minecraft: Minecraft): Boolean {
        return gui.shouldDrawSurvivalElements() && IngameOverlays.setup(gui, minecraft)
    }
    @JvmStatic
    fun renderSelectedItemEnglishName(
        guiGraphics: GuiGraphics,
        langKey: String,
        cnX: Int,
        cnY: Int,
        alpha: Int
    ){
        val color = 0xaaaaaa + (alpha shl 24)
        val comp = EnglishStorage[langKey].toMcText().withStyle(ChatFormatting.ITALIC)
        guiGraphics.drawString(mcFont,comp, (mcUIWidth - mcFont.width(comp))/2,cnY+9, color)
    }
    @JvmStatic
    fun renderHealthBar(entity: LivingEntity, gui: ForgeGui, graphics: GuiGraphics, width: Int, height: Int) {


        val stack = graphics.pose()
        val maxHealth = entity.maxHealth

        val centerX = width / 2
        val y = height - gui.leftHeight
        var absorption = entity.absorptionAmount
        absorption = if (java.lang.Float.isNaN(absorption)) 0f else absorption
        var percentHealth = (entity.health + absorption) / entity.maxHealth
        val currentHealth = percentHealth * maxHealth
        percentHealth = Mth.clamp(percentHealth, 0f, 1f)
        /*stack.pushPose()
        stack.translate((centerX - 91).toFloat(), y.toFloat(), 0f)
        //graphics.blit(IngameOverlays.TEXTURE, 0, 0, 0, 0, 90, 10)




        graphics.blit(IngameOverlays.TEXTURE, 0, 0, 0, 10, (90 * percentHealth).toInt(), 10)

        val isHurt = entity.health > 0.0f && entity.health < entity.maxHealth
        val playerHasSaturation = entity is Player && entity.foodData.saturationLevel > 0
        if ((playerHasSaturation && isHurt) || (entity.hurtTime > 0) || entity.hasEffect(MobEffects.REGENERATION)) {
            graphics.blit(IngameOverlays.TEXTURE, 0, 1, 0, 30, 90, 8)
        }

        val surplusPercent = Mth.clamp(percentHealth + (absorption / 20f) - 1, 0f, 1f)
        if (surplusPercent > 0) {
            // fill up the yellow bar until you get a second full bar, then just fill it up
            val percent = min(surplusPercent.toDouble(), 1.0).toFloat()
            graphics.blit(IngameOverlays.TEXTURE, 0, 0, 90, 10, (90 * percent).toInt(), 10)
        }
        stack.popPose()*/

        // Health modifier affects both max and current health equally. All we do is draw different numbers as a result.
        val healthModifier =
            if (entity is Player && entity.foodData is TFCFoodData) (entity.foodData as TFCFoodData).getHealthModifier() else 1f

        val text = mcText("HP" + (percentHealth * 100).roundToInt())
        stack.pushPose()
        stack.translate((centerX - 45).toDouble(), y + 2.5, 0.0)
        //stack.scale(0.8f, 0.8f, 1.0f)
        graphics.blit(MC_ICONS, -45, 0, 52, 0, 9, 9)

        graphics.drawString(
            gui.font,
            text,
            -35,
            0,
            Color.WHITE.rgb,
            true
        )
        stack.popPose()
    }

    @JvmStatic
    fun renderFood(gui: ForgeGui, graphics: GuiGraphics, partialTicks: Float, width: Int, height: Int) {
        val stack = graphics.pose()
        val minecraft = Minecraft.getInstance()
        if (setupForSurvival(gui, minecraft)) {
            val player = checkNotNull(minecraft.getCameraEntity() as Player?)
            val x = width / 2 - 17
            val y = height - gui.leftHeight+2
            val percentFood = player.foodData.foodLevel.toFloat() / TFCFoodData.MAX_HUNGER

            stack.pushPose()
            stack.translate(x.toFloat(), y.toFloat(), 0f)
            graphics.drawString(
                gui.font,
                "SP${(percentFood * 100).roundToInt()}",
                -35,
                0,
                Color.WHITE.rgb,
                true
            )
            graphics.blit(MC_ICONS, -45, 0, 52, 27, 9, 9)
            stack.popPose()

        }
    }

    @JvmStatic
    fun renderThirst(gui: ForgeGui, graphics: GuiGraphics, partialTicks: Float, width: Int, height: Int) {
        val stack = graphics.pose()
        val minecraft = Minecraft.getInstance()
        if (setupForSurvival(gui, minecraft)) {
            val player = checkNotNull(minecraft.getCameraEntity() as Player?)
            val x = width / 2
            val y = height - gui.rightHeight
            var percentThirst = 0f
            var overheat = 0f
            val data = player.foodData
            if (data is TFCFoodData) {
                percentThirst = data.thirst / TFCFoodData.MAX_THIRST
                overheat = data.getThirstContributionFromTemperature(player)
            }

            stack.pushPose()
            stack.translate((x + 10).toFloat(), (y + 2).toFloat(), 0f)
            val comp = mcText("WP${player.waterPercent}")
            /*  if (overheat > 0) {
                  comp + mcText(" ↓${String.format("%.2f",1+overheat)}x")
              }*/
            graphics.drawString(
                gui.font,
                comp,
                -35,
                0,
                Color.WHITE.rgb,
                true
            )
            graphics.blit(THIRST_ICONS, -45, 0, 16F, 0f, 9, 9, 25, 9)
            stack.popPose()

        }
    }
    @JvmStatic
    fun renderXp(){

    }
    object Armor: IGuiOverlay{
        val ID = "overlay/armor"
        override fun render(
            gui: ForgeGui,
            guiGraphics: GuiGraphics,
            partialTick: Float,
            screenWidth: Int,
            screenHeight: Int
        ) {
            val armor = mc.player?.armorValue ?: 0
            //护甲值太低就不显示
            if(armor<1)
                return
            val x = screenWidth / 2
            val y = screenHeight - gui.rightHeight
            val stack = guiGraphics.pose()
            stack.pushPose()
            stack.translate((x + 10).toFloat(), (y + 2).toFloat(), 0f)
            //乘5为了显示满100 原版满20
            val comp = mcText("AP${armor *5}")
            guiGraphics.drawString(
                gui.font,
                comp,
                10,
                0,
                Color.WHITE.rgb,
                false
            )
            guiGraphics.blit(MC_ICONS, 0,0, 34, 9, 9, 9)
            stack.popPose()
        }

    }
    object Network: IGuiOverlay{
        val ID = "overlay/network"
        override fun render(
            gui: ForgeGui,
            guiGraphics: GuiGraphics,
            partialTick: Float,
            screenWidth: Int,
            screenHeight: Int
        ) {
            NetMetrics.render(guiGraphics);
        }

    }


}