package calebxzhou.rdi.tutorial

import calebxzhou.rdi.mixin.client.tfc.AInventoryBlockEntity
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.util.*
import mezz.jei.api.runtime.IRecipesGui
import net.dries007.tfc.client.screen.FirepitScreen
import net.dries007.tfc.client.screen.KnappingScreen
import net.dries007.tfc.client.screen.LargeVesselScreen
import net.dries007.tfc.client.screen.SmallVesselInventoryScreen
import net.dries007.tfc.common.TFCTags.Items.FIREPIT_STICKS
import net.dries007.tfc.common.TFCTags.Items.ROCK_KNAPPING
import net.dries007.tfc.common.blockentities.PlacedItemBlockEntity
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.devices.PitKilnBlock
import net.dries007.tfc.common.blocks.plant.Plant
import net.dries007.tfc.common.blocks.rock.LooseRockBlock
import net.dries007.tfc.common.blocks.rock.Rock
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.blocks.wood.Wood
import net.dries007.tfc.common.items.Food
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks

/**
 * calebxzhou @ 2024-10-24 8:14
 */
val T1_STONE = tutorial("1_stone","初级生存·石器"){
    step("对准树下面散落的树枝", {
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition())
    }) { it isLooking TFCBlocks.WOODS[Wood.OAK]!![Wood.BlockType.TWIG]!!.get()}
    step("空手对准，按鼠标右键捡起来5个") { it.bagHas(FIREPIT_STICKS,5)}
    step("同样，捡起脚下石头10个",{ player: Player ->
        val rockBlock = TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().defaultBlockState()
        val rock = rockBlock.setValue(LooseRockBlock.COUNT, 3)
        val level = player.level()
        level.setBlock(player.blockPosition(), rock)
        level.setBlock(player.blockPosition().north(), rock)
        level.setBlock(player.blockPosition().south(), rock)
        level.setBlock(player.blockPosition().east(), rock)
        level.setBlock(player.blockPosition().west(), rock)
    }) { it.bagHas(ROCK_KNAPPING,10) }
    step("手持石头，看天空，按鼠标右键进入打磨画面") { mc.screen is KnappingScreen }
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
        it bagHas (TFCItems.FOOD[Food.PORK]!!.get())
    }

}
val T1_CERA = tutorial("1_cera","初级生存·火与陶器"){
    step("捡起脚下石头10个",{ player: Player ->
        val rockBlock = TFCBlocks.ROCK_BLOCKS[Rock.ANDESITE]!![Rock.BlockType.LOOSE]!!.get().defaultBlockState()
        val rock = rockBlock.setValue(LooseRockBlock.COUNT, 3)
        val level = player.level()
        level.setBlock(player.blockPosition(), rock)
        level.setBlock(player.blockPosition().north(), rock)
        level.setBlock(player.blockPosition().south(), rock)
        level.setBlock(player.blockPosition().east(), rock)
        level.setBlock(player.blockPosition().west(), rock)
    }) { it.bagHas(ROCK_KNAPPING,10) }
    step("捡起树枝10个", {
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition())
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition().offset(0, 0, 8))
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition().offset(8, 0, 0))
        (it.level() as ServerLevel).loadStructure("maple_tree", it.blockPosition().offset(8, 0, 8))
    }) { it.bagHas(FIREPIT_STICKS,10)}
    step("做石斧砍树，拿10个原木"){
        it.bagHas(ItemTags.LOGS,10)
    }
    step("按E键打开背包") { mc.screen is InventoryScreen }
    step("搜索起火器的合成方式") {
        mc.screen is IRecipesGui
    }
    step("记住这个图案，按ESC键回到背包") { mc.screen is InventoryScreen }
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
    step("去空旷地带，往地上丢3个树枝+1个原木") {
        it.lookingAtItemEntity?.item?.`is`(ItemTags.LOGS) == true
    }
    step("手持起火器，对准这些树枝和原木，长按鼠标右键点燃（不成功就多试几次）") {
        it.lookingAtBlock?.`is`(TFCBlocks.FIREPIT.get()) == true
    }
    step("鼠标右键点击 刚刚点燃的篝火",{it.give(TFCItems.FOOD[Food.PORK]!!.get())}) {
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
        it.foodData.foodLevel = 16 }
    ) {
        !it.foodData.needsFood()
    }
    step("四处转转，寻找植物“蹄盖蕨”",{
        var originBlock = BlockPos(8,-60,8)
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

    }){
        it isLooking  TFCBlocks.PLANTS[Plant.ATHYRIUM_FERN]!!.get()
    }
    step("手持石铲，挖掉蹄盖蕨下面的黏土草块（对准-鼠标左键长按）",{it give TFCItems.ROCK_TOOLS[RockCategory.METAMORPHIC]!![RockCategory.ItemType.SHOVEL]!!.get()})
    { it.bagHas(Items.CLAY_BALL ,12 ) }

    step("手持石刀，割9个干草（对准-鼠标左键点击）", { it give TFCItems.ROCK_TOOLS[RockCategory.METAMORPHIC]!![RockCategory.ItemType.KNIFE]!!.get()})
    { it.bagHas(TFCItems.STRAW.get(), 9) }
    step("手持黏土，看天空，按鼠标右键，进入塑形画面") { mc.screen is KnappingScreen }
    tip("依次点击绿框 合成陶罐") {
        widgets(0, 10, 15, 17, 19, 20, 21, 23, 24)
        slot(0)
        airSlotContainer()
    }
    esc()
    step("手持黏土 看天空 按鼠标右键") { mc.screen is KnappingScreen }
    tip("依次点击绿框，合成小缸（装小物件和食物用）") {
        widgets(0, 4, 20, 24)
        slot(0)
        airSlotContainer()
    }
    step("自行搜索大缸的合成方法，做一个大缸"){
        it bagHas TFCItems.UNFIRED_LARGE_VESSEL.get()
    }
    //坑里第几格是不是啥
    fun lookingPitKlinHas(player: Player, item: Item): Boolean {
        val looking = player.lookingAtBlockEntity
        if(looking is PlacedItemBlockEntity){
            for(i in 0..3){
                if((looking as AInventoryBlockEntity<*>).getInventory().getStackInSlot(i).`is`(item))
                    return true
            }
        }
            return false

    }
    step("手持陶罐，挖掉脚下的方块，对着下面按V键，把它平放进去") {
        lookingPitKlinHas(it,TFCItems.UNFIRED_JUG.get())
    }
    step("手持小缸，对着坑里按V键，放进去"){
        lookingPitKlinHas(it,TFCItems.UNFIRED_VESSEL.get())
    }
    /**/
    step("手持干草，放进坑里8个 （对准-连点鼠标右键）") {
        it isLooking  TFCBlocks.PIT_KILN.get()
                &&
                it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.STRAW_END
    }
    step("手持原木，放进坑里8个（对准-连点鼠标右键）") {
        it isLooking  TFCBlocks.PIT_KILN.get()
                &&
                it.lookingAtBlock?.getValue(PitKilnBlock.STAGE) == PitKilnBlock.LIT - 1
    }
    step("手持起火器，对准坑窑 长按右键") {
        it isLooking  Blocks.FIRE
    }
    step("等待30秒，然后按鼠标右键，取出烧制完成的熟陶器") {
        it bagHas TFCItems.VESSEL.get() && it bagHas TFCItems.JUG.get() //&&
    }
    step("将小缸丢到水中降温",{
        alert("刚烧制好的陶器特别烫，用不了\n必须等它自然降温，或者丢入水中")
        it.level().setBlock(it.blockPosition().north(),Blocks.WATER)
    }){
        it isLooking TFCItems.VESSEL.get()
    }
    step("把大缸放在坑里"){
        lookingPitKlinHas(it,TFCItems.UNFIRED_LARGE_VESSEL.get())
    }
    step("用同样的方法烧制大缸"){
        it bagHas TFCBlocks.LARGE_VESSEL.get().asItem()
    }
    step("手持陶罐，对准水按下鼠标右键，把水装进罐子里，这样你就能随时随地喝水了",{}) { player ->
        player.mainHandItem.tag?.getCompound("fluid")?.getString("FluidName") == "minecraft:water"
    }
    step("手持小缸，按鼠标右键，看看里边有什么"){
        mc.screen is SmallVesselInventoryScreen
    }
    step("把刚刚挖的黏土球放进去",{ alert("小缸可以放 小体积 的物品\n以及各种食物\n小缸可以让食物不易变质") }){
        it.mainHandItem.tag?.getCompound("inventory") !=null
    }
    esc()
    step("把大缸放在地上"){
        it isLooking TFCBlocks.LARGE_VESSEL.get()
    }
    step("按鼠标右键 看看大缸里面有什么"){
        mc.screen is LargeVesselScreen
    }
    step("往大缸第一格放点东西",{ alert("大缸可以放 中等体积 的物品\n点击“密封”按钮会盖上缸盖，使缸内食品不易变质") }){ player ->
        val slot = (player.lookingAtBlockEntity as? AInventoryBlockEntity<*>)?.getInventory()?.getStackInSlot(0)
        slot?.let {
            return@step !it.isEmpty
        }?:return@step false
    }
    step("扣上大缸的盖子 然后关闭大缸画面"){
        mc.screen==null
    }



}
val T1_BUILD = tutorial("1_build","初级生存·盖房子"){}