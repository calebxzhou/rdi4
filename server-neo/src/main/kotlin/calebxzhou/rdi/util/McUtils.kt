package calebxzhou.rdi.util

import calebxzhou.rdi.Const
import calebxzhou.rdi.log
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.Connection
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

/**
 * calebxzhou @ 2024-06-01 21:36
 */
val mcText: (String) -> MutableComponent = {
    Component.literal(it)
}
lateinit var mcs: MinecraftServer
fun forEachEntity(todo: (ServerLevel, Entity) -> Unit) {

    try {
        mcs.allLevels.forEach { it.allEntities.forEach { ent -> if (ent != null) todo(it, ent) } }
    } catch (e: Exception) {
        log.warn("移除实体出现错误：{}", e.toString())
    }
}

fun String.toMcText() = mcText(this)
fun mcText(str: String? = null): MutableComponent {
    return str?.let {
        Component.literal(it)
    } ?: Component.empty()
}

fun CommandSourceStack.chat(msg: String) {
    msg.split("\n").forEach { sendSystemMessage(mcText(it)) }
}

fun ServerPlayer.chat(msg: String) {
    msg.split("\n").forEach { sendSystemMessage(mcText(it)) }
}

fun ServerPlayer.ok() {
    sendSystemMessage(mcText("成功"))
}

fun ServerPlayer.ok(msg: String) {
    sendSystemMessage(mcText("成功：$msg"))
}

fun ServerPlayer.err(msg: String) {
    sendSystemMessage(mcText("错误：$msg"))
}

fun ServerLevel.placeBlock(blockPos: BlockPos, block: Block) {
    setBlockAndUpdate(blockPos, block.defaultBlockState())
}

fun ServerPlayer.teleportTo(level: ServerLevel, pos: BlockPos) {
    teleportTo(level, pos.x + 0.5, pos.y + 2.0, pos.z + 0.5, 0f, 0f)
}

fun findDimension(dim: String): ServerLevel? {
    val reloc = dim.split(":").let { ResourceLocation(it[0], it[1]) }
    val rekey = ResourceKey.create(Registries.DIMENSION, reloc)
    return mcs.getLevel(rekey)
}

fun ServerPlayer.teleport(dim: String, x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
    findDimension(dim)?.let {
        teleportTo(it, x, y, z, yaw, pitch)
    } ?: log.warn("找不到维度$dim")
}

fun ServerPlayer.teleport(dim: String, posL: Long) {
    BlockPos.of(posL).run {
        teleport(dim, x.toDouble(), y.toDouble(), z.toDouble(), 0f, 0f)
    }
}

infix fun ServerPlayer.teleport(player: ServerPlayer) {
    teleportTo(player.serverLevel(), player.x, player.y, player.z, player.yRot, player.xRot)
}

fun ServerPlayer.setSpawn(level: ServerLevel, pos: BlockPos) {
    setRespawnPosition(level.dimension(), pos, 0f, true, true)
}


fun ServerPlayer.slowfall() {
    addEffect(MobEffectInstance(MobEffects.SLOW_FALLING, 200, 5))
}

fun ServerPlayer.reset() {
    experienceLevel = 0
    inventory.clearContent()
    setSpawn(mcs.overworld(), Const.BASE_POS)
    kill()
}

val ServerPlayer.lookingBlock
    get() = pick(16.0, 1f, true).location.run { BlockPos(x.toInt(), y.toInt(), z.toInt()) }
val ServerPlayer.lookingBlockState
    get() = lookingBlock

        .run { serverLevel().getBlockState(this) }

fun Connection.preventLogin(reason: String) {
    send(ClientboundLoginDisconnectPacket(mcText(reason)))
}

val Level.dimensionName
    get() = dimension().location().toString()
val ServerPlayer.dimensionName
    get() = level().dimensionName
val ServerPlayer.nickname
    get() = displayName.string

object McUtils {

}