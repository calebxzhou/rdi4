package xyz.nucleoid.fantasy.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class VoidChunkGenerator extends ChunkGenerator {
    public static final Codec<VoidChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Biome.CODEC.stable().fieldOf("biome").forGetter(g -> g.biome)
        ).apply(instance, instance.stable(VoidChunkGenerator::new));
    });

    private static final NoiseColumn EMPTY_SAMPLE = new NoiseColumn(0, new BlockState[0]);

    private final Holder<Biome> biome;

    public static final DensityFunction ZERO_DENSITY_FUNCTION = new DensityFunction() {
        @Override
        public double compute(FunctionContext pos) {
            return 0;
        }

        @Override
        public void fillArray(double[] ds, ContextProvider arg) { }

        @Override
        public DensityFunction mapAll(Visitor visitor) {
            return this;
        }

        @Override
        public double minValue() {
            return 0;
        }

        @Override
        public double maxValue() {
            return 0;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return KeyDispatchDataCodec.of(Codec.unit(this));
        }
    };

    public static final Climate.Sampler EMPTY_SAMPLER = new Climate.Sampler(ZERO_DENSITY_FUNCTION, ZERO_DENSITY_FUNCTION, ZERO_DENSITY_FUNCTION, ZERO_DENSITY_FUNCTION, ZERO_DENSITY_FUNCTION, ZERO_DENSITY_FUNCTION, Collections.emptyList());

    public VoidChunkGenerator(Holder<Biome> biome) {
        super(new FixedBiomeSource(biome));
        this.biome = biome;
    }

    @Deprecated
    public VoidChunkGenerator(Supplier<Biome> biome) {
        this(Holder.direct(biome.get()));
    }

    public VoidChunkGenerator(Registry<Biome> biomeRegistry) {
        this(biomeRegistry, Biomes.THE_VOID);
    }

    public VoidChunkGenerator(Registry<Biome> biomeRegistry, ResourceKey<Biome> biome) {
        this(biomeRegistry.getHolder(biome).get());
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager world, StructureManager structureAccessor, ChunkAccess chunk, GenerationStep.Carving carverStep) {

    }

    @Override
    public void createReferences(WorldGenLevel world, StructureManager accessor, ChunkAccess chunk) {
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor world, RandomState noiseConfig) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor world, RandomState noiseConfig) {
        return EMPTY_SAMPLE;
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState noiseConfig, BlockPos pos) {

    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Nullable
    @Override
    public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel world, HolderSet<Structure> structures, BlockPos center, int radius, boolean skipReferencedStructures) {
        return null;
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager accessor, MobCategory group, BlockPos pos) {
        return WeightedRandomList.create();
    }

    @Override
    public void createStructures(RegistryAccess registryManager, ChunkGeneratorStructureState placementCalculator, StructureManager structureAccessor, ChunkAccess chunk, StructureTemplateManager structureTemplateManager) {

    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSetRegistry, RandomState noiseConfig, long seed) {
        return ChunkGeneratorStructureState.createForFlat(noiseConfig, seed, biomeSource, Stream.empty());
    }
}
