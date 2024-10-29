package calebxzhou.rdi.blockguide

import calebxzhou.rdi.common.BlockOperation
import calebxzhou.rdi.logger
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
import org.joml.Matrix4f

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
        val nowItemStack
            get() = ItemStack(nowState.block)
        val nowModel
            get() = mc.itemRenderer.getModel(nowItemStack, mc.level, mc.player, 0)
        val nowDisplayName
            get() = nowItemStack.hoverName
        val targetItemStack
            get() = ItemStack(targetState.block)
        val targetModel
            get() = mc.itemRenderer.getModel(targetItemStack, mc.level, mc.player, 0)
        val targetDisplayName
            get() = targetItemStack.hoverName
        val text = when (type) {
            BlockOperation.PLACE -> "在绿框处放置"//之后的
            BlockOperation.DESTROY -> "破坏红框处的"//当前的
            BlockOperation.INTERACT -> "右键点击蓝框处的"//当前的
        }
        val state
            get() = when(type){
                BlockOperation.PLACE -> targetState
                BlockOperation.DESTROY -> nowState
                BlockOperation.INTERACT -> nowState
            }
        val model
            get() =when(type){
                BlockOperation.PLACE -> targetModel
                BlockOperation.DESTROY -> nowModel
                BlockOperation.INTERACT -> nowModel
            }
        val displayName
            get() = when(type){
                BlockOperation.PLACE -> targetDisplayName
                BlockOperation.DESTROY -> nowDisplayName
                BlockOperation.INTERACT -> nowDisplayName
            }
        val itemStack
            get() = when(type){
                BlockOperation.PLACE -> targetItemStack
                BlockOperation.DESTROY -> nowItemStack
                BlockOperation.INTERACT -> nowItemStack
            }
        fun render(guiGraphics: GuiGraphics){
            guiGraphics.matrixOp {
                translate(100.0, 24.0, 100.0)
                guiGraphics.fill(0, 0, mcUIWidth/2, 20, 0x66000000.toInt())
                translate(0.0, 6.0, 100.0)
                val width = guiGraphics.drawString(mcFont, text, 10, 0, WHITE)
                guiGraphics.drawString(mcFont,displayName,width+16+4,0, WHITE)
                translate(width.toDouble()+8, .0, 100.0)
                translate(0.0, 4.0, 100.0)
                mulPoseMatrix(Matrix4f().scaling(1.0f, -1.0f, 1.0f))
                scale(16.0f,16.0f,100f)
                mc.itemRenderer.render(
                    itemStack,
                    ItemDisplayContext.GUI,
                    false,
                    guiGraphics.pose(),
                    guiGraphics.bufferSource(),
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    model
                )

            }
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
        stepNow?.render(guiGraphics)
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
                        it.nowState, 1f, 0f, 0f, 1f
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
                        it.nowState, 0f, 0f, 1f, 1f
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
        val isOff get() = now==null
    }
}