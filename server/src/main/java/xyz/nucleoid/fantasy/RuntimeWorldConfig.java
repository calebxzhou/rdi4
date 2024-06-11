package xyz.nucleoid.fantasy;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.fantasy.util.GameRuleStore;

/**
 * A configuration describing how a runtime world should be constructed. This includes properties such as the dimension
 * type, chunk generator, and game rules.
 *
 * @see Fantasy
 */
public final class RuntimeWorldConfig {
    private long seed = 0;
    private ResourceKey<DimensionType> dimensionTypeKey = Fantasy.DEFAULT_DIM_TYPE;
    private Holder<DimensionType> dimensionType;
    private ChunkGenerator generator = null;
    private boolean shouldTickTime = true;
    private long timeOfDay = 6000;
    private Difficulty difficulty = Difficulty.HARD;
    private final GameRuleStore gameRules = new GameRuleStore();
    private RuntimeWorld.Constructor worldConstructor = RuntimeWorld::new;

    private int sunnyTime = Integer.MAX_VALUE;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;
    private TriState flat = TriState.DEFAULT;

    public RuntimeWorldConfig setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public RuntimeWorldConfig setWorldConstructor(RuntimeWorld.Constructor constructor) {
        this.worldConstructor = constructor;
        return this;
    }

    public RuntimeWorldConfig setDimensionType(Holder<DimensionType> dimensionType) {
        this.dimensionType = dimensionType;
        this.dimensionTypeKey = null;
        return this;
    }

    @Deprecated
    public RuntimeWorldConfig setDimensionType(DimensionType dimensionType) {
        this.dimensionType = Holder.direct(dimensionType);
        this.dimensionTypeKey = null;
        return this;
    }

    public RuntimeWorldConfig setDimensionType(ResourceKey<DimensionType> dimensionType) {
        this.dimensionTypeKey = dimensionType;
        this.dimensionType = null;
        return this;
    }

    public RuntimeWorldConfig setGenerator(ChunkGenerator generator) {
        this.generator = generator;
        return this;
    }
	
    public RuntimeWorldConfig setShouldTickTime(boolean shouldTickTime) {
        this.shouldTickTime = shouldTickTime;
        return this;
    }

    public RuntimeWorldConfig setTimeOfDay(long timeOfDay) {
        this.timeOfDay = timeOfDay;
        return this;
    }

    public RuntimeWorldConfig setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public RuntimeWorldConfig setGameRule(GameRules.Key<GameRules.BooleanValue> key, boolean value) {
        this.gameRules.set(key, value);
        return this;
    }

    public RuntimeWorldConfig setGameRule(GameRules.Key<GameRules.IntegerValue> key, int value) {
        this.gameRules.set(key, value);
        return this;
    }

    public RuntimeWorldConfig setSunny(int sunnyTime) {
        this.sunnyTime = sunnyTime;
        this.raining = false;
        this.thundering = false;
        return this;
    }

    public RuntimeWorldConfig setRaining(int rainTime) {
        this.raining = rainTime > 0;
        this.rainTime = rainTime;
        return this;
    }

    public RuntimeWorldConfig setRaining(boolean raining) {
        this.raining = raining;
        return this;
    }

    public RuntimeWorldConfig setThundering(int thunderTime) {
        this.thundering = thunderTime > 0;
        this.thunderTime = thunderTime;
        return this;
    }

    public RuntimeWorldConfig setThundering(boolean thundering) {
        this.thundering = thundering;
        return this;
    }

    public RuntimeWorldConfig setFlat(TriState state) {
        this.flat = state;
        return this;
    }

    public RuntimeWorldConfig setFlat(boolean state) {
        return this.setFlat(TriState.of(state));
    }

    public long getSeed() {
        return this.seed;
    }

    public LevelStem createDimensionOptions(MinecraftServer server) {
        var dimensionType = this.resolveDimensionType(server);
        return new LevelStem(dimensionType, this.generator);
    }

    private Holder<DimensionType> resolveDimensionType(MinecraftServer server) {
        var dimensionType = this.dimensionType;
        if (dimensionType == null) {
            dimensionType = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getHolder(this.dimensionTypeKey).orElse(null);
            Preconditions.checkNotNull(dimensionType, "invalid dimension type " + this.dimensionTypeKey);
        }
        return dimensionType;
    }

    @Nullable
    public ChunkGenerator getGenerator() {
        return this.generator;
    }

    public RuntimeWorld.Constructor getWorldConstructor() {
        return this.worldConstructor;
    }
	
    public boolean shouldTickTime() {
        return this.shouldTickTime;
    }

    public long getTimeOfDay() {
        return this.timeOfDay;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public GameRuleStore getGameRules() {
        return this.gameRules;
    }

    public int getSunnyTime() {
        return this.sunnyTime;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public int getThunderTime() {
        return this.thunderTime;
    }

    public boolean isRaining() {
        return this.raining;
    }

    public boolean isThundering() {
        return this.thundering;
    }

    public TriState isFlat() {
        return this.flat;
    }
}
