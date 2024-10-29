package calebxzhou.rdi.tutorial

import calebxzhou.rdi.blockguide.BlockGuide
import calebxzhou.rdi.blockguide.blockGuide
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.util.*
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
val TUTORIAL_BASIC
    get() = tutorial("basic", "基础操作") {
        val origin = BlockPos(0, -60, 0)
        val goHome = { player: Player ->
            player.teleportTo(0.0, -60.0, 0.0)
            player.xRot = 0f
            player.yRot = 0f
        }
        val isAwayFromOrigin = { player: Player ->
            player.blockPosition().distSqr(origin) > 25
        }
        jump()
        step("按T键打开聊天框", {
            goHome(it)
        }) {
            mc.screen is ChatScreen
        }
        step("输入123456，按下回车(Enter)键，发送消息") {
            mc justChatted "123456"
        }
        step("左右晃动鼠标，将视角转到身后") {
            it.yRot > 170
        }
        step("上下晃动鼠标，将视角转到天空") {
            it.xRot < -85
        }
        step("按W键向前走", { goHome(it) }) {
            isAwayFromOrigin(it)
        }
        step("按A键向左走", { goHome(it) }) {
            isAwayFromOrigin(it)
        }
        step("按S键向后走", { goHome(it) }) {
            isAwayFromOrigin(it)
        }
        step("按D键向右走", { goHome(it) }) {
            isAwayFromOrigin(it)
        }
        step("按住左Ctrl键（在键盘的左下角）不松开，再按住W键，跑步 ", { goHome(it) }) {
            it.isSprinting && isAwayFromOrigin(it)
        }
        step("按住左Shift键（在字母Z的左边）不松开，再按住W键，下蹲/慢走 ", { goHome(it) }) {
            it.isCrouching && isAwayFromOrigin(it)
        }
        step("打开聊天框，发送你现在的水分值",{ alert("画面下方的HP SP WP分别代表生命值 饱食度 水分值\n生命值为0会当场去世\n饱食度过低，干什么都没劲，行动速度减慢\n水分值过低，效果同上") }){
            mc justChatted it.waterPercent.toString()
        }
        step("破坏方块：对准脚下的草方块，按住鼠标左键，挖掉它") {
            it bagHas Items.DIRT
        }
        step("按空格键跳出挖开的坑"){
            it.isJumping
        }
        step("放置方块：对准刚刚挖开的地方，按下鼠标右键，把泥土方块放回去") {
            it isLooking Blocks.DIRT && it.mainHandItem.isEmpty
        }
        step("对准面前的水",{it.level().setBlock(it.blockPosition().below().north(),Blocks.WATER.defaultBlockState())}){
            it isLooking Blocks.WATER
        }
        step("按下鼠标右键喝水，直到画面下方的水分值达到100",{ (it.foodData as TFCFoodData).thirst=80f}){
            (it.foodData as TFCFoodData).thirst>=99f
        }
        step("干掉面前这头几猪（对准-鼠标左键狂点），然后靠近掉落的猪肉，捡起来", {
            repeat(3) { _ ->
                mcs?.executeCommand("summon tfc:pig ${it.blockX} ${it.blockY} ${it.blockZ}")
            }
        }) { it bagHas TFCItems.FOOD[Food.PORK]!!.get() }
        step("转动鼠标滚轮，切换手持物品到第9格") {
            it.inventory.selected == 8
        }
        step("切换手持物品到第1格") {
            it.inventory.selected == 1
        }
        step(
            "一边滚动鼠标滚轮一边按下Q键，逐个丢掉所有的物品（同时按Ctrl+Q键可以丢掉一整组）",
            { p ->
                listOf(
                    Items.STONE_BRICKS,
                    Items.CHISELED_STONE_BRICKS,
                    Items.CRACKED_STONE_BRICKS,
                    Items.MOSSY_STONE_BRICKS
                )
                    .map { it*8 }
                    .forEach { p give it }
            }) {
            it.inventory.isEmpty
        }
        step("将丢掉的物品全捡回来") {
            !it.inventory.isEmpty
        }
        step("将苔石放置在地面上") {
            it isLooking Blocks.MOSSY_STONE_BRICKS
        }
        step("站在苔石上面") {
            it feetOn Blocks.MOSSY_STONE_BRICKS
        }
        step("跳起来的同时，把 裂纹石砖 放在脚下") {
            it feetOn Blocks.CRACKED_STONE_BRICKS
        }
        step("同样的方法放置 雕纹石砖 ") {
            it feetOn Blocks.CHISELED_STONE_BRICKS
        }
        step("最后一个石砖也一样") {
            it feetOn Blocks.STONE_BRICKS
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
        step("手持石镐，长按鼠标左键毁掉这四块石砖",{it give Items.STONE_PICKAXE}) {
            it bagHas  Items.MOSSY_STONE_BRICKS*8
        }
        step("按E键打开背包", { it give TFCItems.STRAW.get() * 8 }) { mc.screen is InventoryScreen }
        step("在画面右下角的文本框中，输入汉字 干草块，然后点击画面右侧出现的干草块，查看它的合成方式") {
            mc.screen is IRecipesGui
        }
        step("记住这个配方图案，按ESC回到背包画面") {
            mc.screen is InventoryScreen
        }
        step("点击干草，按鼠标右键平分干草，按照刚刚记住的图案，在画面中间偏右的“合成”区域摆放干草，合成两个干草块"){
            it bagHas (TFCBlocks.THATCH.get().asItem()*2)
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
        selfChk("右键点击兽皮床，设置复活点")
    }