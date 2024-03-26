package net.zaharenko424.a_changed.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.zaharenko424.a_changed.util.Utils;

public class Biomes {
    public static final ResourceKey<Biome> DARK_LATEX_BIOME = Utils.resourceKey(Registries.BIOME, "dark_latex_biome");
    public static final ResourceKey<Biome> WHITE_LATEX_BIOME = Utils.resourceKey(Registries.BIOME, "white_latex_biome");
}