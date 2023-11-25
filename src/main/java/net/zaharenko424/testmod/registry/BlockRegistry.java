package net.zaharenko424.testmod.registry;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.zaharenko424.testmod.TestMod.MODID;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    //Blocks
    public static final DeferredBlock<LiquidBlock> LATEX_SOLVENT_BLOCK = BLOCKS.register("latex_solvent", ()-> new LiquidBlock(FluidRegistry.LATEX_SOLVENT_STILL, BlockBehaviour.Properties.of().liquid().noCollission().pushReaction(PushReaction.DESTROY).replaceable()));
    public static final DeferredBlock<LiquidBlock> WHITE_LATEX_FLUID_BLOCK = BLOCKS.register("white_latex_fluid", ()-> new LiquidBlock(FluidRegistry.WHITE_LATEX_STILL,BlockBehaviour.Properties.of().liquid().noCollission().pushReaction(PushReaction.DESTROY).replaceable()));
    public static final DeferredBlock<LiquidBlock> DARK_LATEX_FLUID_BLOCK = BLOCKS.register("dark_latex_fluid", ()-> new LiquidBlock(FluidRegistry.DARK_LATEX_STILL,BlockBehaviour.Properties.of().liquid().noCollission().pushReaction(PushReaction.DESTROY).replaceable()));
    public static final DeferredBlock<Block> WHITE_LATEX_BLOCK = BLOCKS.register("white_latex_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(6,1).sound(SoundType.SLIME_BLOCK)));
    public static final DeferredBlock<Block> DARK_LATEX_BLOCK = BLOCKS.register("dark_latex_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(6,1).sound(SoundType.SLIME_BLOCK)));
    public static final DeferredBlock<Block> LAB_TILE = BLOCKS.register("lab_tile", ()-> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> BOLTED_LAB_TILE = BLOCKS.register("bolted_lab_tile", ()-> new Block(BlockBehaviour.Properties.of()));
}