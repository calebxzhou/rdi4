package calebxzhou.rdi.item

import calebxzhou.rdi.Const
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object RItems {
    val REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Const.MODID)
    val METEORITE_SUMMONER = REGISTER.register<MeteoriteSummonerItem>("meteorite_summoner", ::MeteoriteSummonerItem)


}
