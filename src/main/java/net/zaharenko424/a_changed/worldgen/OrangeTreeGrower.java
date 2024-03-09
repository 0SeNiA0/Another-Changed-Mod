package net.zaharenko424.a_changed.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.zaharenko424.a_changed.util.Utils;

import java.util.Optional;

public class OrangeTreeGrower {

    public static ResourceKey<ConfiguredFeature<?,?>> TREE = Utils.resourceKey(Registries.CONFIGURED_FEATURE,"orange_tree");
    public static ResourceKey<PlacedFeature> TREE_PLACED = Utils.resourceKey(Registries.PLACED_FEATURE,"orange_tree_placed");
    public static TreeGrower GROWER = new TreeGrower("orange", Optional.empty(), Optional.of(TREE), Optional.empty());
}