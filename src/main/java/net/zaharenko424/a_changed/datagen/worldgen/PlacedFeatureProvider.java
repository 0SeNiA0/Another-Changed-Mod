package net.zaharenko424.a_changed.datagen.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.zaharenko424.a_changed.datagen.worldgen.biome.DarkLatexBiome;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.worldgen.OrangeTreeGrower;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlacedFeatureProvider {

    public static void bootstrap(BootstrapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?,?>> lookup = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, DarkLatexBiome.CRYSTAL_PATCH_PLACED, lookup.getOrThrow(DarkLatexBiome.CRYSTAL_PATCH),
                List.of(NoiseThresholdCountPlacement.of(-0.8, 15, 4),
                        RarityFilter.onAverageOnceEvery(8),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome())
                );

        register(context, OrangeTreeGrower.TREE_PLACED,lookup.getOrThrow(OrangeTreeGrower.TREE),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1,0.1f,1), BlockRegistry.ORANGE_SAPLING.get()));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?,?>> feature, List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(feature,modifiers));
    }
}
