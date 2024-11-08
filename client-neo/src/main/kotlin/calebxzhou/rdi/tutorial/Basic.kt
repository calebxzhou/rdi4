package calebxzhou.rdi.tutorial

import calebxzhou.rdi.blockguide.BlockGuide
import calebxzhou.rdi.blockguide.blockGuide
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.platform.InputConstants
import mezz.jei.api.runtime.IRecipesGui
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.capabilities.food.TFCFoodData
import net.dries007.tfc.common.items.Food
import net.dries007.tfc.common.items.HideItemType
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

/**
 * calebxzhou @ 2024-10-22 16:32
 */
val BASIC
    get() = tutorial("basic", "基础操作",false,Items.ACACIA_DOOR) {
        var origin = BlockPos(0, -60, 0)

        step("按空格键跳跃 (键盘下面的大长条按钮,字母CVBNM底下)",{
            alert("在这个像素方块世界里，你要收集材料生存下去\n制作各种工具，挖矿，建设属于自己的家")
            origin = it.blockPosition()
        }){
            mc pressingKey InputConstants.KEY_SPACE
        }
        step("按T键打开聊天框") {
            mc.screen is ChatScreen
        }
        step("输入123456，按下回车(Enter)键，发送消息") {
            mc justChatted "123456"
        }
        step("和其他3D电脑游戏一样，按WASD键走路，鼠标控制视角。\n往前走远一些") {
            it.blockPosition().distSqr(origin) > 100
        }
        step("按下Ctrl+W键跑步 ") {
            it.isSprinting
        }
        step("按下Shift+W键下蹲/潜行",) {
            it.isCrouching
        }
        step("打开聊天框，发送你现在的水分值",{ alert("画面下方的HP SP WP分别代表生命 饱食 水分\n生命值为0会当场去世\n饱食 水分过低，干什么都没劲，行动速度减慢") }){
            mc justChatted it.waterPercent.toString()
        }
        step("对准脚下的方块，按住鼠标左键，挖掉一个") {
            !it.inventory.isEmpty
        }
        step("按鼠标右键，把圆石放在地上",{
            it give Blocks.COBBLESTONE.asItem()
        }) {
            it isLooking Blocks.COBBLESTONE
        }
        step("对准面前的水",{it.level().setBlock(it.blockPosition().below().north(),Blocks.WATER.defaultBlockState())}){
            it isLooking Blocks.WATER
        }
        step("按下鼠标右键喝水，直到画面下方的水分值达到100",{ (it.foodData as TFCFoodData).thirst=80f}){
            (it.foodData as TFCFoodData).thirst>=99f
        }

        step("按E键打开背包"){
            mc.screen is InventoryScreen
        }
        step("将鼠标放在猪肉上，按下shift键，查看蛋白质含量，记住数值，按ESC关闭背包"){
            mc.screen==null
        }
        step("在聊天框中发送猪肉的蛋白质含量"){
            mc justChatted "1.5"
        }
        step("手持猪肉，长按鼠标右键吃掉", { it.foodData.foodLevel = 16 }) {
            !it.foodData.needsFood()
        }
        step("按E键打开背包", { it give TFCItems.STRAW.get() * 8 }) { mc.screen is InventoryScreen }
        step("在画面右下角的文本框中，输入干草块，查看它的合成方式") {
            mc.screen is IRecipesGui
        }
        step("记住这个配方图案，按ESC回到背包画面") {
            mc.screen is InventoryScreen
        }
        step("合成两个干草块"){
            it bagHas (TFCBlocks.THATCH.get().asItem() * 2)
        }
        step("手持两个干草块"){
            it handHas (TFCBlocks.THATCH.get().asItem() * 2)
        }
        step("将两个干草块呈一字型放在地上",{
            blockGuide {
                place(it.blockPosition(),TFCBlocks.THATCH.get())
                place(it.blockPosition().north(),TFCBlocks.THATCH.get())
            }
        }){
            BlockGuide.isOff
        }
        val hide = TFCItems.HIDES[HideItemType.RAW]!![HideItemType.Size.LARGE]!!.get()
        step("手持大块兽皮",{it.inventory.add(hide.defaultInstance)}){
            it handHas hide
        }
        step("右键点击干草块，得到兽皮床") {
            it.lookingAtBlock?.`is`(TFCBlocks.THATCH_BED.get()) == true
        }
        step("右键点击兽皮床，设置复活点"){
            it.respawnPosition != origin
        }
    }