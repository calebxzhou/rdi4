package calebxzhou.rdi.mixin

import calebxzhou.rdi.logger
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class RMixinPlugin: IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String) {
    }

    override fun getRefMapperConfig(): String {
        TODO("Not yet implemented")
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        logger.info("${mixinClassName} => ${targetClassName}")
        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {

    }

    override fun getMixins(): MutableList<String> {
        return arrayListOf()
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }
}