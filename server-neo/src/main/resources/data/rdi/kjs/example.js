
ServerEvents.recipes(event => { 

  // 原版铁锭不合成
  event.remove({ output: 'minecraft:iron_ingot' })
  //mc铜锭全都换成tfc铜
  event.replaceInput(

    { input: 'minecraft:copper_ingot' }, // Arg 1: the filter
    'minecraft:copper_ingot',            // Arg 2: the item to replace
    'tfc:metal/ingot/copper'         // Arg 3: the item to replace it with
    // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
  )
  //原版铁锭全都换成铁矿辞
    event.replaceInput(

      { input: 'minecraft:iron_ingot' }, // Arg 1: the filter
      'minecraft:iron_ingot',            // Arg 2: the item to replace
      'tfc:metal/ingot/wrought_iron'         // Arg 3: the item to replace it with
      // Note: tagged fluid ingredients do not work on Fabric, but tagged items do.
    )
  //mc铁块-tfc锻铁块
  event.replaceInput(

    { input: 'minecraft:iron_block' }, // Arg 1: the filter
    'minecraft:iron_block',            // Arg 2: the item to replace
    'tfc:metal/block/wrought_iron'         // Arg 3: the item to replace it with
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
    
  }
)

ServerEvents.tags('item', event => {
  //tfc锻铁可作铁锭 
  event.add('forge:ingots/iron', 'tfc:metal/ingot/wrought_iron')
  //tfc胶水可作粘液球
  event.add('forge:slimeballs', 'tfc:glue')
  //mc铁锭不作为#铸铁，防止锻铁焊接会变成铸铁双锭的bug
  event.remove('forge:ingots/cast_iron', 'minecraft:iron_ingot')
}
)