package calebxzhou.rdi.nav

import calebxzhou.rdi.rAsync
import calebxzhou.rdi.util.*
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderStateShard.LineStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.CompositeRenderType
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.ResourceArgument
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.SectionPos
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.event.RenderLevelStageEvent
import org.joml.Matrix4f
import java.util.*
import kotlin.math.sqrt

//方块导航
object OmniNavi {
    var posNow: BlockPos? = null
        private set
    val isOn
        get() = posNow!=null
    val stateNow
        get() = mc.level?.getBlockState(posNow)
    val cmd: (CommandBuildContext) -> LiteralArgumentBuilder<CommandSourceStack> = { ctx: CommandBuildContext ->
        Commands.literal("omninavi").then(
            Commands.argument("1", StringArgumentType.string())
                .suggests { _, build ->
                    SharedSuggestionProvider.suggest(
                        arrayOf(
                            "entity",
                            "block",
                            "stop",
                        ), build
                    )
                }
                .executes {
                    val act = StringArgumentType.getString(it, "1")
                    when (act) {
                        "stop" -> {
                            reset()
                            mc.addChatMessage("已停止导航")
                        }
                    }
                    1
                }
                .then(
                    Commands.argument("blk", BlockStateArgument.block(ctx))
                        .executes {
                            val block = BlockStateArgument.getBlock(it, "blk").state.block
                            this += block
                            1
                        }
                )
                .then(
                    Commands.argument("ett", ResourceArgument.resource(ctx, Registries.ENTITY_TYPE))
                        .executes {
                            val ett = ResourceArgument.getSummonableEntityType(it, "entity")
                            this += ett
                            1
                        }
                )

        )
    }

    operator fun plusAssign(pos: BlockPos) {
        posNow = pos
    }

    operator fun plusAssign(block: BlockState) {
        navigate { blockState -> blockState == block }
    }

    operator fun plusAssign(entityType: Holder.Reference<EntityType<*>>) {
        //todo
    }

    operator fun plusAssign(block: Block) {
        navigate { blockState -> blockState.`is`(block) }
    }

    operator fun plusAssign(tag: TagKey<Block>) {
        navigate { blockState -> blockState.`is`(tag) }
    }

    fun navigate(
        condition: (BlockState) -> Boolean,
    ) {
        rAsync {
            val found = find(condition)
            if (found.isEmpty()) {
                mc.addChatMessage("没找到")
                return@rAsync
            }
            mc.player?.let { player ->
                val dist = { pos: BlockPos -> player.onPos.distSqr(pos) }
                val result = found.minBy { dist(it.key) }
                posNow = result.key
                mc.addChatMessage(
                    mcText("开始导航:") + result.value.block.name + "，坐标${result.key.toShortString()}，距离${
                        sqrt(
                            dist(result.key)
                        ).toFixed(1)
                    }m"
                )

            }
        }
    }

    private fun find(
        condition: (BlockState) -> Boolean,
        maxAmount: Int = 1
    ): HashMap<BlockPos, BlockState> {
        //遍历区块 寻找合适方块
        val found = hashMapOf<BlockPos, BlockState>()
        mc.player?.let { player ->
            val chunkX = player.chunkPosition().x
            val chunkZ = player.chunkPosition().z
            //chunkX-1 chunkX+1 chunkX-2 chunkX+2....
            val offsets = (0..8).flatMap { listOf(it, -it) }
            for (offsetX in offsets) {
                for (offsetZ in offsets) {
                    val cx = chunkX + offsetX
                    val cz = chunkZ + offsetZ
                    val cpos = ChunkPos(cx, cz)
                    val chunk = player.level().getChunk(cx, cz)
                    for (sy in chunk.minSection until chunk.maxSection) {
                        val section = chunk.getSection(chunk.getSectionIndexFromSectionY(sy))
                        for (x in 0..15)
                            for (y in 0..15)
                                for (z in 0..15) {

                                    val state = section.getBlockState(x, y, z)
                                    if (state != null && condition(state)) {
                                        val origin = SectionPos.of(chunk.pos, sy).origin()
                                        val bpos = origin.mutable().setWithOffset(origin, x, y, z)
                                        found += bpos to state
                                        if (found.size >= maxAmount) {
                                            return@let
                                        }
                                    }
                                }
                    }

                }

            }
        }
        return found
    }

    fun reset() {
        posNow = null
    }

    fun renderGui(guiGraphics: GuiGraphics) {
        /*posNow?.let { posNow ->
            guiGraphics.fill(0, 64, mcUIWidth, 84, 0x66000000.toInt())
            guiGraphics.drawCenteredString(mcFont, "导航到：${posNow.toShortString()}", mcUIWidth / 2, 70, WHITE)
        }*/
    }



    fun renderLevelStage(e: RenderLevelStageEvent) {
        val bufferSource = mc.renderBuffers().bufferSource()
        val level = mc.level!!
        val poseStack = e.poseStack
        val partialTick = e.partialTick
        val entity = e.camera.entity

        val camPos = e.camera.position
        if (e.stage == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {

        }

        if (e.stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            posNow?.let { posNow ->


                val x = posNow.x - camPos.x() + 0.5
                val y = posNow.y - camPos.y()
                val z = posNow.z - camPos.z() + 0.5
                level.renderBlockOutline(
                    poseStack,
                    bufferSource.getBuffer(RenderType.lineStrip()),
                    entity,
                    camPos.x(),
                    camPos.y(),
                    camPos.z(),
                    posNow,
                    level.getBlockState(posNow)
                )
                poseStack.matrixOp {
                    translate(x, y, z)
                    renderBeam(bufferSource, poseStack)
                }
            }
        }
    }

    private fun renderBeam(
        bufferSource: MultiBufferSource.BufferSource,
        poseStack: PoseStack
    ) {
        val afloat = FloatArray(16)
        val afloat1 = FloatArray(16)
        val f = 0.0f
        val f1 = 0.0f


        val vertexconsumer: VertexConsumer = bufferSource.getBuffer(RenderType.lightning())
        val matrix4f: Matrix4f = poseStack.last().pose()

        //粗细
        for (j in 0..1) {
            for (k in 1..1) {
                val l = 15
                val i1 = 0

                val x1 = afloat[l] - f
                val z1 = afloat1[l] - f1

                for (index in l downTo i1) {


                    val off1 = 0.1f + j.toFloat() * 0.2f

                    val off2 = 0.1f + j.toFloat() * 0.2f

                    quad(matrix4f, vertexconsumer, x1, z1, index, x1, z1, 1f, 0f, 1f, off1, off2, false, false, true, false)
                    quad(matrix4f, vertexconsumer, x1, z1, index, x1, z1, 0f, 1f, 0f, off1, off2, true, false, true, true)
                    quad(matrix4f, vertexconsumer, x1, z1, index, x1, z1, 0f, 1f, 1f, off1, off2, true, true, false, true)
                    quad(matrix4f, vertexconsumer, x1, z1, index, x1, z1, 1f, 1f, 0f, off1, off2, false, true, false, false)
                }
            }
        }
    }

    private fun quad(
        pMatrix: Matrix4f,
        pConsumer: VertexConsumer,
        pX1: Float,
        pZ1: Float,
        pIndex: Int,
        pX2: Float,
        pZ2: Float,
        pRed: Float,
        pGreen: Float,
        pBlue: Float,
        off1: Float,
        off2: Float,
        p1: Boolean,
        p2: Boolean,
        p3: Boolean,
        p4: Boolean
    ) {
        val alpha = 0.5f
        pConsumer.vertex(
            pMatrix,
            pX1 + (if (p1) off2 else -off2),
            (pIndex * 16).toFloat(),
            pZ1 + (if (p2) off2 else -off2)
        ).color(pRed, pGreen, pBlue, alpha).endVertex()
        pConsumer.vertex(
            pMatrix,
            pX2 + (if (p1) off1 else -off1),
            ((pIndex + 1) * 16).toFloat(),
            pZ2 + (if (p2) off1 else -off1)
        ).color(pRed, pGreen, pBlue, alpha).endVertex()
        pConsumer.vertex(
            pMatrix,
            pX2 + (if (p3) off1 else -off1),
            ((pIndex + 1) * 16).toFloat(),
            pZ2 + (if (p4) off1 else -off1)
        ).color(pRed, pGreen, pBlue, alpha).endVertex()
        pConsumer.vertex(
            pMatrix,
            pX1 + (if (p3) off2 else -off2),
            (pIndex * 16).toFloat(),
            pZ1 + (if (p4) off2 else -off2)
        ).color(pRed, pGreen, pBlue, alpha).endVertex()
    }

    fun tick() {
        posNow?.let { posNow ->
            mc.player?.let { player ->

                if (player.distanceToSqr(posNow.x.toDouble(), posNow.y.toDouble(), posNow.z.toDouble()) < 3) {
                    mc.addChatMessage(mcText("已到达"))

                    reset()
                }
            }
        }
    }

}