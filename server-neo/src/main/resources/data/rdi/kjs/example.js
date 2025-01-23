 
ServerEvents.tags('block', event => {
  
}
)
ServerEvents.tags('item', event => {
  //tfc锻铁可作铁锭 
  event.add('forge:ingots/iron', 'tfc:metal/ingot/wrought_iron')
  //tfc胶水可作粘液球
  event.add('forge:slimeballs', 'tfc:glue')
  //mc铁锭不作为#铸铁，防止锻铁焊接会变成铸铁双锭的bug
  event.remove('forge:ingots/cast_iron', 'minecraft:iron_ingot') 
  //天境 天根木木板-mc木板
  event.add('minecraft:planks', 'aether:skyroot_planks')
  event.add('minecraft:stairs', 'aether:skyroot_stairs')
  event.add('minecraft:slabs', 'aether:skyroot_slabs')
}
)

ServerEvents.recipes(event => { 
  //群峦
  const tfc = event.recipes.tfc
  
  // 原版铁锭不合成
  //event.remove({ output: 'minecraft:iron_ingot' })
  //mc铜锭全都换成tfc铜
  event.replaceInput(

    { input: 'minecraft:copper_ingot' }, // Arg 1: the filter
    'minecraft:copper_ingot',            // Arg 2: the item to replace
    'tfc:metal/ingot/copper'         // Arg 3: the item to replace it with
    // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
  )
  //原版铁锭全都换成tfc
    event.replaceInput(

      { input: 'minecraft:iron_ingot' }, // Arg 1: the filter
      'minecraft:iron_ingot',            // Arg 2: the item to replace
      'tfc:metal/ingot/wrought_iron'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
    //mc钻石-矿词
    event.replaceInput(

      { input: 'minecraft:diamond' }, // Arg 1: the filter
      'minecraft:diamond',            // Arg 2: the item to replace
      '#forge:gems/diamond'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
  //mc铁块-tfc锻铁块
  event.replaceInput(

    { input: 'minecraft:iron_block' }, // Arg 1: the filter
    'minecraft:iron_block',            // Arg 2: the item to replace
    'tfc:metal/block/wrought_iron'         // Arg 3: the item to replace it with
    // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
  )
  //mc海泡菜、tfc
  event.replaceInput(

    { input: 'minecraft:sea_pickle' }, // Arg 1: the filter
    'minecraft:sea_pickle',            // Arg 2: the item to replace
    'tfc:sea_pickle'         // Arg 3: the item to replace it with
    // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
  )
  //mc沙子/红沙 - 矿词
  event.replaceInput(
    { input: 'minecraft:sand' }, // Arg 1: the filter
    'minecraft:sand',            // Arg 2: the item to replace
    '#forge:sand'         // Arg 3: the item to replace it with
    // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
  )
  event.replaceInput(
    { input: 'minecraft:red_sand' }, // Arg 1: the filter
    'minecraft:red_sand',            // Arg 2: the item to replace
    '#forge:sand'         // Arg 3: the item to replace it with
    // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
  )
  //原版金锭全都换成tfc
    event.replaceInput(

      { input: 'minecraft:gold_ingot' }, // Arg 1: the filter
      'minecraft:gold_ingot',            // Arg 2: the item to replace
      'tfc:metal/ingot/gold'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    ) 
  //原料铁粒换成锌粒
    event.replaceInput(

      { input: 'minecraft:iron_nugget' }, // Arg 1: the filter
      'minecraft:iron_nugget',            // Arg 2: the item to replace
      'tfc:ore/small_sphalerite'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
    //mc下届合金锭-tfc黑钢锭
    event.replaceInput(

      { input: 'minecraft:netherite_ingot' }, // Arg 1: the filter
      'minecraft:netherite_ingot',            // Arg 2: the item to replace
      'tfc:metal/ingot/black_steel'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
    
    //mc下届合金块-tfc黑钢块
    event.replaceInput(

      { input: 'minecraft:netherite_block' }, // Arg 1: the filter
      'minecraft:netherite_block',            // Arg 2: the item to replace
      'tfc:metal/block/black_steel'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
  //原料mc海带换成tfc海带
    event.replaceInput(

      { input: 'minecraft:kelp' }, // Arg 1: the filter
      'minecraft:kelp',            // Arg 2: the item to replace
      'tfc:plant/leafy_kelp'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
     
    //原版高炉换成tfc高炉
    event.replaceInput(

      { input: 'minecraft:blast_furnace' }, // Arg 1: the filter
      'minecraft:blast_furnace',            // Arg 2: the item to replace
      'tfc:blast_furnace'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
    //原版熔炉换成tfc坩埚
    event.replaceInput(
      { input: 'minecraft:furnace' }, // Arg 1: the filter
      'minecraft:furnace',            // Arg 2: the item to replace
      'tfc:crucible'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
    //mc绿宝石换成tfc
    event.replaceInput(
      { input: 'minecraft:emerald' }, // Arg 1: the filter
      'minecraft:emerald',            // Arg 2: the item to replace
      'tfc:gem/emerald'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
    //丝绸做羊毛
    event.recipes.tfc.loom('8x minecraft:white_wool', '4x tfc:silk_cloth', 24, 'minecraft:block/white_wool')
    //陶锅煮盐
    event.recipes.tfc.pot(
      [],
       Fluid.of('tfc:salt_water', 125), 1800, 750)
    .itemOutput(['tfc:powder/salt'])
    //16线做丝绸
    event.remove({type: 'tfc:loom',input:'minecraft:string',output:'tfc:silk_cloth' })
    event.recipes.tfc.loom('1x tfc:silk_cloth', '16x minecraft:string', 16, 'minecraft:block/white_wool')
    //机动 压 做精铁方坯
    event.recipes.create.pressing(
      [{ item: "tfc:refined_iron_bloom" }],
      [
        {
          type: "tfc:heatable",
          min_temp: 921,
          ingredient: {
            item: "tfc:raw_iron_bloom"
          }
        }
      ]
    )
    //机动 压 做锻铁锭
    event.recipes.create.pressing(
      [{ item: "tfc:metal/ingot/wrought_iron" }],
      [
        {
          type: "tfc:heatable",
          min_temp: 921,
          ingredient: {
            item: "tfc:refined_iron_bloom"
          }
        }
      ]
    ) 
    //传送带
    event.shaped("2x create:belt_connector", ["   ", "LLL", "MRM"], {
      L: "#tfc:leather_knapping",
      M: "tfc:brass_mechanisms",
      R: "#forge:rods/wrought_iron"
    })  
    //机动树肥料
    event.shapeless("2x create:tree_fertilizer", [
      "#minecraft:small_flowers",
      "#minecraft:small_flowers",
      "tfc:compost", 
      "tfc:rotten_compost", 
    ])   
    //背包 堆叠t4
    event.shaped("1x sophisticatedbackpacks:stack_upgrade_tier_4", ["ABA", "BCB", "ABA"], {
      A: "tfc:metal/block/black_steel",
      B: "tfc:metal/ingot/black_steel",
      C: "sophisticatedbackpacks:stack_upgrade_tier_3"
    }) 
    //精密构件
    event.remove({ output: 'create:precision_mechanism' })
    event.recipes.create.sequenced_assembly([
      Item.of('create:precision_mechanism').withChance(100.0), // this is the item that will appear in JEI as the result
      Item.of('create:golden_sheet').withChance(8.0), // the rest of these items will be part of the scrap
      Item.of('create:andesite_alloy').withChance(8.0),
      Item.of('create:cogwheel').withChance(5.0),
      Item.of('create:shaft').withChance(2.0),
      Item.of('create:crushed_gold_ore').withChance(2.0),
      Item.of('2x minecraft:gold_nugget').withChance(2.0),
      'tfc:metal/rod/cast_iron',
      'minecraft:clock'
    ], 'create:golden_sheet', [ // 'create:golden_sheet' is the input
      // the transitional item set by `transitionalItem('create:incomplete_large_cogwheel')` is the item used during the intermediate stages of the assembly
      event.recipes.createDeploying('create:incomplete_precision_mechanism', ['create:incomplete_precision_mechanism', 'create:cogwheel']),
      // like a normal recipe function, is used as a sequence step in this array. Input and output have the transitional item
      event.recipes.createDeploying('create:incomplete_precision_mechanism', ['create:incomplete_precision_mechanism', 'create:large_cogwheel']),
      event.recipes.createDeploying('create:incomplete_precision_mechanism', ['create:incomplete_precision_mechanism', 'tfc:metal/ingot/wrought_iron'])
    ]).transitionalItem('create:incomplete_precision_mechanism').loops(8) // set the transitional item and the number of loops
  
  }
)

