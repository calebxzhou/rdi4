package calebxzhou.rdi.util

import calebxzhou.rdi.Const
import calebxzhou.rdi.log
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.network.Connection
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import org.bson.types.ObjectId
import xyz.nucleoid.fantasy.Fantasy
import xyz.nucleoid.fantasy.RuntimeWorldConfig
import xyz.nucleoid.fantasy.RuntimeWorldHandle

/**
 * calebxzhou @ 2024-06-01 21:36
 */
val mcText: (String) -> MutableComponent = {
    Component.literal(it)
}
lateinit var mc: MinecraftServer
fun forEachEntity(todo: (ServerLevel, Entity) -> Unit) {

    try {
        mc.allLevels.forEach { it.allEntities.forEach { ent -> if (ent != null) todo(it, ent) } }
    } catch (e: Exception) {
        log.warn("移除实体出现错误：{}", e.toString())
    }
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

fun Fantasy.openIsland(iid: String): RuntimeWorldHandle {
    return getOrOpenPersistentWorld(ResourceLocation("rdi", "i$iid"), RuntimeWorldConfig())
}
fun Fantasy.openIsland(iid: ObjectId): RuntimeWorldHandle {
    return  openIsland(iid.toHexString())
}
fun ServerPlayer.teleportTo(level: ServerLevel, pos: BlockPos) {
    teleportTo(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 0f, 0f)
}
fun ServerPlayer.teleportTo(level: ServerLevel, posL: Long) {
    teleportTo(level,BlockPos.of(posL))
}
fun ServerPlayer.setSpawn(level: ServerLevel, pos: BlockPos) {
    setRespawnPosition(level.dimension(), pos, 0f, true, true)
}

fun ServerPlayer.reset() {
    experienceLevel = 0
    inventory.clearContent()
    setSpawn(mc.overworld(), Const.BASE_POS)
    kill()
}
fun Connection.preventLogin(reason: String){
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