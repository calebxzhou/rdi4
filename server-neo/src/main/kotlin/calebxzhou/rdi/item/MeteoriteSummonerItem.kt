package calebxzhou.rdi.item

import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.mcs
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.structure.BoundingBox
import org.checkerframework.checker.units.qual.m
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.ceil

class MeteoriteSummonerItem() : Item(Item.Properties().stacksTo(1)) {
    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack?> {

        return super.use(pLevel, pPlayer, pUsedHand)
    }



}

