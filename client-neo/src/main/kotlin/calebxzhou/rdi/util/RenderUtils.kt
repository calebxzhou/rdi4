package calebxzhou.rdi.util

import calebxzhou.rdi.common.WHITE
import com.mojang.blaze3d.platform.Lighting
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Matrix4f

/**
 * calebxzhou @ 2024-11-02 23:14
 */
val mcFont
    get() = mc.font
val mcWindowHandle
    get() = mc.window.window
val mcUIWidth
    get() = mc.window.guiScaledWidth
val mcUIHeight
    get() = mc.window.guiScaledHeight
val mcUIScale
    get() = mc.window.guiScale

fun GuiGraphics.renderItemStack(x: Int = 0, y: Int = 0, itemStack: ItemStack, width: Int = 16, height: Int = 16) {
    matrixOp {
        val bakedmodel: BakedModel = mc.itemRenderer.getModel(itemStack, null, null, 1)
        //水平翻转
        translate(x+width/2, y+height/3, 5)
        scale(width.toFloat(), height.toFloat(), 1f)
        mulPoseMatrix(Matrix4f().scaling(1.0f, -1.0f, 1.0f))
        Lighting.setupForFlatItems()
        mc.itemRenderer.render(
            itemStack, ItemDisplayContext.GUI, false,
            this,
            bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel
        )
        flush()
        Lighting.setupFor3DItems()
    }
}

fun GuiGraphics.drawString(
    x: Int = 0,
    y: Int = 0,
    text: String = "",
    comp: Component = text.toMcText(),
    color: Int = WHITE
): Int {
    return drawString(mcFont, comp, x, y, color, true)
}

fun GuiGraphics.matrixOp(handler: PoseStack.() -> Unit) {
    val stack = pose()
    stack.matrixOp(handler)
}

fun PoseStack.matrixOp(handler: PoseStack.() -> Unit) {
    pushPose()
    handler(this)
    popPose()
}

fun PoseStack.translate(x: Int, y: Int, z: Int) {
    translate(x.toFloat(), y.toFloat(), z.toFloat())
}
fun AbstractWidget.justify(){
    x = mcUIWidth/2-width/2
}
//全屏填充
infix fun GuiGraphics.fill(color: Int) {
    fill(0, 0, mcUIWidth, mcUIHeight, color)
}

fun Screen.drawTextAtCenter(gr: GuiGraphics, text: String, height: Int) {
    drawTextAtCenter(gr, text, height, WHITE)
}

fun Screen.drawTextAt(gr: GuiGraphics, text: String, x: Int, y: Int) {
    val font = mcFont
    gr.drawString(font, text, x, y, 0xFFFFFF, false);
}

fun BlockGetter.renderBlockOutline(
    stack: PoseStack,
    vConsumer: VertexConsumer,
    entity: Entity,
    camX: Double, camY: Double, camZ: Double, pos: BlockPos, state: BlockState,
    red: Float, green: Float, blue: Float, alpha: Float
) {
    renderShape(
        stack,
        vConsumer,
        state.getShape(this, pos, CollisionContext.of(entity)),
        pos.x.toDouble() - camX,
        pos.y.toDouble() - camY,
        pos.z.toDouble() - camZ,
        red, green, blue, alpha
    )

}

fun renderShape(
    poseStack: PoseStack,
    consumer: VertexConsumer,
    shape: VoxelShape,
    x: Double,
    y: Double,
    z: Double,
    red: Float,
    green: Float,
    blue: Float,
    alpha: Float
) {
    val pose = poseStack.last()
    shape.forAllEdges { x1, y1, z1, x2, y2, z2 ->
        var dx = (x2 - x1).toFloat()
        var dy = (y2 - y1).toFloat()
        var dz = (z2 - z1).toFloat()
        val f3 = Mth.sqrt(dx * dx + dy * dy + dz * dz)
        dx /= f3
        dy /= f3
        dz /= f3
        consumer.vertex(
            pose.pose(),
            (x1 + x).toFloat(),
            (y1 + y).toFloat(),
            (z1 + z).toFloat()
        ).color(red, green, blue, alpha).normal(pose.normal(), dx, dy, dz).endVertex()
        consumer.vertex(
            pose.pose(),
            (x2 + x).toFloat(),
            (y2 + y).toFloat(),
            (z2 + z).toFloat()
        ).color(red, green, blue, alpha).normal(pose.normal(), dx, dy, dz).endVertex()
    }
}


fun Screen.drawTextAtCenter(gr: GuiGraphics, text: String, height: Int, color: Int) {

    val font = mcFont
    gr.drawString(font, text, width / 2 - font.width(text) / 2, height, color, false);
}

fun Screen.drawTextAtCenter(gr: GuiGraphics, text: Component, height: Int) {

    val font = mcFont
    gr.drawString(font, text, width / 2 - font.width(text) / 2, height, 0xFFFFFF, false);
}