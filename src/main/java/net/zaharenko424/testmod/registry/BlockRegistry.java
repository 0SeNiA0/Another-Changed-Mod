package net.zaharenko424.testmod.registry;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.block.CarpetBlock;
import net.zaharenko424.testmod.block.ConnectedLabTile;
import net.zaharenko424.testmod.block.OrangeTreeGrower;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TestMod.MODID;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    //Liquid Blocks
    public static final DeferredBlock<LiquidBlock> DARK_LATEX_FLUID_BLOCK = BLOCKS.register("dark_latex_fluid", ()-> new LiquidBlock(FluidRegistry.DARK_LATEX_STILL,liquidProperties()));
    public static final DeferredBlock<LiquidBlock> LATEX_SOLVENT_BLOCK = BLOCKS.register("latex_solvent", ()-> new LiquidBlock(FluidRegistry.LATEX_SOLVENT_STILL, liquidProperties()));
    public static final DeferredBlock<LiquidBlock> WHITE_LATEX_FLUID_BLOCK = BLOCKS.register("white_latex_fluid", ()-> new LiquidBlock(FluidRegistry.WHITE_LATEX_STILL,liquidProperties()));

    //Blocks
    public static final DeferredBlock<Block> BOLTED_LAB_TILE = BLOCKS.registerSimpleBlock("bolted_lab_tile", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<Block> BROWN_LAB_BLOCK = BLOCKS.registerSimpleBlock("brown_lab_block", decorProperties().mapColor(DyeColor.BROWN));
    public static final DeferredBlock<CarpetBlock> CARPET_BLOCK = BLOCKS.register("carpet_block", ()-> new CarpetBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).strength(.8f).sound(SoundType.WOOL).ignitedByLava()));
    public static final DeferredBlock<ConnectedLabTile> CONNECTED_LAB_TILE = BLOCKS.register("connected_lab_tile", ()-> new ConnectedLabTile(decorProperties().mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<Block> DARK_LATEX_BLOCK = BLOCKS.registerSimpleBlock("dark_latex_block", BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(6,1).sound(SoundType.SLIME_BLOCK));
    public static final DeferredBlock<Block> HAZARD_BLOCK = BLOCKS.registerSimpleBlock("hazard_block", decorProperties().mapColor(DyeColor.ORANGE));
    public static final DeferredBlock<Block> LAB_BLOCK = BLOCKS.registerSimpleBlock("lab_block", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<Block> LAB_TILE = BLOCKS.registerSimpleBlock("lab_tile", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<LeavesBlock> ORANGE_LEAVES = BLOCKS.register("orange_leaves", ()-> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<SaplingBlock> ORANGE_SAPLING = BLOCKS.register("orange_sapling", ()-> new SaplingBlock(new OrangeTreeGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<RotatedPillarBlock> ORANGE_TREE_LOG = BLOCKS.register("orange_tree_log", ()-> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> WHITE_LATEX_BLOCK = BLOCKS.registerSimpleBlock("white_latex_block", BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(6,1).sound(SoundType.SLIME_BLOCK));
    public static final DeferredBlock<Block> YELLOW_LAB_BLOCK = BLOCKS.registerSimpleBlock("yellow_lab_block", decorProperties().mapColor(DyeColor.YELLOW));

    private static BlockBehaviour.@NotNull Properties liquidProperties(){
        return BlockBehaviour.Properties.of().liquid().noCollission().noLootTable().pushReaction(PushReaction.DESTROY).replaceable();
    }

    private static BlockBehaviour.@NotNull Properties decorProperties(){
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5f,6).sound(SoundType.STONE);
    }
}