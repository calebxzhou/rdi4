package calebxzhou.rdi.lan

import calebxzhou.rdi.util.go
import calebxzhou.rdi.util.mc
import net.minecraft.client.gui.screens.ShareToLanScreen
import net.minecraft.commands.Commands

/**
 * calebxzhou @ 2024-11-06 22:03
 */
object Lan {
    val cmd = Commands.literal("lan").executes {
        mc go ShareToLanScreen(null)
        1
    }
}