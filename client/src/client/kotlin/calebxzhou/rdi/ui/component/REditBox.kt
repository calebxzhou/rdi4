package calebxzhou.rdi.ui.component

import calebxzhou.rdi.mixin.client.AEditBox
import calebxzhou.rdi.util.mcFont
import net.minecraft.SharedConstants
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component

typealias REditBoxValidator = (REditBox) -> REditBoxValidationResult
val REQUIRED_VALIDATOR: REditBoxValidator = {box: REditBox ->
    if(box.value.isNullOrBlank()){
        REditBoxValidationResult(false,"必须填写${box.label}")
    }else{
        REditBoxValidationResult(true)
    }
}
open class REditBox(
     val label: String,
     x: Int,
     y: Int,
     width: Int,
     val length: Int = 128,
    val validator: REditBoxValidator = REQUIRED_VALIDATOR
) : EditBox(mcFont(), x, y, width, 20, Component.literal(label)) {
    constructor(label: String, length: Int) : this(label, 0, 0, length * 10, length)
    constructor(label: String, length: Int,validator: REditBoxValidator) : this(label, 0, 0, length * 10, length,validator)

    init {
        setHint(Component.literal(label))
        setMaxLength(length)
    }

    var isNumberOnly = false
    override fun charTyped(codePoint: Char, modifiers: Int): Boolean {

        if (!this.canConsumeInput()) {
            return false
        } else if (SharedConstants.isAllowedChatCharacter(codePoint)) {
            if ((this as AEditBox).isEditable) {
                if (isNumberOnly) {
                    if (codePoint.isDigit()) {
                        this.insertText(codePoint.toString())
                    }

                } else {

                    this.insertText(codePoint.toString())
                }
            }

            return true
        } else {
            return false
        }
    }
    fun validate(): REditBoxValidationResult{
        return validator(this)
    }
}