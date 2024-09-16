package net.zaharenko424.a_changed.datagen.worldgen;

import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.zaharenko424.a_changed.datagen.worldgen.biome.DarkLatexBiome;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.worldgen.OrangeTreeGrower;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.registry.BlockRegistry.ORANGE_LEAVES;
import static net.zaharenko424.a_changed.registry.BlockRegistry.ORANGE_TREE_LOG;

public class ConfiguredFeatureProvider {

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context){
        register(context, DarkLatexBiome.CRYSTAL_PATCH,
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                    96,
                    8,
                    2,
                    PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                            new WeightedStateProvider(
                                SimpleWeightedRandomList.<BlockState>builder()
                                    .add(BlockRegistry.DARK_LATEX_CRYSTAL.get().defaultBlockState(), 2)
                                    .add(BlockRegistry.GREEN_CRYSTAL.get().defaultBlockState(), 1))),
                        BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(new Vec3i(0,-1,0), BlockRegistry.DARK_LATEX_BLOCK.get()))))
        );

        register(context, OrangeTreeGrower.TREE,Feature.TREE,new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ORANGE_TREE_LOG.get()),
                new StraightTrunkPlacer(4,2,0),
                BlockStateProvider.simple(ORANGE_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2),ConstantInt.of(0),3),
                new TwoLayersFeatureSize(1,0,1)).ignoreVines().build()
        );
    }

    private static <FC extends FeatureConfiguration,F extends Feature<FC>> void register(@NotNull BootstrapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration){
        context.register(key,new ConfiguredFeature<>(feature,configuration));
    }
}