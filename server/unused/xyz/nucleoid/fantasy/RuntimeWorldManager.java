package xyz.nucleoid.fantasy;

import calebxzhou.rdi.mixin.AMcServer;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldLoadEvents;

import java.io.File;
import java.io.IOException;

public final class RuntimeWorldManager {
    private final MinecraftServer server;
    private final AMcServer serverAccess;

    RuntimeWorldManager(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (AMcServer) server;
    }

    IslandLevel add(ResourceKey<Level> worldKey) {
        LevelStem options = config.createDimensionOptions(this.server);

        if (style == IslandLevel.Style.TEMPORARY) {
            ((FantasyDimensionOptions) (Object) options).fantasy$setSave(false);
        }

        MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
        boolean isFrozen = ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$isFrozen();
        ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$setFrozen(false);
        //dimensionsRegistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location()), options, Lifecycle.stable());
		if(!dimensionsRegistry.containsKey(worldKey.location())){
			dimensionsRegistry.register(ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location()), options, Lifecycle.stable());
		}
        ((RemoveFromRegistry<?>) dimensionsRegistry).fantasy$setFrozen(isFrozen);

        IslandLevel world = new IslandLevel(worldKey, config, style);

        this.serverAccess.getLevels().put(world.dimension(), world);
		ServerWorldLoadEvents.LOAD.invoker().loadWorld(this.server, world);


        // tick the world to ensure it is ready for use right away
        world.tick(() -> true);

        return world;
    }
	boolean unload(ServerLevel world){
		ResourceKey<Level> dimensionKey = world.dimension();
		Fantasy.LOGGER.info("卸载维度中 {}", dimensionKey.toString());
		world.save(null,true,false);
		if (this.serverAccess.getLevels().remove(dimensionKey, world)) {
			ServerWorldLoadEvents.UNLOAD.invoker().unloadWorld(this.server, world);
			MappedRegistry<LevelStem> dimensionsRegistry = getDimensionsRegistry(this.server);
			RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.location());
			try {
				world.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Fantasy.LOGGER.info("成功卸载维度 {} ", dimensionKey.toString());
			dimensionKey=null;
			world=null;
			return true;
		}
		return false;
	}
    void delete(ServerLevel world) {
			if(!unload(world))
				return;
			ResourceKey<Level> dimensionKey = world.dimension();
			Fantasy.LOGGER.info("删除维度中 {}", dimensionKey.toString());
			LevelStorageSource.LevelStorageAccess session = this.serverAccess.getStorageSource();
			File worldDirectory = session.getDimensionPath(dimensionKey).toFile();
			if (worldDirectory.exists()) {
				try {
					FileUtils.deleteDirectory(worldDirectory);
				} catch (IOException e) {
					Fantasy.LOGGER.warn("删除存档文件夹失败 {} {}", e.getMessage(),e.getCause());
					try {
						Fantasy.LOGGER.info("强制删除中");
						FileUtils.forceDeleteOnExit(worldDirectory);
					} catch (IOException ignored) {
						ignored.printStackTrace();
					}
				}
				Fantasy.LOGGER.info("删除维度成功！ {}", dimensionKey.toString());
				dimensionKey=null;
				world=null;
			}

    }

    private static MappedRegistry<LevelStem> getDimensionsRegistry(MinecraftServer server) {
        WorldGenSettings generatorOptions = server.getWorldData().worldGenSettings();
        return (MappedRegistry<LevelStem>) generatorOptions.dimensions();
    }
}
