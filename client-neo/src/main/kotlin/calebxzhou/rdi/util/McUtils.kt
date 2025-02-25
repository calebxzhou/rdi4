package calebxzhou.rdi.util

import calebxzhou.rdi.Const
import calebxzhou.rdi.lang.EnglishStorage
import calebxzhou.rdi.logger
import calebxzhou.rdi.ui.RMessageLevel
import calebxzhou.rdi.ui.general.RToast
import calebxzhou.rdi.ui.screen.RTitleScreen
import com.mojang.blaze3d.platform.InputConstants
import net.dries007.tfc.common.blocks.rock.RockCategory
import net.dries007.tfc.common.capabilities.food.TFCFoodData
import net.dries007.tfc.common.items.TFCItems
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.server.IntegratedServer
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.chunk.LevelChunkSection
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import org.anti_ad.mc.common.vanilla.alias.ForgeRegistries
import org.lwjgl.glfw.GLFW
import org.lwjgl.util.tinyfd.TinyFileDialogs
import snownee.jade.overlay.RayTracing
import kotlin.math.roundToInt

val mc: Minecraft
    get() = Minecraft.getInstance() ?: run {
        throw IllegalStateException("Minecraft Not Start !")
    }
val mcs: IntegratedServer?
    get() = mc.singleplayerServer
val isMcStarted
    get() = Minecraft.getInstance() != null

fun mcMainThread(run: () -> Unit) {
    mc.execute(run)
}


val Player.thrist: Float
    get() {
        val d = foodData
        return if (d is TFCFoodData)
            d.thirst
        else 0f
    }

fun asset(path: String) = ResourceLocation(Const.MODID, path)
fun MinecraftServer.executeCommand(cmd: String) {
    commands.performPrefixedCommand(createCommandSourceStack(), cmd)
}

fun mcTick(sec: Int): Int {
    return sec * 20
}

fun String.toMcText() = mcText(this)
fun mcText(str: String? = null): MutableComponent {
    return str?.let {
        Component.literal(it)
    } ?: Component.empty()
}
fun MutableComponent.hoverText(str:String): MutableComponent {
    withStyle(
        Style.EMPTY
            .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, mcText(str)))
    )
    return this
}
fun MutableComponent.clickCommand(cmd: String) : MutableComponent{
    hoverText("点击运行指令")
    withStyle(Style.EMPTY.withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd)))
    return this
}
fun MutableComponent.clickCopy(text: String) : MutableComponent{
    hoverText("点击复制")
    withStyle(Style.EMPTY.withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text)))
    return this
}
fun MutableComponent.clickBrowse(url: String) : MutableComponent{
    hoverText("点击打开链接")
    withStyle(Style.EMPTY.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, url)))
    return this
}

infix fun Minecraft.go(screen: Screen?) {
    execute {
        setScreen(screen)
    }
}
fun Minecraft.goHome(){
    mc go RTitleScreen()
}
infix fun Minecraft.titled(title: String) {
    mc.window.setTitle(title)
}

infix fun Minecraft.pressingKey(keyCode: Int): Boolean {
    return InputConstants.isKeyDown(mcWindowHandle, keyCode)
}
val Minecraft.pressingEnter
    get() =  this pressingKey InputConstants.KEY_RETURN || this pressingKey InputConstants.KEY_NUMPADENTER

operator fun MutableComponent.plus(component: Component): MutableComponent {
    return append(component)
}

operator fun MutableComponent.plus(component: String): MutableComponent {
    return append(component)
}



val Entity.isJumping
    get() = deltaMovement.y > 0

fun Entity.teleportTo(entity: Entity) {
    teleportTo(entity.x, entity.y, entity.z)
}

val Player.lookingAtBlock: BlockState?
    get() {
        val hit = pick(20.0, 0.0f, true)
        if (hit.type == HitResult.Type.BLOCK) {
            val bpos = (hit as BlockHitResult).blockPos
            val bstate = level().getBlockState(bpos)
            return bstate
        }
        return null
    }
val Player.lookingAtBlockEntity: BlockEntity?
    get() {
        val hit = pick(20.0, 0.0f, true)
        if (hit.type == HitResult.Type.BLOCK) {
            val bpos = (hit as BlockHitResult).blockPos
            val bstate = level().getBlockEntity(bpos)
            return bstate
        }
        return null
    }
//轻量化itemstack 只有物品和数量
typealias LiteItemStack = Pair<Item,Int>
fun Player.bagHas(cond: (ItemStack) -> Boolean): Boolean {
    return inventory.hasAnyMatching(cond)
}
fun Player.bagHas(tag: TagKey<Item>, count: Int = 1): Boolean {
    return bagHas { it.`is`(tag) && it.count >= count }
}
infix fun Player.bagHas(item: Item): Boolean {
    return bagHas(item to 1)
}
infix fun Player.bagHasStack(item: ItemStack): Boolean {
    return bagHas { it == item }
}
//数量大于等于
infix fun Player.bagHas(lis: LiteItemStack): Boolean {
    return this.bagHas { it.`is`(lis.first) && it.count >= lis.second }
}

infix fun Player.handHas(item: Item): Boolean {
    return mainHandItem.`is`(item)
}
val Player.handsAir
    get() = mainHandItem.isEmpty
infix fun Player.handHas(itemStack: LiteItemStack): Boolean {
    return mainHandItem.`is`(itemStack.first) && mainHandItem.count == itemStack.second
}
infix fun Player.handHas(itemTag: TagKey<Item>): Boolean {
    return mainHandItem.`is`(itemTag)
}
infix fun Player.feetOn(block: Block): Boolean {
    return level().getBlockState(blockPosition().below()).`is`(block)
}

infix fun Player.isLooking(block: Block): Boolean {
    return lookingAtBlock?.`is`(block) == true
}

infix fun Player.isLooking(item: Item): Boolean = lookingAtItemEntity?.item?.`is`(item) == true



infix fun Minecraft.justChatted(text: String): Boolean = mc.gui.chat.recentChat.lastOrNull()?.contains(text) == true
val Player.waterLevel
    get() = (foodData as? TFCFoodData)?.thirst?:0f
val Player.waterPercent
    get() = (waterLevel / TFCFoodData.MAX_THIRST * 100).roundToInt()
val Player.lookingAtItemEntity: ItemEntity?
    get() {
        val entity = lookingAtEntity
        return if (entity != null && entity is ItemEntity) {
            entity
        } else null
    }
val Player.lookingAtEntity: Entity?
    get() {
        val hit = RayTracing.INSTANCE.rayTrace(this, mc.gameMode?.pickRange?.toDouble() ?: 0.0, mc.frameTime)
        if (hit?.type == HitResult.Type.ENTITY) {
            return (hit as EntityHitResult).entity
        }
        return null
    }


fun Minecraft.addToast(toast: Toast) {
    toasts.addToast(toast)
}

fun Minecraft.addChatMessage(msg: String) {
    msg.split("\n").forEach {
        gui.chat.addMessage(mcText(it))
    }
}

fun Minecraft.addChatMessage(msg: Component) {
    gui.chat.addMessage(msg)
}
val Item.id
    get() = ForgeRegistries.ITEMS.getKey(this)

fun Minecraft.addHudMessage(msg: String) {
    gui.setOverlayMessage(mcText(msg), false);
}

fun Level.setBlock(pos: BlockPos, state: BlockState) {
    setBlock(pos, state, 2)
}
val Item.chineseName
    get() = this.getName(defaultInstance) as MutableComponent
val Item.englishName
    get() = EnglishStorage[descriptionId].toMcText()
fun Level.setBlock(pos: BlockPos, block: Block) {
    setBlock(pos, block.defaultBlockState())
}
fun Level.setBlock(block: Block,vararg pos: BlockPos,) {
    setBlock(block.defaultBlockState(),*pos)
}
fun Level.setBlock(block: BlockState,vararg pos: BlockPos,) {
    pos.forEach {
    setBlock(it, block)
    }
}
val ServerPlayer.serverLevel
    get() = level() as ServerLevel

fun ServerLevel.loadStructure(name: String, pos: BlockPos): Boolean {
    val mgr = structureManager
    val templateO = mgr.get(asset(name))
    if (templateO.isEmpty) {
        logger.warn("找不到结构 $name")
        return false
    }
    val template = templateO.get()
    template.placeInWorld(this, pos, pos, StructurePlaceSettings(), random, 2)

    return true
}
fun openLink(url: String){
    Util.getPlatform().openUri(url)
}
fun LevelChunkSection.forEachBlock(todo: (BlockState) -> Unit) {
    for (x in 0..15)
        for (y in 0..15)
            for (z in 0..15)
                todo(this.getBlockState(x, y, z))
}

fun Player.playNote() {
    level().playSeededSound(
        null as Player?,
        x + 0.5,
        y + 0.5,
        z + 0.5,
        NoteBlockInstrument.BELL.soundEvent,
        SoundSource.RECORDS,
        3.0f,
        1f,
        level().random.nextLong()
    )

}

fun Level.isAir(pos: BlockPos): Boolean {
    return getBlockState(pos).isAir
}

fun Level.blockIs(pos: BlockPos, block: Block): Boolean {
    return getBlockState(pos).`is`(block)
}

fun Level.blockIs(pos: BlockPos, state: BlockState): Boolean {
    return getBlockState(pos) == (state)
}

fun mcButton(text: String, x: Int, y: Int, w: Int, h: Int, onPress: Button.OnPress): Button {
    return mcButton(mcText(text), x, y, w, h, onPress);
}

fun mcButton(text: Component, x: Int, y: Int, w: Int, h: Int, onPress: Button.OnPress): Button {
    return Button.builder(text, onPress).bounds(x, y, w, h).build();
}



fun popupInfo(msg: String) {
    TinyFileDialogs.tinyfd_notifyPopup(msg, "RDI提示您", "info");
}

fun toastOk(msg: String) {
    mc.toasts.addToast(RToast(RMessageLevel.OK, msg))
}

fun showYesNoBox(msg: String): Boolean {
    return TinyFileDialogs.tinyfd_messageBox("提示", msg, "yesno", "question", false)
}

fun showToast(msg: String) {
    mc.toasts.addToast(RToast(RMessageLevel.INFO, msg))
}

fun mcTextWidthOf(text: String): Int {
    return mc.font.width(text)
}

fun mcTextWidthOf(text: Component): Int {
    return mc.font.width(text)
}

fun copyToClipboard(s: String) {
    GLFW.glfwSetClipboardString(mcWindowHandle, s)
}
val RockCategory.ItemType.item
    get() =     TFCItems.ROCK_TOOLS[RockCategory.METAMORPHIC]!![this]!!.get()

object McUtils {
    @JvmStatic
    fun getWindowSize(screenW: Int, screenH: Int): Pair<Int, Int> {
        if (screenW >= 3840 && screenH >= 2160)
            return 1920 to 1080
        if (screenW >= 1920 && screenH >= 1080)
            return 1600 to 900
        if (screenW >= 1600 && screenH >= 900)
            return 1280 to 720
        return 800 to 480
    }

    @JvmStatic
    val mcHwnd
        get() = mcWindowHandle

    @JvmStatic
    val emptyButton = Button.Builder(
        Component.empty()
    ) { _: Button? -> }.build()
}