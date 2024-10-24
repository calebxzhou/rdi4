package calebxzhou.rdi.tutorial

import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.util.*
import mezz.jei.api.runtime.IRecipesGui
import net.dries007.tfc.client.screen.FirepitScreen
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.TFCTags.Items.ROCK_KNAPPING
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.devices.PitKilnBlock
import net.dries007.tfc.common.blocks.plant.Plant
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.dries007.tfc.common.blocks.rock.Rock
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.items.Food
import net.dries007.tfc.common.items.HideItemType
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
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
            val border = it.level().worldBorder
            border.setCenter(0.0, 0.0)
            border.size = 64.0
        }) {
            mc.screen is ChatScreen
        }
        step("输入123456，按下回车(Enter)键，发送消息") {
            mc.screen == null
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
        step("破坏方块：对准脚下的草方块，按住鼠标左键，挖掉它，然后按空格键跳出挖开的坑") {
            it bagHas Items.DIRT
        }
        step("放置方块：对准刚刚挖开的地方，按下鼠标右键，把泥土方块放回去") {
            it isLooking Blocks.DIRT && it.mainHandItem.isEmpty
        }
        step("干掉面前这头几猪（对准-鼠标左键狂点），然后靠近掉落的猪肉，捡起来", {
            repeat(3) { _ ->
                mcs?.executeCommand("summon tfc:pig ${it.blockX} ${it.blockY} ${it.blockZ}")
            }
        }) { it bagHas TFCItems.FOOD[Food.PORK]!!.get() }
        step("转动鼠标滚轮，切换手持物品到第9格") {
            it.inventory.selected == 8
        }
        step(
            "切换回第1格，按Q键丢掉物品。一边滚动鼠标滚轮一边按下Q键，逐个丢掉所有的物品（同时按Ctrl+Q键可以丢掉一整组）",
            { p ->
                listOf(
                    Items.STONE_BRICKS,
                    Items.CHISELED_STONE_BRICKS,
                    Items.CRACKED_STONE_BRICKS,
                    Items.MOSSY_STONE_BRICKS
                )
                    .map { it by 8 }
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
        step("手持猪肉，长按鼠标右键吃掉", { it.foodData.foodLevel = 16 }) {
            !it.foodData.needsFood()
        }
        step("手持石镐，长按鼠标左键毁掉这四块石砖",{it give Items.STONE_PICKAXE}) {
            it bagHas (Items.MOSSY_STONE_BRICKS by 8)
        }
        step("按E键打开背包", { it give (TFCItems.STRAW.get() by 8) }) { mc.screen is InventoryScreen }
        step("鼠标右键点击干草，把它平分为两组"){ it bagHas (TFCItems.STRAW.get() by 4)}
        step("鼠标左键点击干草，按住左键，在没有物品的槽位上滑动，把它平铺在空槽中"){ it bagHas (TFCItems.STRAW.get() by 1)}
        step("在画面右下角的文本框中，输入汉字 干草块，然后点击画面右侧出现的干草块，查看它的合成方式") {
            mc.screen is IRecipesGui
        }
        step("记住这个配方图案，按ESC回到背包画面") {
            mc.screen is InventoryScreen
        }
        step("按照刚刚记住的图案，在画面中间偏右的“合成”区域摆放干草，合成两个干草块"){
            it bagHas (TFCBlocks.THATCH.get().asItem() by 2)
        }
    }