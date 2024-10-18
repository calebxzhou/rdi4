package calebxzhou.rdi.jade

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Block
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class EnglishDisplayPlugin : IWailaPlugin {
    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(EnglishDisplayProvider.INSTANCE,Block::class.java)
        registration.registerEntityComponent(EnglishDisplayProvider.INSTANCE,Entity::class.java)

    }
}