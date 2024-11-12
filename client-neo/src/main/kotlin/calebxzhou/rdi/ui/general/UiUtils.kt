package calebxzhou.rdi.ui.general

import calebxzhou.rdi.util.matrixOp
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcFont
import calebxzhou.rdi.util.mcText
import com.mojang.blaze3d.platform.Lighting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.joml.Matrix4f

/**
 * calebxzhou @ 2024-11-02 23:14
 */
fun GuiGraphics.renderItemStack(itemStack: ItemStack, width: Int = 16, height: Int = 16){
    val pose = pose()
    val bakedmodel: BakedModel = mc.itemRenderer.getModel(itemStack, null, null, 1)
    //翻过来
    pose.mulPoseMatrix(Matrix4f().scaling(1.0f, -1.0f, 1.0f))
    pose.scale(width.toFloat(), height.toFloat(), width.toFloat())
    val flag = true//!bakedmodel.usesBlockLight()
    if (flag) {
        Lighting.setupForFlatItems()
    }

    mc.itemRenderer.render(
        itemStack, ItemDisplayContext.GUI, false,
        pose,
        bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel
    )
    flush()
    if (flag) {
        Lighting.setupFor3DItems()
    }
    //渲染数量
    pose.matrixOp {
        pose.translate(0.0f, 0.0f, 200.0f)
        drawString(mcFont, mcText("${itemStack.count}"), width - 10, height - 10, 16777215, true)
    }
}