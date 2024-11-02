package calebxzhou.rdi.tutorial

import calebxzhou.rdi.common.bos
import calebxzhou.rdi.common.smart
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.util.bagHas
import calebxzhou.rdi.util.serverLevel
import calebxzhou.rdi.util.setBlock
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.crop.Crop
import net.dries007.tfc.common.blocks.plant.fruit.FruitBlocks
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle
import net.dries007.tfc.common.blocks.plant.fruit.SeasonalPlantBlock
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.items.Food
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks


/**
 * calebxzhou @ 2024-11-01 16:52
 */
val T1_ARGI = tutorial("1_agri","农业"){
    step("适合种植的植物分为4类：第一种是农作物，面前的燕麦就是其中之一。打掉即可收集果实+种子（不限工具）",{
        it giveRockTool RockCategory.ItemType.HOE
        it.serverLevel.setBlock(it.blockPosition().smart dz 4,TFCBlocks.WILD_CROPS[Crop.OAT]!!.get())
    }){
        it bagHas TFCItems.FOOD[Food.OAT]!!.get()
    }
    step("第2种是小型灌木，代表为御膳橘灌木。按下鼠标右键收获果实",{
        it.serverLevel.setBlock(
            it.blockPosition().smart dz 4,
            TFCBlocks.STATIONARY_BUSHES[FruitBlocks.StationaryBush.BUNCHBERRY]!!.get().defaultBlockState().apply {
                setValue(SeasonalPlantBlock.LIFECYCLE,Lifecycle.FRUITING)
            })
    }){
        it bagHas TFCItems.FOOD[Food.BUNCHBERRY]!!.get()
    }
    step("第3种是大型灌木，以蓝莓灌木为代表。按下鼠标右键收获果实",{
        it.serverLevel.setBlock(
            it.blockPosition().smart dz 4,
            TFCBlocks.SPREADING_BUSHES[FruitBlocks.SpreadingBush.BLUEBERRY]!!.get().defaultBlockState().apply {
                setValue(SeasonalPlantBlock.LIFECYCLE,Lifecycle.FRUITING)
            })
    }){
        it bagHas TFCItems.FOOD[Food.BLUEBERRY]!!.get()
    }
    step("用石刀把蓝莓灌木和御膳橘灌木收入囊中。",{
        alert("若要将灌木收入囊中，必须用刀\n否则啥也得不到")
        it giveRockTool  RockCategory.ItemType.KNIFE
    }){
        it bagHas TFCBlocks.STATIONARY_BUSHES[FruitBlocks.StationaryBush.BUNCHBERRY]!!.get().asItem()
                &&
        it bagHas TFCBlocks.SPREADING_BUSHES[FruitBlocks.SpreadingBush.BLUEBERRY]!!.get().asItem()
    }
    step("最后一种是果树，比如柠檬树。右键有果实的树叶，可以得到柠檬。",{
        val branch = TFCBlocks.FRUIT_TREE_BRANCHES[FruitBlocks.Tree.LEMON]!!.get()
        val leave = TFCBlocks.FRUIT_TREE_LEAVES[FruitBlocks.Tree.LEMON]!!.get().defaultBlockState().apply {
            setValue(SeasonalPlantBlock.LIFECYCLE,Lifecycle.FRUITING)
        }
        val bos = it.blockPosition().smart dz 4
        it.serverLevel.setBlock(bos, branch)
        it.serverLevel.setBlock(bos dy 1, branch)
        it.serverLevel.setBlock(bos dy 2, branch)
        it.serverLevel.setBlock(bos dy 3, branch)
        it.serverLevel.setBlock(bos dy 3 dx 1, branch)
        it.serverLevel.setBlock(bos dy 3 dx 2, branch)
        it.serverLevel.setBlock(bos dy 3 dx 3, leave)
        it.serverLevel.setBlock(bos dy 4 dx 2, branch)
        it.serverLevel.setBlock(bos dy 4 dx 3, leave)
        it.serverLevel.setBlock(bos dy 5 dx 2, branch)
        it.serverLevel.setBlock(bos dy 5 dx 3, leave)
        it.serverLevel.setBlock(bos dy 4, branch)
        it.serverLevel.setBlock(bos dy 5, branch)
        it.serverLevel.setBlock(bos dy 6, branch)
    }){
        it bagHas TFCItems.FOOD[Food.LEMON]!!.get()
    }
    step("用斧头砍断§c§l侧枝§r，得到树苗。（不要砍树干，否则树就废了，什么都得不到）",{
        it giveRockTool RockCategory.ItemType.AXE
    }){
        it bagHas TFCBlocks.FRUIT_TREE_SAPLINGS[FruitBlocks.Tree.LEMON]!!.get().asItem()
    }
    //§ = Alt+Num21
    var pos = bos(0,-60,0)
    val loam = TFCBlocks.SOIL[SoilBlockType.DIRT]!![SoilBlockType.Variant.LOAM]!!.get()
    buide("按照指示挖掉泥土，摆放壤土",{
        pos = it.blockPosition().smart dz 2
        it give loam.asItem()*16
        it give Items.WATER_BUCKET
    }){
        destroy(pos)
        destroy(pos dx 1)
        destroy(pos dx -1)
        destroy(pos dz 1)
        destroy(pos dz -1)
        destroy(pos dx -1 dz -1)
        destroy(pos dx -1 dz 1)
        destroy(pos dx 1 dz -1)
        destroy(pos dx 1 dz 1)
        place(pos, Blocks.WATER)
        place(pos dx 1,loam)
        place(pos dx -1,loam)
        place(pos dz 1,loam)
        place(pos dz -1,loam)
        place(pos dx -1 dz -1,loam)
        place(pos dx -1 dz 1,loam)
        place(pos dx 1 dz -1,loam)
        place(pos dx 1 dz 1,loam)

    }
    //todo 每个种子都试一遍

}
val T1_ANIMAL = tutorial("1_animal","牧业"){

}
val T1_PET = tutorial("1_pet","宠物"){

}
val T1_FOOD = tutorial("1_food","食物与营养"){

}