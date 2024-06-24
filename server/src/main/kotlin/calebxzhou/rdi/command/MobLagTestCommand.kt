package calebxzhou.rdi.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items

object MobLagTestCommand {
    val builder: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("mob_lag_test").executes { cmd ->
            if (cmd.source.isPlayer) {
                val player = cmd.source.player!!
                for (i in 0..500) {
                    val ent = EntityType.ITEM.create(player.level())?.apply {
                        item = Items.ACACIA_BOAT.defaultInstance
                    }

                    ent?.moveTo(player.position().subtract(0.0, 2.0, 0.0))
                    player.serverLevel().addFreshEntity(ent)
                }
            }
            return@executes 1
        }
}