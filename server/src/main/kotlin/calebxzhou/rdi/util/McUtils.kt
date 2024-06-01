package calebxzhou.rdi.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.MinecraftServer

/**
 * calebxzhou @ 2024-06-01 21:36
 */
val mcText: (String) -> MutableComponent = {
    Component.literal(it)
}
lateinit var mc: MinecraftServer