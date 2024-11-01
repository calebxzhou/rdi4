package calebxzhou.rdi.tutorial

import calebxzhou.rdi.common.SmartBlockPos
import calebxzhou.rdi.common.bos
import calebxzhou.rdi.common.smart
import calebxzhou.rdi.mixin.client.tfc.AInventoryBlockEntity
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.util.*
import com.electronwill.nightconfig.toml.TomlFormat
import mezz.jei.api.runtime.IRecipesGui
import net.dries007.tfc.client.screen.FirepitScreen
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.client.screen.LargeVesselScreen
import net.dries007.tfc.client.screen.SmallVesselInventoryScreen
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.TFCTags.Items.ROCK_KNAPPING
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.WattleBlock
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock
import net.dries007.tfc.common.blocks.devices.FirepitBlock
import net.dries007.tfc.common.blocks.devices.PitKilnBlock
import net.dries007.tfc.common.blocks.plant.Plant
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.dries007.tfc.common.blocks.rock.Rock
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.blocks.wood.Wood
import net.dries007.tfc.common.items.Food
import net.dries007.tfc.common.items.TFCItems
import net.dries007.tfc.util.calendar.Calendars
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock

/**
 * calebxzhou @ 2024-10-24 8:14
 */
val T1_STONE = tutorial("1_stone", "石器") {
    step("对准树下面散落的树枝", {
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition())
    }) { it isLooking TFCBlocks.WOODS[Wood.OAK]!![Wood.BlockType.TWIG]!!.get() }
    step("空手对准，按鼠标右键捡起来5个") { it.bagHas(FIREPIT_STICKS, 5) }
    step("同样，捡起脚下石头10个", { player: Player ->
        val rockBlock = TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().defaultBlockState()
        val rock = rockBlock.setValue(LooseRockBlock.COUNT, 3)
        val level = player.level()
        level.setBlock(player.blockPosition(), rock)
        level.setBlock(player.blockPosition().north(), rock)
        level.setBlock(player.blockPosition().south(), rock)
        level.setBlock(player.blockPosition().east(), rock)
        level.setBlock(player.blockPosition().west(), rock)
    }) { it.bagHas(ROCK_KNAPPING, 10) }
    step("手持石头，看天空，按鼠标右键进入打磨画面") { mc.screen is KnappingScreen }
    step("在画面右下角的文本框中，输入汉字 石斧头，查看它的合成方式") {
        mc.screen is IRecipesGui
    }
    step("记住这个图案，按esc返回") { mc.screen is KnappingScreen }
    //做斧子
    tip("依次点击绿框，打磨石斧头部") {
        widgets(0, 10, 15, 20, 21, 23, 24, 19, 14, 4)
        slot(0)
        airSlotContainer()
    }
    step("用前面学习到的知识，去搜索 石斧 的合成方法，尝试做一个出来") {
        it.hasRockTool(RockCategory.ItemType.AXE)
    }
    step("手持石斧，对准树的根部，长按鼠标左键给它砍断") {
        it.bagHas(ItemTags.LOGS, 1)
    }
    step("拿着石子,看天空,按鼠标右键打磨") { mc.screen is KnappingScreen }
    step("在画面右下角的文本框中，输入汉字 石刀刃，查看它的合成方式") {
        mc.screen is IRecipesGui
    }
    step("记住这个图案，按esc返回") { mc.screen is KnappingScreen }
    tip(
        "依次点击绿框,打磨石刀刃"
    ) {

        widgets(5, 10, 11, 12, 13, 14, 15)
        slot(0)
        airSlotContainer()

    }
    step("用前面学习到的知识，去搜索 石刀 的合成方法，尝试做一个出来") { it.hasRockTool(RockCategory.ItemType.KNIFE) }
    step("手持石刀，去树下面割3个干草（对准-鼠标左键点击）")
    { it bagHas TFCItems.STRAW.get() * 3 }
    step("干掉面前这几头猪 （手持石刀-对准-鼠标左键狂点）", { player ->
        repeat(5) {
            mcs?.executeCommand("summon tfc:pig ${player.blockX} ${player.blockY} ${player.blockZ}")
        }
    }) {
        it bagHas (TFCItems.FOOD[Food.PORK]!!.get())
    }

}
val T1_FIRE = tutorial("1_fire", "钻木取火") {
    step("按E键打开背包", {
        it giveTwig 15
        it giveTwig 15
        it giveLog 15
        it giveLog 15
        it give (TFCItems.STRAW.get() * 15)
    }) { mc.screen is InventoryScreen }
    tip("鼠标右键点击树枝，把它平分") {
        slot(true) { it.item.`is`(FIREPIT_STICKS) }
    }
    tip("鼠标左键点击绿框") {
        airSlotInv()
    }
    tip("依次点击绿框 合成起火器") {
        slot { it.item.`is`(FIREPIT_STICKS) }
        slot(3)
        slot { it.item.`is`(FIREPIT_STICKS) }
        slot(2)
        slot(0)
        airSlotInv()
    }
    esc()
    step("去空旷地带，往地上丢3个树枝+1个原木") {
        it.lookingAtItemEntity?.item?.`is`(ItemTags.LOGS) == true
    }
    step("手持起火器，对准这些树枝和原木，长按鼠标右键点燃（不成功就多试几次）") {
        it isLooking TFCBlocks.FIREPIT.get()
    }
    step("鼠标右键点击 刚刚点燃的篝火", { it.give(TFCItems.FOOD[Food.PORK]!!.get()) }) {
        mc.screen is FirepitScreen
    }
    tip("放入猪肉 开始烹饪") {
        slot { it.item.`is`(TFCItems.FOOD[Food.PORK]!!.get()) }
        slot(4)
    }
    step("等10秒，然后取出熟猪肉") { player ->
        player.inventory.hasAnyMatching {
            it.`is`(TFCItems.FOOD[Food.COOKED_PORK]!!.get())
        }
    }
    step("手持猪肉，长按鼠标右键吃掉", {
        alert("把食物搞熟，可以延长保质期\n也会带来更强的饱腹感")
        it.foodData.foodLevel = 16
    }
    ) {
        !it.foodData.needsFood()
    }
    val pos = SmartBlockPos(8, -61, 8)
    buide("按照指示挖一个2x3的坑", {
    }) {
        destroy(pos dx 1)
        destroy(pos dx 1 dz 1)
        destroy(pos dx 1 dz 2)
        destroy(pos dx 2)
        destroy(pos dx 2 dz 1)
        destroy(pos dx 2 dz 2)
    }
    buide("在坑中放置原木堆，(手持原木 按下左Shift下蹲的同时 按鼠标右键放置)\n右键点击原木堆，把里面的木头都填满", {
        it giveRockTool RockCategory.ItemType.AXE
        it giveLog 16
        it giveLog 16
        it giveLog 16
        it giveLog 16
        it giveLog 16
        it giveLog 16
        it giveLog 16
        it giveLog 16
        it giveLog 16
    }) {
        val bk = TFCBlocks.LOG_PILE.get()
        place(pos dx 1, bk)
        place(pos dx 1 dz 1, bk)
        place(pos dx 1 dz 2, bk)
        place(pos dx 2, bk)
        place(pos dx 2 dz 1, bk)
        place(pos dx 2 dz 2, bk)
    }
    buide("蹲下的同时，在原木堆上方放置泥土") {
        val bk = Blocks.DIRT
        place(pos dy 1 dx 1 dz 1, bk)
        place(pos dy 1 dx 1 dz 2, bk)
        place(pos dy 1 dx 2, bk)
        place(pos dy 1 dx 2 dz 1, bk)
        place(pos dy 1 dx 2 dz 2, bk)
    }
    step("手持起火器",{
        it give Blocks.DIRT.asItem()*16
    }) {
        it handHas TFCItems.FIRESTARTER.get()
    }
    step("对准裸露的木堆，按住鼠标右键，直到点燃") {
        it isLooking Blocks.FIRE
    }
    step("手持泥土，右键点击火焰，把木炭盖好（如果出现其他有火焰的地方，也要盖好，否则烧制木炭失败）") {
        it isLooking Blocks.DIRT
    }
    step("等它不冒烟了，再把木炭堆挖开") {
        it isLooking TFCBlocks.CHARCOAL_PILE.get()
    }
    step("用铲子收集里面的木炭",{
        it giveRockTool RockCategory.ItemType.SHOVEL
    }) {
        it bagHas Items.CHARCOAL
    }
    step("木炭暂时用不到，以后提炼矿物的时候用到，回到之前做的篝火那边") {
        it isLooking TFCBlocks.FIREPIT.get()
    }
    step("在篝火画面左侧添加原木，用起火器重新点燃篝火") {
        it isLooking TFCBlocks.FIREPIT.get() &&
                it.lookingAtBlock?.getValue(FirepitBlock.LIT) == true
    }

}
val T1_CERA = tutorial("1_cera", "陶器") {

    step("四处转转，寻找植物“蹄盖蕨”", {
        it giveTwig 16
        it giveTwig 16
        it giveLog 16
        it giveLog 16
        it give (TFCItems.STRAW.get() * 64)
        var originBlock = BlockPos(8, -60, 8)
        val loam =
            TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.SILTY_LOAM]!!.get().defaultBlockState()
        val athyrium = TFCBlocks.PLANTS[Plant.ATHYRIUM_FERN]!!.get().defaultBlockState()
        val level = it.level()
        level.setBlock(originBlock, athyrium)
        originBlock = originBlock.below()
        level.setBlock(originBlock, loam)
        level.setBlock(originBlock.north(), loam)
        level.setBlock(originBlock.south(), loam)
        level.setBlock(originBlock.east(), loam)
        level.setBlock(originBlock.west(), loam)
        originBlock = originBlock.below()
        level.setBlock(originBlock, loam)
        level.setBlock(originBlock.north(), loam)
        level.setBlock(originBlock.south(), loam)
        level.setBlock(originBlock.east(), loam)
        level.setBlock(originBlock.west(), loam)

    }) {
        it isLooking TFCBlocks.PLANTS[Plant.ATHYRIUM_FERN]!!.get()
    }
    step("手持石铲，挖掉蹄盖蕨下面的黏土草块（对准-鼠标左键长按）", { it giveRockTool RockCategory.ItemType.SHOVEL })
    { it bagHas Items.CLAY_BALL * 12 }


    step("手持黏土，看天空，按鼠标右键，进入塑形画面") { mc.screen is KnappingScreen }
    tip("依次点击绿框 合成陶罐") {
        widgets(0, 10, 15, 17, 19, 20, 21, 23, 24)
        slot(0)
        airSlotContainer()
    }
    esc()
    step("第二次，手持黏土 看天空 按鼠标右键") { mc.screen is KnappingScreen }
    tip("依次点击绿框，合成小缸（装小物件和食物用）") {
        widgets(0, 4, 20, 24)
        slot(0)
        airSlotContainer()
    }
    esc()
    step("自行搜索大缸的合成方法，做一个大缸") {
        it bagHas TFCItems.UNFIRED_LARGE_VESSEL.get()
    }
    //坑里第几格是不是啥
    step("手持陶罐，挖掉脚下的方块，对着下面按V键，把它平放进去") {
        lookingPitKilnHas(it, TFCItems.UNFIRED_JUG.get())
    }
    step("手持小缸，对着坑里按V键，放进去") {
        lookingPitKilnHas(it, TFCItems.UNFIRED_VESSEL.get())
    }
    /**/
    step("手持干草，放进坑里8个 （对准-连点鼠标右键）") {
        it isLooking TFCBlocks.PIT_KILN.get()
                &&
                it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.STRAW_END
    }
    step("手持原木，放进坑里8个（对准-连点鼠标右键）") {
        it isLooking TFCBlocks.PIT_KILN.get()
                &&
                it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.LIT - 1
    }
    step("制作并手持起火器，对准坑窑 长按右键点燃") {
        it isLooking Blocks.FIRE
    }
    step("等待30秒，然后按鼠标右键，取出烧制完成的熟陶器") {
        it bagHas TFCItems.VESSEL.get() && it bagHas TFCItems.JUG.get()
    }
    step("将小缸丢到水中降温", {
        alert("刚烧制好的陶器特别烫，用不了\n必须等它自然降温，或者丢入水中")
        it.level().setBlock(it.blockPosition().north(), Blocks.WATER)
    }) {
        it isLooking TFCItems.VESSEL.get()
    }
    step("把大缸放在坑里") {
        lookingPitKilnHas(it, TFCItems.UNFIRED_LARGE_VESSEL.get())
    }
    step("用同样的方法烧制大缸") {
        it bagHas TFCBlocks.LARGE_VESSEL.get().asItem()
    }
    step("手持陶罐，对准水按下鼠标右键，把水装进罐子里，这样你就能随时随地喝水了", {}) { player ->
        player.mainHandItem.tag?.getCompound("fluid")?.getString("FluidName") == "minecraft:water"
    }
    step("手持小缸，按鼠标右键，看看里边有什么") {
        mc.screen is SmallVesselInventoryScreen
    }
    step("把刚刚挖的黏土球放进去", { alert("小缸可以放 小体积 的物品\n以及各种食物\n小缸可以让食物不易变质") }) {
        it.mainHandItem.tag?.getCompound("inventory") != null
    }
    esc()
    step("把大缸放在地上") {
        it isLooking TFCBlocks.LARGE_VESSEL.get()
    }
    step("按鼠标右键 看看大缸里面有什么") {
        mc.screen is LargeVesselScreen
    }
    step("往大缸第一格放点东西",
        { alert("大缸可以放 中等体积 的物品\n点击“密封”按钮会盖上缸盖，使缸内食品不易变质") }) { player ->
        val slot = (player.lookingAtBlockEntity as? AInventoryBlockEntity<*>)?.getInventory()?.getStackInSlot(0)
        slot?.let {
            return@step !it.isEmpty
        } ?: return@step false
    }
    step("扣上大缸的盖子 然后关闭大缸画面") {
        mc.screen == null
    }

}
val T1_BUILD = tutorial("1_build", "建筑材料") {
    var org = bos(0, -60, 0)
    buide("按照指示，把泥土堆起来", {
        it give Blocks.DIRT.asItem() * 8
    }) {
        place(org, Blocks.DIRT)
        place(org dy 1, Blocks.DIRT)
    }
    buide("堆起来的泥土从旁边滑了下去，因为游戏里大部分物体，没有支撑就会掉下来\n要想盖房子，就要用无需支撑的方块，下面介绍第一种：干草块。按照指示摆放干草块",
        {
            org = org.copy dx 1
            it give TFCBlocks.THATCH.get().asItem() * 12
        },
        {
            val bk = TFCBlocks.THATCH.get()
            place(org, bk)
            place(org dx 1, bk)
            place(org dz 1, bk)
            place(org dy 1 dx 1, bk)
            place(org dy 1 dz 1, bk)
            place(org dy 2 dx 1, bk)
            place(org dy 2, bk)
            place(org dy 2 dz 1, bk)
            place(org dy 2 dz 2, bk)
            place(org dy 2 dz 3, bk)

        })
    step("走到干草块内部，跳一下") { it.blockStateOn.`is`(TFCBlocks.THATCH.get()) }
    buide("干草块是空心的，任何物体都能穿过去，现在按照指示摆放一个泥土") {
        place(org dy 1, Blocks.DIRT)
    }
    step("因为干草块是空心的，所以它起不到任何支撑的作用，泥土放在它上面照样塌陷。现在按下ESC键，看看现在是什么季节，然后打在公屏上") {
        mc justChatted "初夏"
    }
    step("用铲子，最快速度铲掉地面上的雪，并且合成1个雪块", {
        org = org.copy dx 2
        it.serverLevel.setBlock(org, Blocks.SNOW)
        it.serverLevel.setBlock(org dx 1, Blocks.SNOW)
        it.serverLevel.setBlock(org dx 2, Blocks.SNOW)
        it.serverLevel.setBlock(org dx 3, Blocks.SNOW)
        it.serverLevel.setBlock(org dx 4, Blocks.SNOW)
        it.serverLevel.setBlock(org dx 5, Blocks.SNOW)
        it giveRockTool RockCategory.ItemType.SHOVEL
    }) {
        it bagHas Blocks.SNOW_BLOCK.asItem()
    }
    buide("按照指示摆放雪块", {
        org = org.copy dx 3
        it give Blocks.SNOW_BLOCK.asItem() * 15
    }) {
        place(org, Blocks.SNOW_BLOCK)
        place(org.above(), Blocks.SNOW_BLOCK)
        place(org.above().west(), Blocks.SNOW_BLOCK)
        place(org.above().west().west(), Blocks.SNOW_BLOCK)
    }
    step("雪块是实心的，可以起到支撑作用，并且即使在炎热的夏天，也不会融化。搜索板条的合成方式，用刚刚给你的木头做一些板条",
        {
            it giveLog 16
            it giveLog 16
            it giveLog 16
        }) {
        it bagHas TFCBlocks.WATTLE.get().asItem()
    }
    buide("按照指示摆放板条", {
        org = org.copy dx 4
    }) {
        val block = TFCBlocks.WATTLE.get()
        place(org, block)
        place(org dy 1, block)
        place(org dy 2, block)
        destroy(org)
        place(org dx 1, block)
        place(org dx 1 dy 1, Blocks.DIRT)
        place(org dx 1, block)
    }
    step("板条一旦失去支撑，整体都会塌掉，并且现在的空心板条仍然没有支撑作用，所以我们需要制作实心板条\n手持至少4个树枝，右键点击空心板条，即可制成实心板条",
        {
            it giveTwig 16
        }) {
        it.lookingAtBlock?.let { bst ->
            return@step bst.block is WattleBlock && bst.getValue(WattleBlock.WOVEN)
        } ?: false
    }
    step("用背包里的材料，制作涂料（自行搜索合成配方）", {
        it give TFCItems.STRAW.get()
        it give Items.CLAY_BALL
        it give Blocks.DIRT.asItem()
    }) {
        it bagHas TFCItems.DAUB.get()
    }
    step("手持涂料，右键点击实心板条，即可制成未染色板条") {
        it isLooking TFCBlocks.UNSTAINED_WATTLE.get()
    }
    DyeColor.entries.forEach { color ->
        step("手持刚刚给你的${color.name}染料，右键点击板条，给它上色", {
            it.inventory.clearContent()
            it give DyeItem.byColor(color)
        }) {
            it isLooking TFCBlocks.STAINED_WATTLE[color]!!.get()
        }
    }
    buide("按照指示摆放染色板条", {
        org = org.copy dx 5
        DyeColor.entries.forEach { color -> it give TFCBlocks.STAINED_WATTLE[color]!!.get().asItem() }
    }) {
        DyeColor.entries.forEachIndexed { i, color ->
            place(org dz i, TFCBlocks.STAINED_WATTLE[color]!!.get())
        }
    }
    val mud = TFCBlocks.SOIL[SoilBlockType.MUD]!![SoilBlockType.Variant.LOAM]!!.get()
    step("用石铲挖掉前方出现的壤泥", { player ->
        org = org.copy dx 6
        player giveRockTool RockCategory.ItemType.SHOVEL
        player.serverLevel.setBlock(org dz 1, mud)
    }) {
        it bagHas mud.asItem()
    }
    val wetBrick = TFCBlocks.SOIL[SoilBlockType.DRYING_BRICKS]!![SoilBlockType.Variant.LOAM]!!.get()
    step("把壤泥和干草合成为4个湿壤泥砖", {
        it give TFCItems.STRAW.get()
    }) {
        it bagHas wetBrick.asItem() * 4
    }
    step("按下鼠标右键 把4个湿壤泥砖放在地上") {
        it.lookingAtBlock?.let { bst ->
            return@step bst.block is DryingBricksBlock && bst.getValue(DryingBricksBlock.COUNT) == 4
        } ?: false
    }
    step("等待游戏时间一天，让湿壤泥砖变为干壤泥砖（此处已经加速，等5~20秒就行）", {
    }) {
        it.lookingAtBlock?.let { bst ->
            return@step bst.block is DryingBricksBlock && bst.getValue(DryingBricksBlock.DRIED)
        } ?: false
    }
    step("把干壤泥砖捡起来") {
        it bagHas TFCItems.LOAM_MUD_BRICK.get()
    }
    val mudBricks = TFCBlocks.SOIL[SoilBlockType.MUD_BRICKS]!![SoilBlockType.Variant.LOAM]!!.get()
    step("用4个干壤泥砖合成一个壤泥砖块") {
        it bagHas mudBricks.asItem()
    }
    buide("壤泥砖块非常坚固，纯实心，可以任意悬空放置，任意支撑任何物品。必须用铲子才能破坏它。按照指示放置与破坏壤泥砖块", {
        it give mudBricks.asItem() * 16
    }) {
        place(org, mudBricks)
        place(org dy 1, mudBricks)
        place(org dy 2, mudBricks)
        place(org dy 3, mudBricks)
        place(org dy 4, mudBricks)
        place(org dy 4 dx 1, mudBricks)
        place(org dy 4 dx 2, mudBricks)
        place(org dy 4 dz 1, mudBricks)
        place(org dy 4 dz 2, mudBricks)
        destroy(org dy 1)
        destroy(org dy 2)
        destroy(org dy 3)
        destroy(org dy 4)
    }
}
