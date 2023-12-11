package net.zaharenko424.testmod.registry;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.block.*;
import net.zaharenko424.testmod.block.boxes.CardboardBox;
import net.zaharenko424.testmod.block.boxes.SmallCardboardBox;
import net.zaharenko424.testmod.block.boxes.TallBox;
import net.zaharenko424.testmod.block.boxes.TallCardboardBox;
import net.zaharenko424.testmod.block.doors.AbstractTwoByTwoDoor;
import net.zaharenko424.testmod.block.doors.LabDoor;
import net.zaharenko424.testmod.block.doors.LibraryDoor;
import net.zaharenko424.testmod.block.doors.MaintenanceDoor;
import net.zaharenko424.testmod.worldgen.OrangeTreeGrower;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.testmod.TestMod.MODID;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    //Liquid Blocks
    public static final DeferredBlock<LiquidBlock> DARK_LATEX_FLUID_BLOCK = BLOCKS.register("dark_latex_fluid", ()-> new LiquidBlock(FluidRegistry.DARK_LATEX_STILL,liquidProperties()));
    public static final DeferredBlock<LiquidBlock> LATEX_SOLVENT_BLOCK = BLOCKS.register("latex_solvent", ()-> new LiquidBlock(FluidRegistry.LATEX_SOLVENT_STILL, liquidProperties()));
    public static final DeferredBlock<LiquidBlock> WHITE_LATEX_FLUID_BLOCK = BLOCKS.register("white_latex_fluid", ()-> new LiquidBlock(FluidRegistry.WHITE_LATEX_STILL,liquidProperties()));

    //Blocks
    public static final DeferredBlock<Block> BLUE_LAB_TILE = BLOCKS.registerSimpleBlock("blue_lab_tile", decorProperties().mapColor(DyeColor.LIGHT_BLUE));
    public static final DeferredBlock<Block> BOLTED_BLUE_LAB_TILE = BLOCKS.registerSimpleBlock("bolted_blue_lab_tile", decorProperties().mapColor(DyeColor.LIGHT_BLUE));
    public static final DeferredBlock<Block> BOLTED_LAB_TILE = BLOCKS.registerSimpleBlock("bolted_lab_tile", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<BookStack> BOOK_STACK = BLOCKS.register("book_stack", ()-> new BookStack(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block> BROWN_LAB_BLOCK = BLOCKS.registerSimpleBlock("brown_lab_block", decorProperties().mapColor(DyeColor.BROWN));
    public static final DeferredBlock<CardboardBox> CARDBOARD_BOX = BLOCKS.register("cardboard_box", ()-> new CardboardBox(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<ConnectedTextureBlock> CARPET_BLOCK = BLOCKS.register("carpet_block", ()-> new ConnectedTextureBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).strength(.8f).sound(SoundType.WOOL).ignitedByLava()));
    public static final DeferredBlock<Chair> CHAIR = BLOCKS.register("chair", ()-> new Chair(decorProperties().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<ConnectedTextureBlock> CONNECTED_BLUE_LAB_TILE = BLOCKS.register("connected_blue_lab_tile", ()-> new ConnectedTextureBlock(decorProperties().mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<ConnectedTextureBlock> CONNECTED_LAB_TILE = BLOCKS.register("connected_lab_tile", ()-> new ConnectedTextureBlock(decorProperties().mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<Block> DARK_LATEX_BLOCK = BLOCKS.registerSimpleBlock("dark_latex_block", BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(6,1).sound(SoundType.SLIME_BLOCK));
    public static final DeferredBlock<Block> HAZARD_BLOCK = BLOCKS.registerSimpleBlock("hazard_block", decorProperties().mapColor(DyeColor.ORANGE));
    public static final DeferredBlock<Keypad> KEYPAD = BLOCKS.register("keypad", ()-> new Keypad(decorProperties().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> LAB_BLOCK = BLOCKS.registerSimpleBlock("lab_block", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<LabDoor> LAB_DOOR = BLOCKS.register("lab_door", ()-> new LabDoor(doorProperties()));
    public static final DeferredBlock<Block> LAB_TILE = BLOCKS.registerSimpleBlock("lab_tile", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<LatexContainer> LATEX_CONTAINER = BLOCKS.register("latex_container", ()-> new LatexContainer(decorProperties().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<AbstractTwoByTwoDoor> LIBRARY_DOOR = BLOCKS.register("library_door", ()-> new LibraryDoor(doorProperties().noOcclusion()));
    public static final DeferredBlock<MaintenanceDoor> MAINTENANCE_DOOR = BLOCKS.register("maintenance_door", ()-> new MaintenanceDoor(doorProperties()));
    public static final DeferredBlock<TallBox> METAL_BOX = BLOCKS.register("metal_box", ()-> new TallBox(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Note> NOTE = BLOCKS.register("note", ()-> new Note(BlockBehaviour.Properties.copy(Blocks.ORANGE_WOOL)));
    public static final DeferredBlock<Notepad> NOTEPAD = BLOCKS.register("notepad", ()-> new Notepad(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));
    public static final DeferredBlock<LeavesBlock> ORANGE_LEAVES = BLOCKS.register("orange_leaves", ()-> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<SaplingBlock> ORANGE_SAPLING = BLOCKS.register("orange_sapling", ()-> new SaplingBlock(new OrangeTreeGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<RotatedPillarBlock> ORANGE_TREE_LOG = BLOCKS.register("orange_tree_log", ()-> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Scanner> SCANNER = BLOCKS.register("scanner", ()-> new Scanner(decorProperties().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<SmallCardboardBox> SMALL_CARDBOARD_BOX = BLOCKS.register("small_cardboard_box", ()-> new SmallCardboardBox(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY).noLootTable()));
    public static final DeferredBlock<Table> TABLE = BLOCKS.register("table", ()-> new Table(decorProperties()));
    public static final DeferredBlock<TallCardboardBox> TALL_CARDBOARD_BOX = BLOCKS.register("tall_cardboard_box", ()-> new TallCardboardBox(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<TrafficCone> TRAFFIC_CONE = BLOCKS.register("traffic_cone", ()-> new TrafficCone(BlockBehaviour.Properties.of().strength(.75f,3f).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).mapColor(DyeColor.YELLOW).noOcclusion()));
    public static final DeferredBlock<TrapDoorBlock> VENT = BLOCKS.register("vent", ()-> new TrapDoorBlock(decorProperties().noOcclusion(), BlockSetType.STONE));
    public static final DeferredBlock<Block> WHITE_LATEX_BLOCK = BLOCKS.registerSimpleBlock("white_latex_block", BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(6,1).sound(SoundType.SLIME_BLOCK));
    public static final DeferredBlock<Block> YELLOW_LAB_BLOCK = BLOCKS.registerSimpleBlock("yellow_lab_block", decorProperties().mapColor(DyeColor.YELLOW));

    private static BlockBehaviour.@NotNull Properties liquidProperties(){
        return BlockBehaviour.Properties.of().liquid().noCollission().noLootTable().pushReaction(PushReaction.DESTROY).replaceable();
    }

    private static BlockBehaviour.@NotNull Properties decorProperties(){
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5f,6).sound(SoundType.STONE);
    }

    private static BlockBehaviour.@NotNull Properties doorProperties(){
        return decorProperties().sound(SoundType.METAL).pushReaction(PushReaction.BLOCK);
    }
}