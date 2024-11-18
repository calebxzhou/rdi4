package calebxzhou.rdi.tutorial

import calebxzhou.rdi.common.bos
import calebxzhou.rdi.common.smart
import calebxzhou.rdi.text.richText
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.util.*
import net.dries007.tfc.common.blocks.TFCBlocks
import net.dries007.tfc.common.blocks.crop.Crop
import net.dries007.tfc.common.blocks.crop.WildCropBlock
import net.dries007.tfc.common.blocks.plant.fruit.FruitBlocks
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle
import net.dries007.tfc.common.blocks.plant.fruit.SeasonalPlantBlock
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.blocks.soil.SoilBlockType
import net.dries007.tfc.common.items.Food
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks


/**
 * calebxzhou @ 2024-11-01 16:52
 */
val T1_ARGI = tutorial("1_agri", "农业") {
    val oat = TFCBlocks.WILD_CROPS[Crop.OAT]!!.get()
    step(richText {
        text("适合种植的植物分为4类")
        ret()
        text("1.农作物 例：")
        item(oat.asItem())
        text("燕麦")
        ret()
        lmb()
        text("打掉->果实+种子")
    }, {
        it giveRockTool RockCategory.ItemType.HOE
        it.serverLevel.setBlock(
            it.blockPosition().smart dz 4,
            oat.defaultBlockState().setValue(WildCropBlock.MATURE, true)
        )
    }) {
        it bagHas TFCItems.FOOD[Food.OAT]!!.get()
    }
    val bunchBerry = TFCBlocks.STATIONARY_BUSHES[FruitBlocks.StationaryBush.BUNCHBERRY]!!.get()
    step(
        richText {
            text("2. 小型灌木")
            ret()
            text("例：")
            item(bunchBerry.asItem())
            text("御膳橘灌木")
            ret()
            rmb()
            text("收获果实")
        }, {
            it.serverLevel.setBlock(
                it.blockPosition().smart dz 4,
                bunchBerry.defaultBlockState()
                    .setValue(SeasonalPlantBlock.LIFECYCLE, Lifecycle.FRUITING)
            )
        }) {
        it bagHas TFCItems.FOOD[Food.BUNCHBERRY]!!.get()
    }
    val blueBerry = TFCBlocks.SPREADING_BUSHES[FruitBlocks.SpreadingBush.BLUEBERRY]!!.get()
    step(richText {
        text("3. 大型灌木")
        ret()
        text("例：")
        item(blueBerry.asItem())
        text("蓝莓灌木")
        ret()
        rmb()
        text("收获果实")
    }, {
        it.serverLevel.setBlock(
            it.blockPosition().smart dz 4,
            blueBerry.defaultBlockState().setValue(SeasonalPlantBlock.LIFECYCLE, Lifecycle.FRUITING)
        )
    }) {
        it bagHas TFCItems.FOOD[Food.BLUEBERRY]!!.get()
    }
    step(richText {
        text("用")
        item(RockCategory.ItemType.KNIFE.item)
        text("石刀 将")
        item(blueBerry.asItem())
        text("蓝莓灌木")
        text(" 和")
        item(bunchBerry.asItem())
        text("御膳橘灌木")
        text(" 收入囊中")
    }, {
        mc.addChatMessage("采集灌木，必须用刀，否则啥也得不到")
        it giveRockTool RockCategory.ItemType.KNIFE
    }) {
        it bagHas bunchBerry.asItem()
    }
    val lemonLeaf = TFCBlocks.FRUIT_TREE_LEAVES[FruitBlocks.Tree.LEMON]!!.get()
    val lemon = TFCItems.FOOD[Food.LEMON]!!.get()
    step(richText {
        text("4. 果树  例：")
        item(lemonLeaf.asItem())
        text("柠檬树")
        rmb()
        text("有果实的树叶 -> ")
        item(lemon)
        text("柠檬")
    }, {
        val branch = TFCBlocks.FRUIT_TREE_BRANCHES[FruitBlocks.Tree.LEMON]!!.get()
        val leave = lemonLeaf.defaultBlockState()
            .setValue(SeasonalPlantBlock.LIFECYCLE, Lifecycle.FRUITING)

        val bos = it.blockPosition().smart dz 4
        it.serverLevel.run {
            //树干
            setBlock(
                branch,
                bos,
                bos dy 1,
                bos dy 2,
                bos dy 3,
                bos dy 4,
                bos dy 5
            )
            //树枝
            setBlock(
                branch,
                bos dy 3 dx 1,
                bos dy 3 dx 2,
                bos dy 4 dx 3,
                bos dy 4 dx 2,
                bos dy 4 dx 3,
                bos dy 5 dx 2,
                bos dy 5 dx 3
            )
            //树叶
            setBlock(
                leave,
                bos dy 3 dx 3,
                bos dy 4 dx 3,
                bos dy 5 dx 3
            )
        }
    }) {
        it bagHas lemon
    }
    val lemonSpl = TFCBlocks.FRUIT_TREE_SAPLINGS[FruitBlocks.Tree.LEMON]!!.get()
    step("用斧头砍断§c§l侧枝§r，得到树苗。（不要砍树干，否则树就废了，什么都得不到）", {
        it giveRockTool RockCategory.ItemType.AXE
    }) {
        it bagHas lemonSpl.asItem()
    }
    step(richText {
        text("把")
        item(lemonSpl.asItem())
        text("柠檬树苗")
        rmb()
        text("种在地上")
    }){
        it isLooking lemonSpl
    }
    //§ = Alt+Num21
    val pos = bos(0, -61, 0)
    val loam = TFCBlocks.SOIL[SoilBlockType.DIRT]!![SoilBlockType.Variant.LOAM]!!.get()
    buide("前往地图x0z0的中心位置，按照指示挖掉泥土，摆放壤土", {
        it give loam.asItem() * 32
        it give Items.WATER_BUCKET
    }) {
        destroy(pos, *pos.around2, pos.below)
        place(loam, pos.below)
        place(loam, *pos.around2)
    }
    step("往中间那个坑里倒水"){
        it isLooking Blocks.WATER
    }
    step(richText {
        text("手持")
        item(RockCategory.ItemType.HOE.item)
        text("石锄")
    }) {
        it handHas RockCategory.ItemType.HOE.item
    }
    buide(richText {
        rmb()
        item(RockCategory.ItemType.HOE.item)
        text(" ->")
        item(loam.asItem())
        text("按照提示，用石锄耕地")
    }) {
        interact(*pos.around2)
    }
    buide("把水底下那格也耕了") {
        interact(pos.below)
    }
    //每个种子都试一遍
    TFCBlocks.CROPS.forEach { (crop, block) ->
        val cropBlock = block.get()
        val cropItem = cropBlock.asItem()
        step(richText {
            text("把")
            item(cropItem)
            text(cropItem.description)
            rmb()
            if (crop == Crop.RICE) {
                text("种在§c§l水里§r的耕地上")
            } else {
                text("种在耕地上")
            }
        }, {
            it give cropItem
        }) {
            it isLooking cropBlock
        }
    }
    step(richText {
        text("每种农作物都有适宜的温度范围，温度不合适就会枯萎")
        ret()
        text("现在打掉所有的枯萎农作物")
    },{
        it.inventory.clearContent()
    }){ player ->
        player.inventory.items.filter { it!=(ItemStack.EMPTY) }.size>=8
    }

}
val T1_ANIMAL = tutorial("1_animal", "牧业") {

}
val T1_PET = tutorial("1_pet", "宠物") {

}
val T1_FOOD = tutorial("1_food", "食物与营养") {

}