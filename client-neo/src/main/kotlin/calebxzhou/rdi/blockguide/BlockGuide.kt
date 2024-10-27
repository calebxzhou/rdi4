package calebxzhou.rdi.blockguide

import calebxzhou.rdi.common.BlockOperation
import calebxzhou.rdi.logger
import calebxzhou.rdi.nav.OmniNavi.posNow
import calebxzhou.rdi.uiguide.UiGuide
import calebxzhou.rdi.uiguide.UiGuide.Companion
import calebxzhou.rdi.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.event.RenderLevelStageEvent
import org.checkerframework.checker.units.qual.m

fun blockGuide(builder: BlockGuide.Builder.() -> Unit) {
    BlockGuide.Builder().apply(builder).build().start()
}

class BlockGuide(val steps: List<Step>) {
    private var stepIndex = -1
    private val stepNow
        get() = steps.getOrNull(stepIndex)

    data class Step(
        val pos: BlockPos,
        val type: BlockOperation,
        //目标方块状态 默认是空气
        val targetState: BlockState = Blocks.AIR.defaultBlockState(),
        //完成条件 client tick每次检查
        val okCond: (Level) -> Boolean = { it.blockIs(pos, targetState) }
    ) {
        val nowState: BlockState
            get() = mc.level!!.getBlockState(pos)
        val itemStack
            get() = ItemStack(nowState.block)
        val model
            get() = mc.itemRenderer.getModel(itemStack, mc.level, mc.player, 0)
        val displayName
            get() = itemStack.hoverName
        val text = when (type) {
            BlockOperation.PLACE -> "在绿框处放置"
            BlockOperation.DESTROY -> "破坏红框处的"
            BlockOperation.INTERACT -> "右键点击蓝框处的"
        }
    }

    class Builder {
        val steps = arrayListOf<Step>()
        fun place(pos: BlockPos, block: Block) {
            steps += Step(pos, BlockOperation.PLACE, block.defaultBlockState()) { it.blockIs(pos, block) }
        }

        fun place(pos: BlockPos, state: BlockState) {
            steps += Step(pos, BlockOperation.PLACE, state)
        }

        fun destroy(pos: BlockPos) {
            steps += Step(pos, BlockOperation.DESTROY) { it.isAir(pos) }
        }

        fun build(): BlockGuide {
            return BlockGuide(steps)
        }
    }

    fun renderGui(guiGraphics: GuiGraphics) {
        stepNow?.let { step ->
            guiGraphics.matrixOp {
                translate(0.0, 24.0, 100.0)
                guiGraphics.fill(0, 0, mcUIWidth/2, 20, 0x66000000.toInt())
                val width = guiGraphics.drawString(mcFont, step.text, 10, 6, WHITE)
                translate(width.toDouble()+2, 24.0, 100.0)
                mc.itemRenderer.render(
                    step.itemStack,
                    ItemDisplayContext.GUI,
                    false,
                    guiGraphics.pose(),
                    guiGraphics.bufferSource(),
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    step.model
                )
                guiGraphics.drawString(mcFont,step.displayName,0,0, WHITE)
            }

        }
    }

    fun render(e: RenderLevelStageEvent) {
        stepNow?.let {
            val buff = mc.renderBuffers().bufferSource()
            val level = mc.level!!
            val poseStack = e.poseStack
            val partialTick = e.partialTick
            val entity = e.camera.entity
            val camPos = e.camera.position

            val bPos = it.pos
            when (it.type) {
                BlockOperation.PLACE -> {
                    level.renderBlockOutline(
                        poseStack,
                        buff.getBuffer(RenderType.lines()),
                        entity,
                        camPos.x(),
                        camPos.y(),
                        camPos.z(),
                        bPos,
                        it.targetState, 0f, 1f, 0f, 1f
                    )
                }

                BlockOperation.DESTROY -> {
                    level.renderBlockOutline(
                        poseStack,
                        buff.getBuffer(RenderType.lineStrip()),
                        entity,
                        camPos.x(),
                        camPos.y(),
                        camPos.z(),
                        bPos,
                        it.targetState, 1f, 0f, 0f, 1f
                    )
                }

                BlockOperation.INTERACT -> {
                    level.renderBlockOutline(
                        poseStack,
                        buff.getBuffer(RenderType.lines()),
                        entity,
                        camPos.x(),
                        camPos.y(),
                        camPos.z(),
                        bPos,
                        it.targetState, 0f, 0f, 1f, 0.6f
                    )
                }
            }
        }
    }

    fun tick(level: Level) {
        stepNow?.let {
            if (it.okCond(level)) {
                next()
                return@let
            }

        } ?: stop()
    }

    fun start() {
        logger.info("block guide started ${steps.size}steps")
        next()
        now = this
    }

    fun stop() {
        logger.info("block guide stopped")
        now = null
    }

    fun next() {
        stepIndex++
        stepNow?.let {

        } ?: stop()
    }

    companion object {
        var now: BlockGuide? = null
            private set
        val isOn
            get() = now != null
    }
}