package net.zaharenko424.testmod.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

import static net.zaharenko424.testmod.registry.BlockRegistry.*;

public class BlockLootTableProvider extends BlockLootSubProvider {
    public BlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(BOLTED_LAB_TILE.get());
        dropSelf(CARPET_BLOCK.get());
        dropSelf(DARK_LATEX_BLOCK.get());
        dropSelf(HAZARD_BLOCK.get());
        dropSelf(LAB_BLOCK.get());
        dropSelf(LAB_TILE.get());
        dropSelf(WHITE_LATEX_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }
}