package xyz.nucleoid.fantasy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import xyz.nucleoid.fantasy.util.VoidChunkGenerator;

public final class FantasyInitializer {

    public static void onInitialize() {
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, new ResourceLocation(Fantasy.ID, "void"), VoidChunkGenerator.CODEC);
    }
}
