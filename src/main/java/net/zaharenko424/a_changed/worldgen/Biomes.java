package net.zaharenko424.a_changed.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.zaharenko424.a_changed.AChanged;

public class Biomes {
    public static final ResourceKey<Biome> DARK_LATEX_BIOME = ResourceKey.create(Registries.BIOME, AChanged.resourceLoc("dark_latex_biome"));
    public static final ResourceKey<Biome> WHITE_LATEX_BIOME = ResourceKey.create(Registries.BIOME, AChanged.resourceLoc("white_latex_biome"));
}