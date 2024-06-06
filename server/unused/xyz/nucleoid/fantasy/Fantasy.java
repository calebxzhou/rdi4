package xyz.nucleoid.fantasy;

import calebxzhou.rdi.mixin.AMcServer;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Fantasy {
    public static final Logger LOGGER = LogManager.getLogger("Fantasy");
    public static final String ID = "fantasy";
    private static Fantasy instance;

    private final MinecraftServer server;
    private final AMcServer serverAccess;

    private final RuntimeWorldManager worldManager;

    private final Set<ServerLevel> deletionQueue = new ReferenceOpenHashSet<>();

    static {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            Fantasy fantasy = get(server);
            fantasy.tick();
        });

        ServerLifecycleEvents.STOPPING.register(server -> {
            Fantasy fantasy = get(server);
            fantasy.onServerStopping();
        });
    }

    private Fantasy(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (AMcServer) server;

        this.worldManager = new RuntimeWorldManager(server);
    }

    /**
     * Gets the {@link Fantasy} instance for the given server instance.
     *
     * @param server the server to work with
     * @return the {@link Fantasy} instance to work with runtime dimensions
     */
    public static Fantasy get(MinecraftServer server) {
        Preconditions.checkState(server.isSameThread(), "cannot create worlds from off-thread!");

        if (instance == null || instance.server != server) {
            instance = new Fantasy(server);
        }

        return instance;
    }

    private void tick() {
        Set<ServerLevel> deletionQueue = this.deletionQueue;
        if (!deletionQueue.isEmpty()) {
            deletionQueue.removeIf(this::tickDeleteWorld);
        }
    }

    /**
     * Gets or creates a new persistent world with the given identifier and {@link RuntimeWorldConfig}. These worlds
     * will be saved to disk and can be restored after a server restart.
     * <p>
     * If a world with this identifier exists already, it will be returned and no new world will be constructed.
     * <p>
     * <b>Note!</b> These persistent worlds will not be automatically restored! This function
     * must be called after a server restart with the relevant identifier and configuration such that it can be loaded.
     * <p>
     * The created world is returned asynchronously through a {@link RuntimeWorldHandle}.
     * This handle can be used to acquire the {@link ServerLevel} object through {@link RuntimeWorldHandle#asWorld()},
     * as well as to delete the world through {@link RuntimeWorldHandle#delete()}.
     *
     * @param key the unique identifier for this dimension
     * @param config the config with which to construct this persistent world
     * @return a future providing the created world
     */
    @Nullable
	public RuntimeWorldHandle getOrOpenPersistentWorld(ResourceLocation key, RuntimeWorldConfig config) {
        ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, key);

        ServerLevel slvl = this.server.getLevel(worldKey);
		IslandLevel ilvl;
        if (slvl == null) {
			//找不到岛，创建新的
			ilvl = this.addPersistentWorld(key, config);

        } else {
			//找到了岛，直接用
            this.deletionQueue.remove(slvl);
			if(slvl instanceof IslandLevel i){
				ilvl = 	i;
			}else{
				RdiCore.logger.warn("要打开的维度"+key+"不是岛屿维度！");
				return null;
			}
        }
		return new RuntimeWorldHandle(this,ilvl);

    }
	public void unloadWorld(ServerLevel world){
		this.server.submit(() -> {
			worldManager.unload(world);
		});
	}
    private IslandLevel addPersistentWorld(ResourceLocation key) {
        ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, key);
        return this.worldManager.add(worldKey, config, IslandLevel.Style.PERSISTENT);
    }

    private IslandLevel addTemporaryWorld(ResourceLocation key, RuntimeWorldConfig config) {
        ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, key/*generateTemporaryWorldKey()*/);

        try {
            LevelStorageSource.LevelStorageAccess session = this.serverAccess.getStorageSource();
            FileUtils.forceDeleteOnExit(session.getDimensionPath(worldKey).toFile());
        } catch (IOException ignored) {
        }

        return this.worldManager.add(worldKey, config, IslandLevel.Style.TEMPORARY);
    }

    void enqueueWorldDeletion(ServerLevel world) {
        this.server.submit(() -> {
            this.deletionQueue.add(world);
        });
    }

    private boolean tickDeleteWorld(ServerLevel world) {
        if (this.isWorldUnloaded(world)) {
            this.worldManager.delete(world);
            return true;
        } else {
            this.kickPlayers(world);
            return false;
        }
    }

    private void kickPlayers(ServerLevel world) {
        if (world.players().isEmpty()) {
            return;
        }

        ServerLevel overworld = this.server.overworld();
        BlockPos spawnPos = overworld.getSharedSpawnPos();
        float spawnAngle = overworld.getSharedSpawnAngle();

        List<ServerPlayer> players = new ArrayList<>(world.players());
        for (ServerPlayer player : players) {
            player.teleportTo(overworld, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, spawnAngle, 0.0F);
        }
    }

    private boolean isWorldUnloaded(ServerLevel world) {
        return world.players().isEmpty() && world.getChunkSource().getLoadedChunksCount() <= 0;
    }

    private void onServerStopping() {

    }


}
