package net.zaharenko424.testmod.datagen.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.zaharenko424.testmod.block.OrangeTreeGrower;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.registry.BlockRegistry.ORANGE_LEAVES;
import static net.zaharenko424.testmod.registry.BlockRegistry.ORANGE_TREE_LOG;

public class ConfiguredFeatureProvider {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?,?>> context){
        register(context,OrangeTreeGrower.TREE,Feature.TREE,new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ORANGE_TREE_LOG.get()),
                new StraightTrunkPlacer(4,2,0),
                BlockStateProvider.simple(ORANGE_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2),ConstantInt.of(0),3),
                new TwoLayersFeatureSize(1,0,1)).ignoreVines().build());
    }

    private static <FC extends FeatureConfiguration,F extends Feature<FC>> void register(@NotNull BootstapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration){
        context.register(key,new ConfiguredFeature<>(feature,configuration));
    }
}