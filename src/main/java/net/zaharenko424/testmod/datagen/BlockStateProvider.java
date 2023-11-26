package net.zaharenko424.testmod.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.testmod.TestMod;

import static net.zaharenko424.testmod.registry.BlockRegistry.*;

public class BlockStateProvider extends net.neoforged.neoforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TestMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(BOLTED_LAB_TILE.get(),cubeAll(BOLTED_LAB_TILE.get()));
        simpleBlockWithItem(CARPET_BLOCK.get(),cubeAll(CARPET_BLOCK.get()));
        simpleBlockWithItem(DARK_LATEX_BLOCK.get(),cubeAll(DARK_LATEX_BLOCK.get()));
        simpleBlockWithItem(HAZARD_BLOCK.get(),cubeAll(HAZARD_BLOCK.get()));
        simpleBlockWithItem(LAB_BLOCK.get(),cubeAll(LAB_BLOCK.get()));
        simpleBlockWithItem(LAB_TILE.get(),cubeAll(LAB_TILE.get()));
        simpleBlockWithItem(WHITE_LATEX_BLOCK.get(),cubeAll(WHITE_LATEX_BLOCK.get()));
    }
}