package calebxzhou.rdi.tutorial

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
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

/**
 * calebxzhou @ 2024-10-24 8:14
 */
val TUTORIAL_PRIMARY_STONE = tutorial("primary_stone","初级生存-石器时代"){
    step("捡起树下的树枝10个(空手对准-鼠标右键)", {
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition())
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition().offset(0, 0, 8))
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition().offset(8, 0, 0))
    }) { it.bagHas(FIREPIT_STICKS,10)}
    step("捡起面前出现的石头10个", { player: Player ->
        val rockBlock = TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().defaultBlockState()
        val rock = rockBlock.setValue(LooseRockBlock.COUNT, 3)
        val level = player.level()
        level.setBlock(player.blockPosition(), rock)
        level.setBlock(player.blockPosition().north(), rock)
        level.setBlock(player.blockPosition().south(), rock)
        level.setBlock(player.blockPosition().east(), rock)
        level.setBlock(player.blockPosition().west(), rock)
    }) { it.bagHas(ROCK_KNAPPING,10) }

    step("手持石头，看天空，按鼠标右键开始打磨") { mc.screen is KnappingScreen }
    step("在画面右下角的文本框中，输入汉字 石斧头，查看它的合成方式") {
        mc.screen is IRecipesGui
    }
    step("记住这个图案，按esc返回"){mc.screen is KnappingScreen }
    //做斧子
    tip("依次点击绿框，打磨石斧头部") {
        widgets(0, 10, 15, 20, 21, 23, 24, 19, 14, 4)
        slot(0)
        airSlotContainer()
    }
    step("用前面学习到的知识，去搜索 石斧 的合成方法，尝试做一个出来"){
        it.bagHasRockTool(RockCategory.ItemType.AXE)
    }
    step("手持石斧，对准树的根部，长按鼠标左键给它砍断") {
        it.bagHas(ItemTags.LOGS,1)
    }
    step("拿着石子,看天空,按鼠标右键打磨") { mc.screen is KnappingScreen }
    step("在画面右下角的文本框中，输入汉字 石刀刃，查看它的合成方式") {
        mc.screen is IRecipesGui
    }
    step("记住这个图案，按esc返回"){mc.screen is KnappingScreen }
    tip(
        "依次点击绿框,打磨石刀刃"
    ) {

        widgets(5, 10, 11, 12, 13, 14, 15)
        slot(0)
        airSlotContainer()

    }
    step("用前面学习到的知识，去搜索 石刀 的合成方法，尝试做一个出来") { it.bagHasRockTool(RockCategory.ItemType.KNIFE) }
    step("同样，做一个石标枪" ) {
        it.bagHasRockTool(RockCategory.ItemType.JAVELIN)
    }
    step("干掉面前这几头猪 （手持石刀-对准-鼠标左键狂点 或 手持石标枪-对准-鼠标右键蓄力投掷）", { player ->
        repeat(5) {
            mcs?.executeCommand("summon tfc:pig ${player.blockX} ${player.blockY} ${player.blockZ}")
        }
    }) {
        it bagHas (TFCItems.FOOD[Food.PORK]!!.get() by 3)
    }

}
val TUTORIAL_PRIMARY = tutorial("primary","初级生存"){
    val originBlock = BlockPos(0, -61, 3)
    val originBlockAbove = BlockPos(0, -60, 3)
    val goHome = { player: Player ->
        player.teleportTo(0.0, -60.0, 0.0)
        player.xRot = 0f
        player.yRot = 0f
    }
//树枝
    step("空手对准，通过鼠标右键，捡起前面树下的树枝，10个", {
        (it.level() as ServerLevel).loadStructure("maple_tree", originBlockAbove)
        (it.level() as ServerLevel).loadStructure("maple_tree", originBlockAbove.offset(0, 0, 8))
        (it.level() as ServerLevel).loadStructure("maple_tree", originBlockAbove.offset(8, 0, 0))
    }) { it.bagHas(FIREPIT_STICKS,10)}
    step("按E键打开背包") { mc.screen is InventoryScreen }
    step("在画面右下角的文本框中，输入汉字 起火器，点击画面上出现的起火器，查看它的合成方式") {
        mc.screen is IRecipesGui
    }
    step("由此得知起火器的合成方式，是树枝斜对角放置。按ESC键回到背包") { mc.screen is InventoryScreen }
    tip("鼠标右键点击树枝，把它平分") {
        rightClick = true
        slot { it.item.`is`(FIREPIT_STICKS) }
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

    step("面前出现了几个黏土草块，手持石铲把他们挖下来 （对准-鼠标左键长按）", { player: Player ->
        goHome(player)
        val loam =
            TFCBlocks.SOIL[SoilBlockType.CLAY_GRASS]!![SoilBlockType.Variant.SILTY_LOAM]!!.get().defaultBlockState()
        val level = player.level()
        level.setBlock(originBlockAbove, loam)
        level.setBlock(originBlockAbove.north(), loam)
        level.setBlock(originBlockAbove.north().above(), loam)
        level.setBlock(originBlockAbove.south(), loam)
        level.setBlock(originBlockAbove.south().above(), loam)
        level.setBlock(originBlockAbove.east(), loam)
        level.setBlock(originBlockAbove.east().above(), loam)
        level.setBlock(originBlockAbove.west(), loam)
        level.setBlock(originBlockAbove.west().above(), loam)
    })
    { player -> player.inventory.hasAnyMatching { it.`is`(Items.CLAY_BALL) && it.count >= 12 } }

    step("面前出现了一堆草，手持石刀把他们割下来（对准-鼠标左键点击）", { player: Player ->
        goHome(player)
        val grass = TFCBlocks.PLANTS[Plant.BLUEGRASS]!!.get().defaultBlockState()
        val level = player.level()
        level.setBlock(originBlockAbove, grass)
        level.setBlock(originBlockAbove.north(), grass)
        level.setBlock(originBlockAbove.north().north(), grass)
        level.setBlock(originBlockAbove.south(), grass)
        level.setBlock(originBlockAbove.south().south(), grass)
        level.setBlock(originBlockAbove.east(), grass)
        level.setBlock(originBlockAbove.east().east(), grass)
        level.setBlock(originBlockAbove.west(), grass)
        level.setBlock(originBlockAbove.west().west(), grass)
    }) { it.bagHas(TFCItems.STRAW.get(), 9) }
    step("制作陶器：手持黏土 看天空 按鼠标右键") { mc.screen is KnappingScreen }
    tip("依次点击绿框 合成陶罐（装水用）") {
        widgets(0, 10, 15, 17, 19, 20, 21, 23, 24)
        slot(0)
        airSlotContainer()
    }
    esc()
    step("再来，手持黏土 看天空 按鼠标右键") { mc.screen is KnappingScreen }
    tip("依次点击绿框，合成小缸（装小物件和食物用）") {
        widgets(0, 4, 20, 24)
        slot(0)
        airSlotContainer()
    }
    step("手持陶罐，挖掉脚下的方块，对着下面按V键，把它平放进去") {
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
    step("等待30秒，然后按鼠标右键，取出烧制完成的熟陶器") { player ->
        player.inventory.hasAnyMatching { it.`is`(TFCItems.VESSEL.get()) }
                && player.inventory.hasAnyMatching {
            it.`is`(TFCItems.JUG.get())
        }
    }
    selfChk("我猜你已经渴了，面前的地面上有水，对准按下鼠标右键可以喝水。\n其中一滩水是淡水，另一滩是咸水，饮用咸水会导致口渴debuff（负面效果），因此你应该尽可能喝淡水") {
        goHome(it)
        it.serverLevel.setBlock(originBlock, Blocks.WATER.defaultBlockState())
        it.serverLevel.setBlock(originBlock.north(), TFCBlocks.SALT_WATER.get().defaultBlockState())
    }
    step("手持陶罐，对准淡水按下鼠标右键，把水装进罐子里，这样你就能随时随地喝水了") { player ->
        player.mainHandItem.tag?.getCompound("fluid")?.getString("FluidName") == "minecraft:water"
    }

    selfChk("手持树枝，按下3次Q键，向面前的红色羊毛上丢3个树枝。同样的方法，再丢1个原木。", {
        it.serverLevel.setBlock(
            it.blockPosition().below().relative(it.direction, 2),
            Blocks.RED_WOOL.defaultBlockState()
        )
    })
    step("手持刚刚做好的打火器，对准这些树枝和原木，长按鼠标右键点燃") {
        it.lookingAtBlock?.`is`(TFCBlocks.FIREPIT.get()) == true
    }
    step("鼠标右键 点击刚刚点燃的篝火") {
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
    step("吃饱喝足了，最重要的事当然是睡一觉，下面学习如何制作一张床。按下E键打开背包", { player ->
        player.inventory.add(ItemStack(TFCItems.STRAW.get(), 8))
    }) {
        mc.screen is InventoryScreen
    }
    step("在画面右下角的文本框中，输入汉字 干草块，然后点击画面上出现的干草块，查看它的合成方式") {
        mc.screen is IRecipesGui
    }
    step("按下ESC键返回到背包画面") {
        mc.screen is InventoryScreen
    }
    tip("点击干草") {
        slot { it.item.`is`(TFCItems.STRAW.get()) }
    }
    step("将鼠标放在“合成”二字下面的框上，按住鼠标左键，在4个合成框上滑动，让干草充满4个框。按下左Shift键，点击右侧合成完成的干草块") { player ->
        player.inventory.hasAnyMatching {
            it.`is`(TFCBlocks.THATCH.get().asItem()) && it.count >= 2
        }
    }
    step("将两个干草块呈一字型放在地上，然后手持大块兽皮，右键点击干草块", {
        it.inventory.add(TFCItems.HIDES[HideItemType.RAW]!![HideItemType.Size.LARGE]!!.get().defaultInstance)
    }) {
        it.lookingAtBlock?.`is`(TFCBlocks.THATCH_BED.get()) == true
    }
    selfChk("右键点击这个床，可以设置复活点（但是暂时不能睡觉）")


}