package net.zaharenko424.a_changed.datagen.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.a_changed.registry.EntityRegistry;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.a_changed.worldgen.OrangeTreeGrower;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class BiomeModifierProvider {

    public static final ResourceKey<BiomeModifier> ADD_LATEX_SPAWNS = Utils.resourceKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "add_latex_spawns");
    public static final ResourceKey<BiomeModifier> ADD_ORANGE_TREE = Utils.resourceKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS,"add_orange_tree");

    public static void bootstrap(BootstapContext<BiomeModifier> context){
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(ADD_LATEX_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                List.of(
                        new MobSpawnSettings.SpawnerData(EntityRegistry.BEI_FENG.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityRegistry.DARK_LATEX_WOLF_FEMALE.get(), 10 , 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityRegistry.DARK_LATEX_WOLF_MALE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityRegistry.GAS_WOLF.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityRegistry.WHITE_LATEX_WOLF_FEMALE.get(), 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityRegistry.WHITE_LATEX_WOLF_MALE.get(), 10, 1, 2)
                ))
        );

        context.register(ADD_ORANGE_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(OrangeTreeGrower.TREE_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION)
        );
    }
}