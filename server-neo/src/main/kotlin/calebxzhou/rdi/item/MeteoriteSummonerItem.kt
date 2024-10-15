package calebxzhou.rdi.item

import appeng.debug.MeteoritePlacerItem
import appeng.util.Platform
import appeng.worldgen.meteorite.CraterType
import appeng.worldgen.meteorite.MeteoritePlacer
import appeng.worldgen.meteorite.debug.MeteoriteSpawner
import calebxzhou.rdi.util.mcText
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

    override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
        val player = context.player as ServerPlayer? ?: return InteractionResult.PASS
        val level = context.level as ServerLevel
        if (level.isClientSide) return InteractionResult.PASS
        val pos = context.clickedPos

        // See MeteoriteStructure for original code
        val coreRadius = level.getRandom().nextFloat() * 6.0f + 2
        val pureCrater = level.getRandom().nextFloat() > 0.5f
        val craterType = CraterType.entries.random()
        val spawner = MeteoriteSpawner()
        val spawned = spawner.trySpawnMeteoriteAtSuitableHeight(
            level, pos, coreRadius, craterType,
            pureCrater
        )

        if (spawned == null) {
            player.sendSystemMessage(mcText("位置不合适"))
            return InteractionResult.FAIL
        }
        player.sendSystemMessage(mcText("30秒后 陨石会砸在${pos.toShortString()}"))
        Timer().schedule(30000) {

            // Since we don't know yet if the meteorite will be underground or not,
            // we have to assume maximum size
            val range = ceil(((coreRadius * 2 + 5) * 5f).toDouble()).toInt()

            val boundingBox = BoundingBox(
                pos.x - range, pos.y - 10, pos.z - range,
                pos.x + range, pos.y + 10, pos.z + range
            )

            MeteoritePlacer.place(level, spawned, boundingBox, level.random)

            player.sendSystemMessage(mcText("陨石已降落"))
            player.connection.send(
                ClientboundSoundPacket(
                    Holder.direct(SoundEvents.GENERIC_EXPLODE), SoundSource.PLAYERS, pos.x.toDouble(),
                    pos.y.toDouble(),
                    pos.z.toDouble(), 100f, 1f, 0L
                )
            )

            // The placer will not send chunks to the player since it's used as part
            // of world-gen normally, so we'll have to do it ourselves. Since this
            // is a debug tool, we'll not care about being terribly efficient here
            ChunkPos.rangeClosed(ChunkPos(spawned.pos), 1).forEach { cp: ChunkPos ->
                val c = level.getChunk(cp.x, cp.z)
                player.connection.send(Platform.getFullChunkPacket(c))
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide())
    }

}

