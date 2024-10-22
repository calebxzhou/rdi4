package calebxzhou.rdi.tutorial

import calebxzhou.rdi.nav.OmniNavi
import calebxzhou.rdi.ui.RScreenRectTip.Mode
import calebxzhou.rdi.util.*
import net.dries007.tfc.client.screen.FirepitScreen
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.TFCTags.Items.ROCK_KNAPPING
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.devices.PitKilnBlock
import net.dries007.tfc.common.blocks.plant.Plant
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.dries007.tfc.common.blocks.rock.Rock
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.items.TFCItems
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
val BASIC_TUTORIAL
    get() = tutorial("basic", "基础操作") {
        val originBlock = BlockPos(0, -60, 5)
        val goHome = { player: Player -> player.teleportTo(0.0, -60.0, 0.0) }
        jump()
        step("这是一个自由创造的生存型游戏。收集材料，在这个虚拟世界活下去，建造自己的家。\n" +
                "按WASD键 前后左右走路，晃动鼠标改变视角，走到光柱位置",
            {
                goHome(it)
                val border = it.level().worldBorder
                border.setCenter(0.0, 0.0)
                border.size = 64.0
                it.level().setBlock(originBlock, Blocks.RED_WOOL.defaultBlockState(), 2)
                OmniNavi += originBlock
            }) { !OmniNavi.isOn }
        step("将画面中心的十字 对准这块红色羊毛，长按鼠标左键破坏它，收入囊中") { player ->
            player.inventory.hasAnyMatching {
                it.`is`(Items.RED_WOOL)
            }
        }
        step("手持羊毛，对准地面，按下鼠标右键，把它放在地上") { player ->
            player.lookingAtBlock?.`is`(Blocks.RED_WOOL) == true
        }
        step("转动鼠标滚轮，切换手持物品，用打火石对准红色羊毛，按下鼠标右键点燃", { player: Player ->
            player.inventory.add(9, Items.FLINT_AND_STEEL.defaultInstance)
        }) {
            it.lookingAtBlock?.`is`(Blocks.FIRE) == true
        }
        step("连续按两下W键，跑步"){
            it.isSprinting
        }
        step("按下左Shift键，下蹲/慢走 （在字母Z的左边）"){
            it.isCrouching
        }
        step("面前出现了几个石头，依次对准它们 按下鼠标右键 全部捡起", { player: Player ->
            goHome(player)
            val rock = TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().defaultBlockState()
            val level = player.level()
            level.setBlock(originBlock, rock)
            rock.setValue(LooseRockBlock.COUNT, 2)
            level.setBlock(originBlock.north(), rock)
            level.setBlock(originBlock.south(), rock)
            rock.setValue(LooseRockBlock.COUNT, 3)
            level.setBlock(originBlock.east(), rock)
            level.setBlock(originBlock.west(), rock)
        }) { player ->
            player.inventory.hasAnyMatching { it.`is`(ROCK_KNAPPING) && it.count >= 11 }
        }
        //树枝
        step("面前又出现了一棵树，用同样的方法，捡起树下 全部的树枝", {
            (it.level() as ServerLevel).loadStructure("maple_tree", originBlock)
        }) { player -> player.inventory.hasAnyMatching { it.`is`(FIREPIT_STICKS) && it.count >= 14 } }
        //打磨石头
        //step("",{}){}
        step("手持石头 看天空，按鼠标右键开始打磨") { mc.screen is KnappingScreen }
        //做斧子
        tip("依次点击绿框,打磨石斧头部") {
            widgets(0, 10, 15, 20, 21, 23, 24, 19, 14, 4)
            slot(0)
            emptyInvSlotContainer()
        }
        esc()
        step("按E键打开背包") { mc.screen is InventoryScreen }
        tip("依次点击绿框,合成石斧") {
            slot { it.item.`is`(FIREPIT_STICKS) }
            slot(3)
            slot { it.item.toString().contains("axe_head") }
            slot(1)
            slot(0)
            emptyInvSlot()
        }
        step("手持石斧 对准树的根部，长按鼠标左键砍树，然后捡起掉落的木头") { player ->
            player.inventory.hasAnyMatching {
                it.`is`(
                    ItemTags.LOGS
                ) && it.count >= 5
            }
        }
        step("按E键打开背包") { mc.screen is InventoryScreen }
        tip("鼠标右键点击树枝，把它平分") {
            mode = Mode.RMB
            slot { it.item.`is`(FIREPIT_STICKS) }
        }
        tip("鼠标左键点击绿框") {
            emptyInvSlot()
        }
        tip("依次点击绿框 合成打火器") {
            slot { it.item.`is`(FIREPIT_STICKS) }
            slot(3)
            slot { it.item.`is`(FIREPIT_STICKS) }
            slot(2)
            slot(0)
            emptyInvSlot()
        }
        esc()
        step("面前的地面上出现了一块红色羊毛，挖掉它", {
            goHome(it)
            it.serverLevel.setBlock(originBlock, Blocks.RED_WOOL.defaultBlockState())
        }) {
            it.lookingAtBlock?.`is`(Blocks.RED_WOOL) == true
        }
        step("手持树枝，按下3次Q键，向这个坑中丢3个树枝。同样的方法，再丢1个原木。") {
            it.lookingAtItemEntity?.item?.`is`(ItemTags.LOGS) == true
        }
        step("手持刚刚做好的打火器，对准这些树枝和原木，长按鼠标右键点燃") {
            it.lookingAtBlock?.`is`(TFCBlocks.FIREPIT.get()) == true
        }
        step("鼠标右键点击刚刚点燃的篝火") {
            mc.screen is FirepitScreen
        }
        tip("放入树枝点燃") {
            slot { it.item.`is`(FIREPIT_STICKS) }
            slot(4)
        }
        step("等10秒，然后取出火把"){ player ->
            player.inventory.hasAnyMatching { it.`is`(TFCItems.TORCH.get()) }
        }

        step("手持石头 看天空，按鼠标右键，开始打磨",{
            val rockItemStack = ItemStack(TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().asItem(),32)
            it.inventory.add(rockItemStack)
        }) { mc.screen is KnappingScreen }
        tip("依次点击绿框,打磨石铲头子") {

            widgets(0, 1, 2, 3, 4, 9, 19, 20, 21, 22, 23, 24)
            slot(0)
            emptyInvSlotContainer()
        }
        esc()
        step("按E键打开背包") { mc.screen is InventoryScreen }
        tip("依次点击绿框,合成石铲") {

            slot { it.item.`is`(FIREPIT_STICKS) }
            slot(3)
            slot { it.item.toString().contains("shovel_head") }
            slot(1)
            slot(0)
            emptyInvSlot()

        }
        esc()

        step("面前出现了几个黏土草块，手持石铲把他们挖下来 （对准-鼠标左键长按）", { player: Player ->
            goHome(player)
            val loam = TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.SILTY_LOAM]!!.get().defaultBlockState()
            val level = player.level()
            level.setBlock(originBlock, loam)
            level.setBlock(originBlock.north(), loam)
            level.setBlock(originBlock.north().above(), loam)
            level.setBlock(originBlock.south(), loam)
            level.setBlock(originBlock.south().above(), loam)
            level.setBlock(originBlock.east(), loam)
            level.setBlock(originBlock.east().above(), loam)
            level.setBlock(originBlock.west(), loam)
            level.setBlock(originBlock.west().above(), loam)
        })
        { player -> player.inventory.hasAnyMatching { it.`is`(Items.CLAY_BALL) && it.count >= 12 } }
        step("再来，拿着石子,看天空,按鼠标右键打磨") { mc.screen is KnappingScreen }
        tip("依次点击绿框,打磨石刀刃"
        ) {

            widgets(5, 10, 11, 12, 13, 14, 15)
            slot(0)
            emptyInvSlotContainer()

        }
        step("按E键打开背包") { mc.screen is InventoryScreen }
        tip("依次点击绿框,合成石刀") {

            slot { it.item.`is`(FIREPIT_STICKS) }
            slot(3)
            slot { it.item.toString().contains("knife_head") }
            slot(1)
            slot(0)
            emptyInvSlot()

        }
        step("面前出现了一堆草，手持石刀把他们割下来（对准-鼠标左键点击）", { player: Player ->
            goHome(player)
            val grass = TFCBlocks.PLANTS[Plant.BLUEGRASS]!!.get().defaultBlockState()
            val level = player.level()
            level.setBlock(originBlock, grass)
            level.setBlock(originBlock.north(), grass)
            level.setBlock(originBlock.north().north(), grass)
            level.setBlock(originBlock.south(), grass)
            level.setBlock(originBlock.south().south(), grass)
            level.setBlock(originBlock.east(), grass)
            level.setBlock(originBlock.east().east(), grass)
            level.setBlock(originBlock.west(), grass)
            level.setBlock(originBlock.west().west(), grass)
        }){ player ->
            player.inventory.hasAnyMatching {
                it.`is`(
                    TFCItems.STRAW.get()
                ) && it.count >= 9
            }
        }
        step("手持黏土 看天空 按鼠标右键") { mc.screen is KnappingScreen }
        tip("依次点击绿框 合成陶罐（装水用）") {
            widgets(0, 10, 15, 17, 19, 20, 21, 23, 24)
            slot(0)
            emptyInvSlotContainer()
        }
        esc()
        step("再来，手持黏土 看天空 按鼠标右键") { mc.screen is KnappingScreen }
        tip("依次点击绿框，合成小缸（装小物件和食物用）") {
            widgets(0, 4, 20, 24)
            slot(0)
            emptyInvSlotContainer()
        }
        step("面前的地面上出现了一块红色羊毛，挖掉它", {
            goHome(it)
            it.serverLevel.setBlock(originBlock, Blocks.RED_WOOL.defaultBlockState())
        }) {
            it.lookingAtBlock?.`is`(Blocks.RED_WOOL) == true
        }
        step("手持陶罐，对着挖掉的坑里按V键，把它平放进去") {
            it.lookingAtBlock?.`is`(TFCBlocks.PLACED_ITEM.get()) == true
        }
        selfChk("手持小缸，对着坑里按V键，放进去")
        step("手持干草，放进坑里8个 （对准-连点鼠标右键）") {
            it.lookingAtBlock?.`is`(TFCBlocks.PIT_KILN.get()) == true
                    &&
                    it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.STRAW_END
        }
        step("手持原木，放进坑里8个（对准-连点鼠标右键）") {
            it.lookingAtBlock?.`is`(TFCBlocks.PIT_KILN.get()) == true
                    &&
                    it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.LIT - 1
        }
        step("手持起火器，对准坑窑 长按右键") {
            it.lookingAtBlock?.`is`(Blocks.FIRE) == true
        }
        step("等30秒 取出熟陶器") { player ->
            player.inventory.hasAnyMatching { it.`is`(TFCItems.VESSEL.get()) }
        }
        //砍树
        //打火器 篝火 火把
        //陶器..
        /*step(
            "除此之外，你还可以种植农作物、饲养动物，提高自己的生活品质与营养。走到光柱位置，将画面中心的十字瞄准成熟的水稻，按下鼠标左键收割它。",
            {
                //读取建筑 农牧场
            }) { player ->
            player.inventory.hasAnyMatching {
                it.`is`(Items.WHEAT)
            }
        }
        step("滚动鼠标滚轮，可以切换手持物品。切换到种子，瞄准耕地，按下鼠标右键播种。") { player ->
            player.lookingAtBlock?.let { block ->
                block.`is`(Blocks.WHEAT) && block.getValue(CropBlock.AGE) < 1
            } == true
        }
        step("手持水稻，走到前方的牧场，瞄准一头牛，按下鼠标右键喂它，用完你所有的水稻") {
            it.lookingAtEntity?.type == EntityType.COW && it.mainHandItem.`is`(Items.AIR)
        }*/

    }