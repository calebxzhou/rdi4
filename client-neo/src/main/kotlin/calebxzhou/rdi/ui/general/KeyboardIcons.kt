package calebxzhou.rdi.ui.general

import net.minecraft.resources.ResourceLocation

object KeyboardIcons {
    operator fun get(name: String): ResourceLocation {
        return ResourceLocation("rdi","textures/gui/icons/keyboard/${name}.png")
    }
}