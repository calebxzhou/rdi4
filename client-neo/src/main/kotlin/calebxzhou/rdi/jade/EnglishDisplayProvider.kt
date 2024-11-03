package calebxzhou.rdi.jade

import calebxzhou.rdi.lang.EnglishStorage
import calebxzhou.rdi.util.mcText
import calebxzhou.rdi.util.asset
import net.minecraft.ChatFormatting
import snownee.jade.api.*
import snownee.jade.api.config.IPluginConfig

enum class EnglishDisplayProvider : IBlockComponentProvider,IEntityComponentProvider{
    INSTANCE;

    override fun getUid() = asset("jade/english_display")

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        appendTooltip(tooltip,accessor.block.descriptionId)
    }

    override fun appendTooltip(tooltip: ITooltip, accessor: EntityAccessor, config: IPluginConfig) {
        appendTooltip(tooltip,accessor.entity.type.descriptionId)
    }
    private  fun appendTooltip(tooltip: ITooltip,langKey:String ){
        tooltip.add(mcText( EnglishStorage[langKey]).withStyle(ChatFormatting.ITALIC))
    }
    override fun getDefaultPriority(): Int {
        return TooltipPosition.HEAD + 1
    }
}