package calebxzhou.rdi.text

import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.StringDecomposer
import org.joml.Matrix4f

/*
渲染带小图标的text
{{back}} => 不带冒号 读取rdi:textures/gui/icons/back.png
{{tfc:thatch}} => 带冒号 读取itemRenderer
 */
/*
object RichTextRenderer {
    val REGEX = "\\{\\{(.*?)}}".toRegex()

    fun extractText(input: String): String? {
        val matchResult = REGEX.find(input)
        return matchResult?.groups?.get(1)?.value
    }

}*/
