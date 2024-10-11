package calebxzhou.rdi.ui.general

import net.minecraft.resources.ResourceLocation

object Icons {
    operator fun get(name: String): ResourceLocation {
        return ResourceLocation("rdi","textures/gui/icons/${name}.png")
    }
}