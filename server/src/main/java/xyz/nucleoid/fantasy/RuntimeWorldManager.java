package xyz.nucleoid.fantasy;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;

import java.io.File;
import java.io.IOException;

final class RuntimeWorldManager {
    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;

    RuntimeWorldManager(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;
    }

    RuntimeWorld add(ResourceKey<Level> worldKey, RuntimeWorldConfig config, RuntimeWorld.Style style) {
        LevelStem options = config.createDimensionOptions(this.server);

        if (style == RuntimeWorld.Style.TEMPORARY) {
            ((FantasyDimensionOptions) (Object) options).fantasy$setSave(false);
        }
        ((FantasyDimensionOptions) (Object) options).fantasy$setSaveProperties(false);

        MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
        boolean isFrozen = ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$isFrozen();
        ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$setFrozen(false);

        var key = ResourceKey.create(Registries.LEVEL_STEM, worldKey.location());
        if(!dimensionsRegistry.containsKey(key)) {
            dimensionsRegistry.register(key, options, Lifecycle.stable());
        }
        ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$setFrozen(isFrozen);

        RuntimeWorld world = config.getWorldConstructor().createWorld(this.server, worldKey, config, style);

        this.serverAccess.getLevels().put(world.dimension(), world);
        ServerWorldEvents.LOAD.invoker().onWorldLoad(this.server, world);

        // tick the world to ensure it is ready for use right away
        world.tick(() -> true);

        return world;
    }

    void delete(ServerLevel world) {
        ResourceKey<Level> dimensionKey = world.dimension();

        if (this.serverAccess.getLevels().remove(dimensionKey, world)) {
            ServerWorldEvents.UNLOAD.invoker().onWorldUnload(this.server, world);

            MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
            RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.location());

            LevelStorageSource.LevelStorageAccess session = this.serverAccess.getStorageSource();
            File worldDirectory = session.getDimensionPath(dimensionKey).toFile();
            if (worldDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(worldDirectory);
                } catch (IOException e) {
                    Fantasy.LOGGER.warn("Failed to delete world directory", e);
                    try {
                        FileUtils.forceDeleteOnExit(worldDirectory);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    void unload(ServerLevel world) {
        ResourceKey<Level> dimensionKey = world.dimension();

        if (this.serverAccess.getLevels().remove(dimensionKey, world)) {
            world.save(new ProgressListener() {
                @Override
                public void progressStartNoAbort(Component title) {}

                @Override
                public void progressStart(Component title) {}

                @Override
                public void progressStage(Component task) {}

                @Override
                public void progressStagePercentage(int percentage) {}

                @Override
                public void stop() {
                    ServerWorldEvents.UNLOAD.invoker().onWorldUnload(RuntimeWorldManager.this.server, world);

                    MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(RuntimeWorldManager.this.server);
                    RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.location());
                }
            }, true, false);
        }
    }

    private static MappedRegistry<LevelStem> getDimensionsRegistry(MinecraftServer server) {
        RegistryAccess registryManager = server.registries().compositeAccess();
        return (MappedRegistry<LevelStem>) registryManager.registryOrThrow(Registries.LEVEL_STEM);
    }
}
