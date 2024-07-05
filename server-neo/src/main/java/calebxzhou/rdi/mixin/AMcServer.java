package calebxzhou.rdi.mixin;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MinecraftServer.class)
public interface AMcServer {
    @Accessor
    Map<ResourceKey<Level>, ServerLevel> getLevels();

    @Accessor
    LevelStorageSource.LevelStorageAccess getStorageSource();

    @Accessor
    LayeredRegistryAccess<RegistryLayer> getRegistries();
}
