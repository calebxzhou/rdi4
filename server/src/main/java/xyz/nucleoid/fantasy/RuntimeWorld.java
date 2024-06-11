package xyz.nucleoid.fantasy;

import com.google.common.collect.ImmutableList;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;
import xyz.nucleoid.fantasy.util.VoidWorldProgressListener;

import java.util.List;
import java.util.concurrent.Executor;

public class RuntimeWorld extends ServerLevel {
    final Style style;
    private boolean flat;

    protected RuntimeWorld(MinecraftServer server, ResourceKey<Level> registryKey, RuntimeWorldConfig config, Style style) {
        super(
                server, Util.backgroundExecutor(), ((MinecraftServerAccess) server).getStorageSource(),
                new RuntimeWorldProperties(server.getWorldData(), config),
                registryKey,
                config.createDimensionOptions(server),
                VoidWorldProgressListener.INSTANCE,
                false,
                BiomeManager.obfuscateSeed(config.getSeed()),
                ImmutableList.of(),
                config.shouldTickTime(),
                null
        );
        this.style = style;
        this.flat = config.isFlat().orElse(super.isFlat());
    }


    protected RuntimeWorld(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, LevelStem dimensionOptions, ChunkProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, @Nullable RandomSequences randomSequencesState, Style style) {
        super(server, workerExecutor, session, properties, worldKey, dimensionOptions, worldGenerationProgressListener, debugWorld, seed, spawners, shouldTickTime, randomSequencesState);
        this.style = style;
    }


    @Override
    public long getSeed() {
        return ((RuntimeWorldProperties) this.levelData).config.getSeed();
    }

    @Override
    public void save(@Nullable ProgressListener progressListener, boolean flush, boolean enabled) {
        if (this.style == Style.PERSISTENT || !flush) {
            super.save(progressListener, flush, enabled);
        }
    }

    @Override
    public boolean isFlat() {
        return this.flat;
    }

    public enum Style {
        PERSISTENT,
        TEMPORARY
    }

    public interface Constructor {
        RuntimeWorld createWorld(MinecraftServer server, ResourceKey<Level> registryKey, RuntimeWorldConfig config, Style style);
    }
}
