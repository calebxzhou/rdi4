package calebxzhou.rdi.mixin.ae2;

import appeng.datagen.providers.tags.BiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * calebxzhou @ 2024-10-14 13:23
 */
@Mixin(BiomeTagsProvider.class)
public class mGenMeteoriteEverywhere {
    @Redirect(method = "addTags",at= @At(value = "INVOKE", target = "Lnet/minecraft/data/tags/TagsProvider$TagAppender;addOptionalTag(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/data/tags/TagsProvider$TagAppender;"))
    private TagsProvider.TagAppender  genMet(TagsProvider.TagAppender instance, ResourceLocation pLocation){
        return instance;//.addOptionalTag(new ResourceLocation("aether","is_aether"));
    }
}
