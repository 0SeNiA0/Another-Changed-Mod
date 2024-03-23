package net.zaharenko424.a_changed.registry;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.blocks.*;
import net.zaharenko424.a_changed.block.boxes.CardboardBox;
import net.zaharenko424.a_changed.block.boxes.SmallCardboardBox;
import net.zaharenko424.a_changed.block.boxes.TallBox;
import net.zaharenko424.a_changed.block.boxes.TallCardboardBox;
import net.zaharenko424.a_changed.block.doors.*;
import net.zaharenko424.a_changed.block.machines.*;
import net.zaharenko424.a_changed.block.sign.HangingSign;
import net.zaharenko424.a_changed.block.sign.StandingSign;
import net.zaharenko424.a_changed.block.sign.WallHangingSign;
import net.zaharenko424.a_changed.block.sign.WallSign;
import net.zaharenko424.a_changed.worldgen.OrangeTreeGrower;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.AChanged.MODID;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    //Liquid Blocks
    public static final DeferredBlock<LiquidBlock> DARK_LATEX_FLUID_BLOCK = BLOCKS.register("dark_latex_fluid", ()-> new LiquidBlock(FluidRegistry.DARK_LATEX_STILL,liquidProperties()));
    public static final DeferredBlock<LiquidBlock> LATEX_SOLVENT_BLOCK = BLOCKS.register("latex_solvent", ()-> new LiquidBlock(FluidRegistry.LATEX_SOLVENT_STILL, liquidProperties()));
    public static final DeferredBlock<LiquidBlock> WHITE_LATEX_FLUID_BLOCK = BLOCKS.register("white_latex_fluid", ()-> new LiquidBlock(FluidRegistry.WHITE_LATEX_STILL,liquidProperties()));

    //Blocks
    public static final DeferredBlock<Compressor> COMPRESSOR = BLOCKS.register("compressor", ()-> new Compressor(decorProperties()));
    public static final DeferredBlock<WireBlock> COPPER_WIRE = BLOCKS.register("copper_wire", ()-> new WireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final DeferredBlock<DNAExtractor> DNA_EXTRACTOR = BLOCKS.register("dna_extractor", ()-> new DNAExtractor(decorProperties().noOcclusion()));
    public static final DeferredBlock<LatexEncoder> LATEX_ENCODER = BLOCKS.register("latex_encoder", ()-> new LatexEncoder(decorProperties().noOcclusion()));
    public static final DeferredBlock<LatexPurifier> LATEX_PURIFIER = BLOCKS.register("latex_purifier", ()-> new LatexPurifier(decorProperties()));

    public static final DeferredBlock<AirConditioner> AIR_CONDITIONER = BLOCKS.register("air_conditioner", ()-> new AirConditioner(decorProperties()));
    public static final DeferredBlock<BigLabDoor> BIG_LAB_DOOR = BLOCKS.register("big_lab_door", ()-> new BigLabDoor(bigDoorProperties()));
    public static final DeferredBlock<BigLibraryDoor> BIG_LIBRARY_DOOR = BLOCKS.register("big_library_door", ()-> new BigLibraryDoor(bigDoorProperties()));
    public static final DeferredBlock<BigMaintenanceDoor> BIG_MAINTENANCE_DOOR = BLOCKS.register("big_maintenance_door", ()-> new BigMaintenanceDoor(bigDoorProperties()));
    public static final DeferredBlock<Block> BLUE_LAB_TILE = BLOCKS.registerSimpleBlock("blue_lab_tile", decorProperties().mapColor(DyeColor.LIGHT_BLUE));
    public static final DeferredBlock<Block> BOLTED_BLUE_LAB_TILE = BLOCKS.registerSimpleBlock("bolted_blue_lab_tile", decorProperties().mapColor(DyeColor.LIGHT_BLUE));
    public static final DeferredBlock<Block> BOLTED_LAB_TILE = BLOCKS.registerSimpleBlock("bolted_lab_tile", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<BookStack> BOOK_STACK = BLOCKS.register("book_stack", ()-> new BookStack(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL)));
    public static final DeferredBlock<BrokenCup> BROKEN_CUP = BLOCKS.register("broken_cup", ()-> new BrokenCup(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(.1f)));
    public static final DeferredBlock<BrokenFlask> BROKEN_FLASK = BLOCKS.register("broken_flask", ()-> new BrokenFlask(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<Block> BROWN_LAB_BLOCK = BLOCKS.registerSimpleBlock("brown_lab_block", decorProperties().mapColor(DyeColor.BROWN));
    public static final DeferredBlock<Capacitor> CAPACITOR = BLOCKS.register("capacitor", () -> new Capacitor(decorProperties()));
    public static final DeferredBlock<CardboardBox> CARDBOARD_BOX = BLOCKS.register("cardboard_box", ()-> new CardboardBox(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<ConnectedTextureBlock> CARPET_BLOCK = BLOCKS.register("carpet_block", ()-> new ConnectedTextureBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).strength(.8f).sound(SoundType.WOOL).ignitedByLava()));
    public static final DeferredBlock<Chair> CHAIR = BLOCKS.register("chair", ()-> new Chair(decorProperties().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Computer> COMPUTER = BLOCKS.register("computer", ()-> new Computer(decorProperties()));
    public static final DeferredBlock<ConnectedTextureBlock> CONNECTED_BLUE_LAB_TILE = BLOCKS.register("connected_blue_lab_tile", ()-> new ConnectedTextureBlock(decorProperties().mapColor(DyeColor.LIGHT_BLUE)));
    public static final DeferredBlock<ConnectedTextureBlock> CONNECTED_LAB_TILE = BLOCKS.register("connected_lab_tile", ()-> new ConnectedTextureBlock(decorProperties().mapColor(DyeColor.WHITE)));
    public static final DeferredBlock<CryoChamber> CRYO_CHAMBER = BLOCKS.register("cryo_chamber", ()-> new CryoChamber(doorProperties()));
    public static final DeferredBlock<Cup> CUP = BLOCKS.register("cup", ()-> new Cup(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(.4f)));
    public static final DeferredBlock<DangerSign> DANGER_SIGN = BLOCKS.register("danger_sign", ()-> new DangerSign(decorProperties()));
    public static final DeferredBlock<Block> DARK_LATEX_BLOCK = BLOCKS.registerSimpleBlock("dark_latex_block", BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(1.5f,1).sound(SoundType.SLIME_BLOCK));
    public static final DeferredBlock<Crystal> DARK_LATEX_CRYSTAL = BLOCKS.register("dark_latex_crystal", ()-> new Crystal(decorProperties(),TransfurRegistry.DARK_LATEX_WOLF_M_TF));
    public static final DeferredBlock<Block> DARK_LATEX_CRYSTAL_ICE = BLOCKS.registerSimpleBlock("dark_latex_ice", BlockBehaviour.Properties.of().mapColor(MapColor.ICE).friction(1.1f).strength(0.5f).sound(SoundType.GLASS));
    public static final DeferredBlock<LatexPuddle> DARK_LATEX_PUDDLE_F = BLOCKS.register("dark_latex_puddle_f", ()-> new LatexPuddle(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK), TransfurRegistry.DARK_LATEX_WOLF_F_TF));
    public static final DeferredBlock<LatexPuddle> DARK_LATEX_PUDDLE_M = BLOCKS.register("dark_latex_puddle_m", ()-> new LatexPuddle(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK), TransfurRegistry.DARK_LATEX_WOLF_M_TF));
    public static final DeferredBlock<Flask> FLASK = BLOCKS.register("flask", ()-> new Flask(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<GasTank> GAS_TANK = BLOCKS.register("gas_tank", ()-> new GasTank(decorProperties().noLootTable().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Generator> GENERATOR = BLOCKS.register("generator", ()-> new Generator(decorProperties()));
    public static final DeferredBlock<TallCrystal> GREEN_CRYSTAL = BLOCKS.register("green_crystal", ()-> new TallCrystal(decorProperties(),TransfurRegistry.BEI_FENG_TF));
    public static final DeferredBlock<Block> HAZARD_BLOCK = BLOCKS.registerSimpleBlock("hazard_block", decorProperties().mapColor(DyeColor.ORANGE));
    public static final DeferredBlock<Block> HAZARD_LAB_BLOCK = BLOCKS.registerSimpleBlock("hazard_lab_block", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<IVRack> IV_RACK = BLOCKS.register("iv_rack", ()-> new IVRack(decorProperties()));
    public static final DeferredBlock<Keypad> KEYPAD = BLOCKS.register("keypad", ()-> new Keypad(decorProperties().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> LAB_BLOCK = BLOCKS.registerSimpleBlock("lab_block", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<LabDoor> LAB_DOOR = BLOCKS.register("lab_door", ()-> new LabDoor(doorProperties()));
    public static final DeferredBlock<Block> LAB_TILE = BLOCKS.registerSimpleBlock("lab_tile", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<LaserEmitter> LASER_EMITTER = BLOCKS.register("laser_emitter", ()-> new LaserEmitter(decorProperties()));
    public static final DeferredBlock<LatexContainer> LATEX_CONTAINER = BLOCKS.register("latex_container", ()-> new LatexContainer(decorProperties().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Abstract2By2Door> LIBRARY_DOOR = BLOCKS.register("library_door", ()-> new LibraryDoor(doorProperties().noOcclusion()));
    public static final DeferredBlock<MaintenanceDoor> MAINTENANCE_DOOR = BLOCKS.register("maintenance_door", ()-> new MaintenanceDoor(doorProperties()));
    public static final DeferredBlock<TallBox> METAL_BOX = BLOCKS.register("metal_box", ()-> new TallBox(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Note> NOTE = BLOCKS.register("note", ()-> new Note(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_WOOL)));
    public static final DeferredBlock<Notepad> NOTEPAD = BLOCKS.register("notepad", ()-> new Notepad(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final DeferredBlock<Block> ORANGE_LAB_BLOCK = BLOCKS.registerSimpleBlock("orange_lab_block", decorProperties().mapColor(DyeColor.ORANGE));
    public static final DeferredBlock<LeavesBlock> ORANGE_LEAVES = BLOCKS.register("orange_leaves", ()-> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<SaplingBlock> ORANGE_SAPLING = BLOCKS.register("orange_sapling", ()-> new SaplingBlock(OrangeTreeGrower.GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<Pipe> PIPE = BLOCKS.register("pipe", ()-> new Pipe(decorProperties()));
    public static final DeferredBlock<FlowerPotBlock> POTTED_ORANGE_SAPLING = BLOCKS.register("potted_orange_sapling", ()-> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, ORANGE_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
    public static final DeferredBlock<Scanner> SCANNER = BLOCKS.register("scanner", ()-> new Scanner(decorProperties().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<SmallCardboardBox> SMALL_CARDBOARD_BOX = BLOCKS.register("small_cardboard_box", ()-> new SmallCardboardBox(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY).noLootTable()));
    public static final DeferredBlock<SmartSewageSystem> SMART_SEWAGE_SYSTEM = BLOCKS.register("smart_sewage_system", ()-> new SmartSewageSystem(decorProperties()));
    public static final DeferredBlock<Block> STRIPED_ORANGE_LAB_BLOCK = BLOCKS.registerSimpleBlock("striped_orange_lab_block", decorProperties().mapColor(DyeColor.ORANGE));
    public static final DeferredBlock<Table> TABLE = BLOCKS.register("table", ()-> new Table(decorProperties()));
    public static final DeferredBlock<TallCardboardBox> TALL_CARDBOARD_BOX = BLOCKS.register("tall_cardboard_box", ()-> new TallCardboardBox(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<TestTubes> TEST_TUBES = BLOCKS.register("test_tubes", ()-> new TestTubes(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<TrafficCone> TRAFFIC_CONE = BLOCKS.register("traffic_cone", ()-> new TrafficCone(BlockBehaviour.Properties.of().strength(.75f,3f).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).mapColor(DyeColor.YELLOW).noOcclusion()));
    public static final DeferredBlock<VentDuct> VENT_DUCT = BLOCKS.register("vent_duct", ()-> new VentDuct(decorProperties()));
    public static final DeferredBlock<TrapDoorBlock> VENT_HATCH = BLOCKS.register("vent_hatch", ()-> new TrapDoorBlock(BlockSetType.STONE, decorProperties().noOcclusion()));
    public static final DeferredBlock<Block> VENT_WALL = BLOCKS.registerSimpleBlock("vent_wall", decorProperties().mapColor(DyeColor.WHITE));
    public static final DeferredBlock<Block> WHITE_LATEX_BLOCK = BLOCKS.registerSimpleBlock("white_latex_block", BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(1.5f,1).sound(SoundType.SLIME_BLOCK));
    public static final DeferredBlock<LatexPuddle> WHITE_LATEX_PUDDLE_F = BLOCKS.register("white_latex_puddle_f", ()-> new LatexPuddle(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK), TransfurRegistry.WHITE_LATEX_WOLF_F_TF));
    public static final DeferredBlock<LatexPuddle> WHITE_LATEX_PUDDLE_M = BLOCKS.register("white_latex_puddle_m", ()-> new LatexPuddle(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK), TransfurRegistry.WHITE_LATEX_WOLF_M_TF));
    public static final DeferredBlock<Block> YELLOW_LAB_BLOCK = BLOCKS.registerSimpleBlock("yellow_lab_block", decorProperties().mapColor(DyeColor.YELLOW));

    //Wood
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_ORANGE_LOG = BLOCKS.register("stripped_orange_log", ()-> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_LOG)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_ORANGE_WOOD = BLOCKS.register("stripped_orange_wood", ()-> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD)));
    public static final DeferredBlock<ButtonBlock> ORANGE_BUTTON = BLOCKS.register("orange_button", ()-> new ButtonBlock(BlockSetType.ACACIA, 30, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<DoorBlock> ORANGE_DOOR = BLOCKS.register("orange_door", ()-> new DoorBlock(BlockSetType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_DOOR)));
    public static final DeferredBlock<FenceBlock> ORANGE_FENCE = BLOCKS.register("orange_fence", ()-> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE)));
    public static final DeferredBlock<FenceGateBlock> ORANGE_FENCE_GATE = BLOCKS.register("orange_fence_gate", ()-> new FenceGateBlock(AChanged.ORANGE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE_GATE)));
    public static final DeferredBlock<HangingSign> ORANGE_HANGING_SIGN = BLOCKS.register("orange_hanging_sign", ()-> new HangingSign(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_HANGING_SIGN), AChanged.ORANGE));
    public static final DeferredBlock<Block> ORANGE_PLANKS = BLOCKS.registerSimpleBlock("orange_planks", BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS));
    public static final DeferredBlock<PressurePlateBlock> ORANGE_PRESSURE_PLATE = BLOCKS.register("orange_pressure_plate", ()-> new PressurePlateBlock(BlockSetType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PRESSURE_PLATE)));
    public static final DeferredBlock<StandingSign> ORANGE_SIGN = BLOCKS.register("orange_sign", ()-> new StandingSign(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SIGN), AChanged.ORANGE));
    public static final DeferredBlock<SlabBlock> ORANGE_SLAB = BLOCKS.register("orange_slab", ()-> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SLAB)));
    public static final DeferredBlock<StairBlock> ORANGE_STAIRS = BLOCKS.register("orange_stairs", ()-> new StairBlock(()-> ORANGE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ORANGE_PLANKS.get())));
    public static final DeferredBlock<TrapDoorBlock> ORANGE_TRAPDOOR = BLOCKS.register("orange_trapdoor", ()-> new TrapDoorBlock(BlockSetType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_TRAPDOOR)));
    public static final DeferredBlock<LogBlock> ORANGE_TREE_LOG = BLOCKS.register("orange_tree_log", ()-> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG), STRIPPED_ORANGE_LOG));
    public static final DeferredBlock<WallSign> ORANGE_WALL_SIGN = BLOCKS.register("orange_wall_sign", ()-> new WallSign(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), AChanged.ORANGE));
    public static final DeferredBlock<WallHangingSign> ORANGE_WALL_HANGING_SIGN = BLOCKS.register("orange_wall_hanging_sign", ()-> new WallHangingSign(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava(), AChanged.ORANGE));
    public static final DeferredBlock<LogBlock> ORANGE_WOOD = BLOCKS.register("orange_wood", ()-> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WOOD), STRIPPED_ORANGE_WOOD));


    private static BlockBehaviour.@NotNull Properties liquidProperties(){
        return BlockBehaviour.Properties.of().liquid().noCollission().noLootTable().pushReaction(PushReaction.DESTROY).replaceable();
    }

    private static BlockBehaviour.@NotNull Properties decorProperties(){
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5f,6).sound(SoundType.STONE);
    }

    private static BlockBehaviour.@NotNull Properties doorProperties(){
        return decorProperties().sound(SoundType.METAL).pushReaction(PushReaction.BLOCK);
    }

    private static BlockBehaviour.@NotNull Properties bigDoorProperties(){
        return doorProperties().strength(2f, 8);
    }
}