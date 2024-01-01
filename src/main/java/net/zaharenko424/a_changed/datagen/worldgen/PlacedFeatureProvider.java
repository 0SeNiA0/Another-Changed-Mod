package net.zaharenko424.a_changed.datagen.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.worldgen.OrangeTreeGrower;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlacedFeatureProvider {

    public static void bootstrap(BootstapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?,?>> lookup = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context,OrangeTreeGrower.TREE_PLACED,lookup.getOrThrow(OrangeTreeGrower.TREE),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(2,0.1f,1), BlockRegistry.ORANGE_SAPLING.get()));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?,?>> feature, List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(feature,modifiers));
    }
}
