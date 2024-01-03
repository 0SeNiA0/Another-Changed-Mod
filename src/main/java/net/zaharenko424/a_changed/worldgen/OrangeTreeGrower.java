package net.zaharenko424.a_changed.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OrangeTreeGrower extends AbstractTreeGrower {

    public static ResourceKey<ConfiguredFeature<?,?>> TREE = Utils.resourceKey(Registries.CONFIGURED_FEATURE,"orange_tree");
    public static ResourceKey<PlacedFeature> TREE_PLACED = Utils.resourceKey(Registries.PLACED_FEATURE,"orange_tree_placed");

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource p_222910_, boolean p_222911_) {
        return TREE;
    }
}