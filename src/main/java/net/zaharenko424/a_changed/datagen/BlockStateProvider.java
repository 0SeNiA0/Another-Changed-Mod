package net.zaharenko424.a_changed.datagen;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.blocks.*;
import net.zaharenko424.a_changed.block.boxes.SmallCardboardBox;
import net.zaharenko424.a_changed.block.doors.Abstract2By2Door;
import net.zaharenko424.a_changed.block.doors.Abstract3By3Door;
import net.zaharenko424.a_changed.block.machines.AbstractMachine;
import net.zaharenko424.a_changed.block.machines.Capacitor;
import net.zaharenko424.a_changed.block.machines.WireBlock;
import net.zaharenko424.a_changed.block.smalldecor.MetalCan;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;
import static net.zaharenko424.a_changed.registry.BlockRegistry.*;
import static net.zaharenko424.a_changed.util.StateProperties.*;

@ParametersAreNonnullByDefault
public class BlockStateProvider extends net.neoforged.neoforge.client.model.generators.BlockStateProvider {

    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AChanged.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        rotatedDoublePartBlock(AIR_CONDITIONER,null);
        threeByThreeDoorWithItem(BIG_LAB_DOOR);
        rotatedDoublePartBlock(BIG_LAB_LAMP, null);
        threeByThreeDoorWithItem(BIG_LIBRARY_DOOR);
        threeByThreeDoorWithItem(BIG_MAINTENANCE_DOOR);
        blockWithItem(BLUE_LAB_TILE);
        slabWithItem(BLUE_LAB_TILE_SLAB, blockLoc(BLUE_LAB_TILE));
        stairsWithItem(BLUE_LAB_TILE_STAIRS, blockLoc(BLUE_LAB_TILE));
        blockWithItem(BOLTED_BLUE_LAB_TILE);
        blockWithItem(BOLTED_LAB_TILE);
        simpleBlock(BOOK_STACK.get());
        horizontalDirectionalBlockWithItem(BROKEN_CUP);
        horizontalDirectionalBlockWithItem(BROKEN_FLASK);
        blockWithItem(BROWN_LAB_BLOCK);
        metalCan(CANNED_ORANGES);
        allDirectionalBlockWithItem(CAPACITOR);
        horizontalDirectionalBlockWithItem(CARDBOARD_BOX);
        connectedTextureWithItem(CARPET_BLOCK,"carpet");
        horizontalDirectionalBlockWithItem(CHAIR);
        machineLikeWithItem(COMPRESSOR, false);
        horizontalDirectionalBlockWithItem(COMPUTER);
        connectedTextureWithItem(CONNECTED_BLUE_LAB_TILE,"blue_lab_tile");
        connectedTextureWithItem(CONNECTED_LAB_TILE,"lab_tile");
        wire(COPPER_WIRE);
        cryoChamber(CRYO_CHAMBER);
        horizontalDirectionalBlockWithItem(CUP);
        horizontalDirectionalBlockWithItem(DANGER_SIGN);
        blockWithItem(DARK_LATEX_BLOCK);
        crossWithItem(DARK_LATEX_CRYSTAL);
        blockWithItem(DARK_LATEX_CRYSTAL_ICE);
        simpleBlock(DARK_LATEX_FLUID_BLOCK.get(), models().getBuilder(DARK_LATEX_FLUID_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/dark_latex_still"));
        latexPuddleWithItem(DARK_LATEX_PUDDLE_F, false);
        latexPuddleWithItem(DARK_LATEX_PUDDLE_M, false);
        horizontalDirectionalBlockWithItem(DERELICT_LATEX_ENCODER);
        horizontalDirectionalBlockWithItem(DERELICT_LATEX_PURIFIER);
        machineLikeWithItem(DNA_EXTRACTOR, true);
        allDirectionalBlockWithItem(EXPOSED_PIPES);
        simpleBlockWithItemExisting(FLASK);
        rotatedDoublePartBlock(GAS_TANK,null);
        machineLikeWithItem(GENERATOR, false);
        doublePartCrossWithItem(GREEN_CRYSTAL);
        blockWithItem(HAZARD_BLOCK);
        slabWithItem(HAZARD_SLAB, blockLoc(HAZARD_BLOCK));
        stairsWithItem(HAZARD_STAIRS, blockLoc(HAZARD_BLOCK));
        pillarWithItem(HAZARD_LAB_BLOCK, blockLoc(LAB_BLOCK));
        rotatedDoublePartBlock(IV_RACK, null);
        keypadWithItem();
        blockWithItem(LAB_BLOCK);
        slabWithItem(LAB_SLAB, blockLoc(LAB_BLOCK));
        stairsWithItem(LAB_STAIRS, blockLoc(LAB_BLOCK));
        twoByTwoDoorWithItem(LAB_DOOR);
        allDirectionalBlockWithItem(LAB_LAMP);
        blockWithItem(LAB_TILE);
        slabWithItem(LAB_TILE_SLAB, blockLoc(LAB_TILE));
        stairsWithItem(LAB_TILE_STAIRS, blockLoc(LAB_TILE));
        laserWithItem();
        doublePartYBlockWItem(LATEX_CONTAINER);
        machineLikeWithItem(LATEX_ENCODER, false);
        machineLikeWithItem(LATEX_PURIFIER, false);
        simpleCubeWithItem(LATEX_RESISTANT_BLOCK);
        simpleBlockWithItem(LATEX_RESISTANT_GLASS.get(), models().cubeAll(LATEX_RESISTANT_GLASS.getId().getPath(), blockLoc(LATEX_RESISTANT_GLASS)).renderType("translucent"));
        paneBlockWithRenderType(LATEX_RESISTANT_GLASS_PANE.get(), blockLoc(LATEX_RESISTANT_GLASS), blockLoc(LATEX_RESISTANT_GLASS), "translucent");
        simpleBlock(LATEX_SOLVENT_BLOCK.get(),models().getBuilder(LATEX_SOLVENT_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/latex_solvent_still"));
        twoByTwoDoorWithItem(LIBRARY_DOOR);
        twoByTwoDoorWithItem(MAINTENANCE_DOOR);
        rotatedDoublePartBlock(METAL_BOX,null);
        metalCan(METAL_CAN);
        horizontalDirectionalBlockWithItem(BlockRegistry.NOTE);
        horizontalDirectionalBlockWithItem(NOTEPAD);
        orange();

        ResourceLocation planks = blockLoc(ORANGE_PLANKS);
        buttonBlock(ORANGE_BUTTON.get(), planks);
        ResourceLocation door = blockLoc(ORANGE_DOOR);
        doorBlock(ORANGE_DOOR.get(), door.withSuffix("_bottom"), door.withSuffix("_top"));
        fenceBlock(ORANGE_FENCE.get(), planks);
        fenceGateBlock(ORANGE_FENCE_GATE.get(), planks);
        hangingSignBlock(ORANGE_HANGING_SIGN, ORANGE_WALL_HANGING_SIGN, planks);
        blockWithItem(ORANGE_LAB_BLOCK);
        slabWithItem(ORANGE_LAB_SLAB, blockLoc(ORANGE_LAB_BLOCK));
        stairsWithItem(ORANGE_LAB_STAIRS, blockLoc(ORANGE_LAB_BLOCK));
        leavesWithItem(ORANGE_LEAVES);
        simpleCubeWithItem(ORANGE_PLANKS);
        pressurePlateBlock(ORANGE_PRESSURE_PLATE.get(), planks);
        crossWithItem(ORANGE_SAPLING);
        signBlock(ORANGE_SIGN.get(), ORANGE_WALL_SIGN.get(), planks);
        slabWithItem(ORANGE_SLAB, planks);
        stairsWithItem(ORANGE_STAIRS, planks);
        trapdoorBlock(ORANGE_TRAPDOOR.get(), planks, true);

        logWithItem(ORANGE_TREE_LOG, null, null);
        logWithItem(ORANGE_WOOD, blockLoc(ORANGE_TREE_LOG), blockLoc(ORANGE_TREE_LOG));
        pipe();
        simpleBlockWithItem(POTTED_ORANGE_SAPLING.get(), models().singleTexture(POTTED_ORANGE_SAPLING.getId().getPath(),
                ResourceLocation.withDefaultNamespace("flower_pot_cross"), "plant", blockLoc(ORANGE_SAPLING)).renderType("cutout"));
        blockExisting(ROTATING_CHAIR);
        horizontalDirectionalBlockWithItem(SCANNER);
        smallCardboardBoxPileWithItem();
        smartSewageSystemWithItem();
        pillarWithItem(STRIPED_ORANGE_LAB_BLOCK, blockLoc(ORANGE_LAB_BLOCK));
        logWithItem(STRIPPED_ORANGE_LOG, null, blockLoc(ORANGE_TREE_LOG).withSuffix("_top"));
        logWithItem(STRIPPED_ORANGE_WOOD, blockLoc(STRIPPED_ORANGE_LOG), blockLoc(STRIPPED_ORANGE_LOG));
        tableModel();
        rotatedDoublePartBlock(TALL_CARDBOARD_BOX,"tall_box");
        horizontalDirectionalBlockWithItem(TEST_TUBES);
        simpleBlockWithItemExisting(TRAFFIC_CONE);
        rotatedDoublePartBlock(TV_SCREEN, null);
        ventDuct();
        ventHatchWithItem();
        pillarWithItem(VENT_WALL,null);
        blockWithItem(WHITE_LATEX_BLOCK);
        simpleBlock(WHITE_LATEX_FLUID_BLOCK.get(), models().getBuilder(WHITE_LATEX_FLUID_BLOCK.getId().getPath()).texture("particle", AChanged.MODID + ":block/white_latex_still"));
        doublePartYBlockWOItem(WHITE_LATEX_PILLAR);
        latexPuddleWithItem(WHITE_LATEX_PUDDLE_F, true);
        latexPuddleWithItem(WHITE_LATEX_PUDDLE_M, true);
        blockWithItem(YELLOW_LAB_BLOCK);
        slabWithItem(YELLOW_LAB_SLAB, blockLoc(YELLOW_LAB_BLOCK));
        stairsWithItem(YELLOW_LAB_STAIRS, blockLoc(YELLOW_LAB_BLOCK));
    }

    private void pipe(){
        ResourceLocation id = PIPE.getId();
        ResourceLocation blockLoc = blockLoc(id.withPrefix("pipe/"));
        ModelFile pipe_n = models().getExistingFile(blockLoc.withSuffix("_n"));
        ModelFile pipe_ne = models().getExistingFile(blockLoc.withSuffix("_ne"));
        ModelFile pipe_ns = models().getExistingFile(blockLoc.withSuffix("_ns"));
        getVariantBuilder(PIPE.get()).forAllStates(state -> {
            boolean n = state.getValue(NORTH);
            boolean e = state.getValue(EAST);
            boolean s = state.getValue(SOUTH);
            boolean w = state.getValue(WEST);

            if(n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(pipe_n)};
            if(!n && e && !s && !w) return horizontalRotatedModelAr(pipe_n, Direction.EAST);
            if(!n && !e && s && !w) return horizontalRotatedModelAr(pipe_n, Direction.SOUTH);
            if(!n && !e && !s && w) return horizontalRotatedModelAr(pipe_n, Direction.WEST);

            if(n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(pipe_ne)};
            if(!n && e && s && !w) return horizontalRotatedModelAr(pipe_ne, Direction.EAST);
            if(!n && !e && s) return horizontalRotatedModelAr(pipe_ne, Direction.SOUTH);
            if(n && !e && !s) return horizontalRotatedModelAr(pipe_ne, Direction.WEST);

            if(!n && e && !s) return horizontalRotatedModelAr(pipe_ns, Direction.EAST);
            return new ConfiguredModel[]{new ConfiguredModel(pipe_ns)};
        });
        itemModels().getBuilder(id.getPath()).parent(pipe_n);
    }

    private final ResourceLocation WIDE_CROSS = blockLoc(AChanged.resourceLoc("wide_cross"));

    private @NotNull ResourceLocation blockLoc(ResourceLocation loc){
        return loc.withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    private @NotNull ResourceLocation blockLoc(DeferredBlock<?> block){
        return blockLoc(block.getId());
    }

    private void blockExisting(DeferredBlock<?> block){
        simpleBlock(block.get(), models().getExistingFile(blockLoc(block)));
    }

    private void blockWithItem(DeferredBlock<?> block){
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void allDirectionalBlockWithItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile file = models().getExistingFile(id);
        getVariantBuilder(block.get()).forAllStates(state -> rotatedModelAr(file, state.getValue(Capacitor.FACING)));
        itemModels().getBuilder(id.getPath()).parent(file);
    }

    private void crossWithItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        simpleBlock(block.get(), models().cross(id.getPath(), blockLoc(id)).renderType("cutout"));
        simpleItem(id, blockLoc(id));
    }

    private void doublePartCrossWithItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ResourceLocation blockLoc = blockLoc(id);
        ModelFile part0 = wideCross(id.getPath() + "_0", blockLoc.withSuffix("_0"));
        ModelFile part1 = wideCross(id.getPath() + "_1", blockLoc.withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                state.getValue(StateProperties.PART2) == 0 ? new ConfiguredModel[]{new ConfiguredModel(part0)} : new ConfiguredModel[]{new ConfiguredModel(part1)});
        simpleItem(id, blockLoc.withSuffix("_1"));
    }

    private ModelFile cube(ResourceLocation textureLoc, String modelId, String str0, String str1, String str2, String str3, String str5, String str4){
        return models().cube(textureLoc+modelId, textureLoc.withSuffix(str0), textureLoc.withSuffix(str1), textureLoc.withSuffix(str2), textureLoc.withSuffix(str3), textureLoc.withSuffix(str4), textureLoc.withSuffix(str5)).texture("particle", textureLoc.withSuffix("0c"));
    }

    private void doublePartYBlockWOItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile part0 = models().getExistingFile(blockLoc(id));
        ModelFile part_1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                new ConfiguredModel[]{new ConfiguredModel(state.getValue(StateProperties.PART2) == 1 ? part_1 : part0)}
        );
    }

    private void doublePartYBlockWItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile part0 = models().getExistingFile(blockLoc(id));
        ModelFile part_1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                new ConfiguredModel[]{new ConfiguredModel(state.getValue(StateProperties.PART2) == 1 ? part_1 : part0)}
        );
        itemModels().getBuilder(id.getPath()).parent(part0);
    }

    private void machineLikeWithItem(DeferredBlock<? extends AbstractMachine> block, boolean sameActiveModel){
        if(!block.get().defaultBlockState().hasProperty(ACTIVE)) return;
        ResourceLocation loc = blockLoc(block.getId());
        ModelFile file = models().getExistingFile(loc);
        ModelFile file_active = sameActiveModel ? file : models().getExistingFile(loc.withSuffix("_active"));
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
            if(state.getValue(ACTIVE)){
                return horizontalRotatedModelAr(file_active, direction);
            }
            return horizontalRotatedModelAr(file, direction);
        });
        simpleBlockItem(block.get(), file);
    }

    private void hangingSignBlock(DeferredBlock<?> signBlock, DeferredBlock<?> wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(signBlock.getId().getPath(), texture);
        simpleBlock(signBlock.get(), sign);
        simpleBlock(wallSignBlock.get(), sign);
    }

    private void horizontalDirectionalBlock(DeferredBlock<? extends HorizontalDirectionalBlock> block){
        ModelFile file = models().getExistingFile(blockLoc(block));
        getVariantBuilder(block.get())
                .forAllStates(state-> horizontalRotatedModelAr(file, state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    private void horizontalDirectionalBlockWithItem(DeferredBlock<? extends HorizontalDirectionalBlock> block){
        ModelFile file = models().getExistingFile(blockLoc(block));
        simpleBlockItem(block.get(), file);
        getVariantBuilder(block.get())
                .forAllStates(state-> horizontalRotatedModelAr(file, state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    private ConfiguredModel horizontalRotatedModel(ModelFile file, Direction direction){
        return switch (direction){
            case EAST -> new ConfiguredModel(file, 0, 90, false);
            case SOUTH -> new ConfiguredModel(file, 0, 180, false);
            case WEST -> new ConfiguredModel(file, 0, 270, false);
            default -> new ConfiguredModel(file);
        };
    }

    private ConfiguredModel rotatedModel(ModelFile file, Direction direction){
        return switch (direction){
            case UP -> new ConfiguredModel(file, -90, 0, false);
            case DOWN -> new ConfiguredModel(file, 90, 0, false);
            default -> horizontalRotatedModel(file, direction);
        };
    }

    @Contract("_, _ -> new")
    private ConfiguredModel @NotNull [] horizontalRotatedModelAr(ModelFile file, Direction direction){
        return new ConfiguredModel[]{horizontalRotatedModel(file, direction)};
    }

    @Contract("_, _ -> new")
    private ConfiguredModel @NotNull [] rotatedModelAr(ModelFile file, Direction direction){
        return new ConfiguredModel[]{rotatedModel(file, direction)};
    }

    private void keypadWithItem(){
        ResourceLocation id = KEYPAD.getId();
        ModelFile file = models().getExistingFile(blockLoc(id));
        ModelFile file_unlocked = models().getExistingFile(blockLoc(id).withSuffix("_unlocked"));
        getVariantBuilder(KEYPAD.get()).forAllStates(state -> {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
            return state.getValue(StateProperties.UNLOCKED) ? horizontalRotatedModelAr(file_unlocked, direction) : horizontalRotatedModelAr(file, direction);
        });
        itemModels().getBuilder(id.getPath()).parent(file);
    }

    private void laserWithItem(){
        ResourceLocation loc = blockLoc(LASER_EMITTER);
        ModelFile active = models().getExistingFile(loc.withSuffix("_active"));
        ModelFile inactive = models().getExistingFile(loc.withSuffix("_inactive"));
        getVariantBuilder(LASER_EMITTER.get())
                .forAllStates(state ->
                        rotatedModelAr(state.getValue(StateProperties.ACTIVE) ? active : inactive, state.getValue(LaserEmitter.FACING)));
        itemModels().getBuilder(LASER_EMITTER.getId().getPath()).parent(inactive);
    }

    private void latexPuddleWithItem(DeferredBlock<LatexPuddle> puddle, boolean white){
        ResourceLocation loc = blockLoc(AChanged.resourceLoc((white ? "white" : "dark") + "_latex_puddle/latex_puddle"));
        ModelFile puddle0 = models().getExistingFile(loc);
        ModelFile puddleN = models().getExistingFile(loc.withSuffix("_n"));
        ModelFile puddleNE = models().getExistingFile(loc.withSuffix("_ne"));
        ModelFile puddleNS = models().getExistingFile(loc.withSuffix("_ns"));
        ModelFile puddleNES = models().getExistingFile(loc.withSuffix("_nes"));
        ModelFile puddleNESW = models().getExistingFile(loc.withSuffix("_nesw"));
        getVariantBuilder(puddle.get()).forAllStates(state -> {
            boolean n = state.getValue(NORTH);
            boolean e = state.getValue(EAST);
            boolean s = state.getValue(SOUTH);
            boolean w = state.getValue(WEST);
            if(n && e && s && w) return new ConfiguredModel[]{new ConfiguredModel(puddleNESW)};

            if(n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(puddleN)};
            if(!n && e && !s && !w) return horizontalRotatedModelAr(puddleN, Direction.EAST);
            if(!n && !e && s && !w) return horizontalRotatedModelAr(puddleN, Direction.SOUTH);
            if(!n && !e && !s && w) return horizontalRotatedModelAr(puddleN, Direction.WEST);

            if(n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(puddleNE)};
            if(!n && e && s && !w) return horizontalRotatedModelAr(puddleNE, Direction.EAST);
            if(!n && !e && s) return horizontalRotatedModelAr(puddleNE, Direction.SOUTH);
            if(n && !e && !s) return horizontalRotatedModelAr(puddleNE, Direction.WEST);

            if(n && !e && !w) return new ConfiguredModel[]{new ConfiguredModel(puddleNS)};
            if(!n && e && !s) return horizontalRotatedModelAr(puddleNS, Direction.EAST);

            if(n && e && s) return new ConfiguredModel[]{new ConfiguredModel(puddleNES)};
            if(!n && e) return horizontalRotatedModelAr(puddleNES, Direction.EAST);
            if(n && !e) return horizontalRotatedModelAr(puddleNES, Direction.SOUTH);
            if(n) return horizontalRotatedModelAr(puddleNES, Direction.WEST);

            return new ConfiguredModel[]{new ConfiguredModel(puddle0)};
        });
        itemModels().getBuilder(puddle.getId().getPath()).parent(puddle0);
    }

    private void leavesWithItem(DeferredBlock<?> leaves){
        ResourceLocation id = leaves.getId();
        simpleBlockWithItem(leaves.get(), models().withExistingParent(id.getPath(),"block/leaves").texture("all", blockLoc(id)));
    }

    private void logWithItem(DeferredBlock<? extends RotatedPillarBlock> block, @Nullable ResourceLocation side, @Nullable ResourceLocation top){
        ResourceLocation id = block.getId();
        ResourceLocation loc = side != null ? side : blockLoc(id);
        ResourceLocation end = top != null ? top : blockLoc(id).withSuffix("_top");
        ModelFile vertical = models().cubeColumn(id.getPath(), loc, end);
        ModelFile horizontal = models().cubeColumnHorizontal(id.getPath() + "_horizontal", loc, end);
        getVariantBuilder(block.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        itemModels().getBuilder(id.getPath()).parent(vertical);
    }

    private void metalCan(DeferredBlock<? extends MetalCan> can){
        ResourceLocation loc = blockLoc(can);
        ModelFile file = models().getExistingFile(loc);
        ModelFile file_open = models().getExistingFile(loc.withSuffix("_open"));
        getVariantBuilder(can.get()).forAllStates(state ->
                horizontalRotatedModelAr(state.getValue(MetalCan.OPEN) ? file_open : file, state.getValue(HORIZONTAL_FACING)));
        itemModels().getBuilder(can.getId().getPath()).parent(file);
    }

    private void orange(){
        ResourceLocation loc = blockLoc(ORANGE.getId().withPrefix("orange/"));
        getVariantBuilder(ORANGE.get()).forAllStates(state ->
                new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(loc.withSuffix("_" + state.getValue(Orange.AGE_5))))});
    }

    private void pillarWithItem(DeferredBlock<?> block, @Nullable ResourceLocation up){
        ResourceLocation loc = blockLoc(block);
        ResourceLocation top = up != null ? up : loc.withSuffix("_up");
        ModelFile file = models().cube(block.getId().getPath(), top, top, loc, loc, loc, loc).texture("particle",loc);
        simpleBlockWithItem(block.get(), file);
    }

    private void rotatedDoublePartBlock(DeferredBlock<?> block, @Nullable String subFolder){
        ResourceLocation id = block.getId();
        if(subFolder != null) id = id.withPrefix(subFolder + "/");
        else id = id.withPrefix(id.getPath() + "/");
        ModelFile part0 = models().getExistingFile(blockLoc(id).withSuffix("_0"));
        ModelFile part1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(state.getValue(StateProperties.PART2) == 1 ? part1 : part0, state.getValue(HorizontalDirectionalBlock.FACING))
        );
    }

    private void simpleCubeWithItem(DeferredBlock<?> block){
        ModelFile file = cubeAll(block.get());
        simpleBlockWithItem(block.get(), file);
    }

    private void simpleBlockWithItemExisting(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile model = models().getExistingFile(blockLoc(id));
        getVariantBuilder(block.get()).forAllStates(state -> new ConfiguredModel[]{new ConfiguredModel(model)});
        itemModels().getBuilder(id.getPath()).parent(model);
    }

    private void simpleItem(ResourceLocation id, ResourceLocation texture){
        itemModels().getBuilder(id.getPath()).parent(models().getExistingFile(ResourceLocation.withDefaultNamespace("item/generated"))).texture("layer0", texture);
    }

    private void slabWithItem(DeferredBlock<? extends SlabBlock> slab, ResourceLocation texture){
        slabBlock(slab.get(), texture, texture);
        itemModels().slab(slab.getId().getPath(), texture, texture, texture);
    }

    private void smallCardboardBoxPileWithItem(){
        ResourceLocation id = blockLoc(SMALL_CARDBOARD_BOX.getId().withPrefix("box_pile/"));
        ModelFile file1 = models().getExistingFile(id.withSuffix("_1"));
        ModelFile file2 = models().getExistingFile(id.withSuffix("_2"));
        ModelFile file3 = models().getExistingFile(id.withSuffix("_3"));
        getVariantBuilder(SMALL_CARDBOARD_BOX.get())
                .forAllStates(state -> {
                    Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
                    return switch (state.getValue(SmallCardboardBox.BOX_AMOUNT)){
                        case 2 -> horizontalRotatedModelAr(file2, direction);
                        case 3 -> horizontalRotatedModelAr(file3, direction);
                        default -> horizontalRotatedModelAr(file1, direction);
                    };
                });
        itemModels().getBuilder(SMALL_CARDBOARD_BOX.getId().getPath()).parent(file1);
    }

    private void smartSewageSystemWithItem(){
        ResourceLocation id = SMART_SEWAGE_SYSTEM.getId();
        ResourceLocation loc = blockLoc(LAB_BLOCK);
        ResourceLocation top = blockLoc(id);
        String path = id.getPath();
        ModelFile file = models().cube(path, top, top, loc, loc, loc, loc).texture("particle", loc);
        getVariantBuilder(SMART_SEWAGE_SYSTEM.get()).forAllStates(state ->
                horizontalRotatedModelAr(file, state.getValue(HorizontalDirectionalBlock.FACING)));
        itemModels().getBuilder(path).parent(file);
    }

    private void stairsWithItem(DeferredBlock<? extends StairBlock> stairs, ResourceLocation texture){
        stairsBlock(stairs.get(), texture);
        itemModels().stairs(stairs.getId().getPath(), texture, texture, texture);
    }

    private void tableModel(){
        ResourceLocation id = blockLoc(TABLE);
        ModelFile file1 = models().getExistingFile(id.withSuffix("_top"));
        ModelFile file2 = models().getExistingFile(id.withSuffix("_leg"));
        getMultipartBuilder(TABLE.get()).part().modelFile(file1).addModel().end()
                .part().modelFile(file2).addModel().condition(Table.LEG_1,true).end()
                .part().modelFile(file2).rotationY(90).addModel().condition(Table.LEG_2,true).end()
                .part().modelFile(file2).rotationY(180).addModel().condition(Table.LEG_3,true).end()
                .part().modelFile(file2).rotationY(270).addModel().condition(Table.LEG_4,true).end();
    }

    private void cryoChamber(DeferredBlock<CryoChamber> block){
        ResourceLocation loc = blockLoc(block.getId().withPrefix("cryo_chamber/"));
        getVariantBuilder(block.get()).forAllStates(state -> horizontalRotatedModelAr(models()
                .getExistingFile(loc.withSuffix("_" + state.getValue(StateProperties.PART12)
                        + (state.getValue(CryoChamber.OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }

    private void threeByThreeDoorWithItem(DeferredBlock<? extends Abstract3By3Door> block){
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(models().getExistingFile(id.withSuffix("_" + state.getValue(PART9) + (state.getValue(OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));

    }

    private void twoByTwoDoorWithItem(DeferredBlock<? extends Abstract2By2Door> block){
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(models().getExistingFile(id.withSuffix("_" + state.getValue(PART4) + (state.getValue(OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }

    private void ventDuct(){
        ResourceLocation id = VENT_DUCT.getId();
        ResourceLocation loc = blockLoc(id.withPrefix("vent_duct/"));

        // _letters -> sides with holes
        ModelFile duct = models().getExistingFile(loc);
        ModelFile duct_n = models().getExistingFile(loc.withSuffix("_n"));

        ModelFile duct_ns = models().getExistingFile(loc.withSuffix("_ns"));
        ModelFile duct_ns_ = models().getExistingFile(loc.withSuffix("_ns_"));
        ModelFile duct_ns_b = models().getExistingFile(loc.withSuffix("_ns_b"));

        ModelFile duct_eu = models().getExistingFile(loc.withSuffix("_eu"));
        ModelFile duct_wud = models().getExistingFile(loc.withSuffix("_wud"));
        ModelFile duct_ewu = models().getExistingFile(loc.withSuffix("_ewu"));
        ModelFile duct_esu = models().getExistingFile(loc.withSuffix("_esu"));
        ModelFile duct_ewud = models().getExistingFile(loc.withSuffix("_ewud"));
        ModelFile duct_eswud = models().getExistingFile(loc.withSuffix("_eswud"));
        ModelFile duct_nswd = models().getExistingFile(loc.withSuffix("_nswd"));
        ModelFile duct_neswud = models().getExistingFile(loc.withSuffix("_neswud"));

        getVariantBuilder(VENT_DUCT.get()).forAllStates(state -> {
            boolean u = state.getValue(UP);
            boolean d = state.getValue(DOWN);
            boolean n = state.getValue(NORTH);
            boolean e = state.getValue(EAST);
            boolean s = state.getValue(SOUTH);
            boolean w = state.getValue(WEST);
            int flags = state.getValue(FLAGS3);

            int sidesConnected = 0;
            Object2BooleanArrayMap<Direction> map = new Object2BooleanArrayMap<>();
            boolean b;
            for(Direction direction : Direction.values()){
                b = state.getValue(ConnectedTextureBlock.propByDirection.get(direction));
                map.put(direction, b);
                if(b) sidesConnected++;
            }

            return new ConfiguredModel[]{switch (sidesConnected) {// == amount of holes
                case 0 -> new ConfiguredModel(duct);
                case 1 -> rotatedModel(duct_n, firstMatches(map, true));
                case 2 -> {
                    if (isStraightThrough(map, true)) {
                        yield rotatedModel(flags == 2 ? duct_ns_b : flags == 1 ? duct_ns_ : duct_ns, firstMatches(map, true));
                    } else yield cornerModel2(u, d, n, e, s, w, duct_eu);
                }
                case 3 -> model3(u, d, n, e, s, w, duct_wud, duct_ewu, duct_esu);
                case 4 -> {
                    if (isStraightThrough(map, false)) {
                        yield rotatedModel(duct_ewud, firstMatches(map, false));
                    } else yield cornerModel2(!u, !d, !n, !e, !s, !w, duct_nswd);
                }
                case 5 -> rotatedModel(duct_eswud, firstMatches(map, false));
                default -> new ConfiguredModel(duct_neswud);
            }};
        });
    }

    private Direction firstMatches(Object2BooleanArrayMap<Direction> map, boolean match){
        ObjectIterator<Object2BooleanMap.Entry<Direction>> iterator = map.object2BooleanEntrySet().fastIterator();
        while(iterator.hasNext()){
            Object2BooleanMap.Entry<Direction> entry = iterator.next();
            if(entry.getBooleanValue() == match) return entry.getKey();
        }
        return Direction.NORTH;
    }

    private boolean isStraightThrough(Object2BooleanArrayMap<Direction> map, boolean match){
        return map.getBoolean(firstMatches(map, match).getOpposite()) == match;
    }

    private void ventHatchWithItem(){
        ResourceLocation id = VENT_HATCH.getId();
        ModelFile top = models().getExistingFile(blockLoc(id).withSuffix("_top"));
        ModelFile bottom = models().getExistingFile(blockLoc(id).withSuffix("_bottom"));
        ModelFile open = models().getExistingFile(blockLoc(id).withSuffix("_open"));
        trapdoorBlock(VENT_HATCH.get(), bottom, top, open, true);
        itemModels().getBuilder(id.getPath()).parent(bottom);
    }

    private @NotNull ModelFile wideCross(String path, ResourceLocation texture){
        return models().withExistingParent(path, WIDE_CROSS).texture("1", texture);
    }

    private void wire(DeferredBlock<WireBlock> wire){
        ResourceLocation wireLoc = AChanged.resourceLoc("block/wire/wire");
        ModelFile wire_ = models().getExistingFile(wireLoc);
        ModelFile wire_n = models().getExistingFile(wireLoc.withSuffix("_n"));
        ModelFile wire_u = models().getExistingFile(wireLoc.withSuffix("_u"));
        ModelFile wire_ns = models().getExistingFile(wireLoc.withSuffix("_ns"));
        itemModels().getBuilder(wire.getId().getPath()).parent(wire_ns);
        getMultipartBuilder(wire.get())
                .part().modelFile(wire_).addModel().end()
                .part().modelFile(wire_n).addModel().condition(NORTH, true).end()
                .part().modelFile(wire_n).rotationY(90).addModel().condition(EAST, true).end()
                .part().modelFile(wire_n).rotationY(-180).addModel().condition(SOUTH, true).end()
                .part().modelFile(wire_n).rotationY(-90).addModel().condition(WEST, true).end()
                .part().modelFile(wire_u).addModel().condition(UP, true).end()
                .part().modelFile(wire_u).rotationX(180).addModel().condition(DOWN, true).end();
    }

    private void connectedTextureWithItem(DeferredBlock<?> block, String subFolder){
        ResourceLocation textureLoc = block.getId().withPrefix("block/" + subFolder + "/");
        ConfiguredModel c0 = new ConfiguredModel(models().cubeAll(textureLoc + "_c0", textureLoc.withSuffix("0c")));
        itemModels().getBuilder(block.getId().getPath()).parent(c0.model);
        ModelFile c1 = cube(textureLoc,"_c1","0c","4c","1c_u","1c_u","1c_u","1c_u");
        ModelFile c2 = cube(textureLoc,"_c2","4c","4c","2c_ud","2c_ud","2c_ud","2c_ud");
        ModelFile c2angle = cube(textureLoc,"_c2angle","1c_e","4c","2c_uw","2c_ue","1c_u","4c");
        ModelFile c3t1 = cube(textureLoc,"_c3t1","4c","4c","3c_ued","3c_uwd","4c","2c_ud");
        ModelFile c3t2 = cube(textureLoc,"_c3t2","2c_we","4c","3c_uwe","3c_uwe","4c","4c");
        ModelFile c3angle = cube(textureLoc,"_c3angle","2c_ue","4c","2c_uw","4c","2c_ue","4c");
        ModelFile c4x = cube(textureLoc,"_c4x","4c","4c","4c","4c","4c","4c");
        ModelFile c4angle = cube(textureLoc,"_c4angle","4c","4c","4c","3c_uwd","4c","3c_ued");
        ConfiguredModel middle = new ConfiguredModel(models().cubeAll(textureLoc+"_middle", textureLoc.withSuffix("4c")));
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            boolean u = state.getValue(UP);
            boolean d = state.getValue(DOWN);
            boolean n = state.getValue(NORTH);
            boolean e = state.getValue(EAST);
            boolean s = state.getValue(SOUTH);
            boolean w = state.getValue(WEST);
            if(!u && !d && !n && !e && !s && !w) return new ConfiguredModel[]{c0};

            if(u && !d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1)};
            if(!u && d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,180,0,false)};
            if(!u && !d && n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,90,0,false)};
            if(!u && !d && !n && !e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,-90,0,false)};
            if(!u && !d && !n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c1,90,-90,false)};
            if(!u && !d && !n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,90,90,false)};

            ConfiguredModel model = cornerModel2(u, d, n, e, s, w, c2angle);
            if(model != null) return new ConfiguredModel[]{model};

            if(u && d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2)};
            if(!u && !d && !n && !s)           return new ConfiguredModel[]{new ConfiguredModel(c2,-90,90,false)};
            if(!u && !d && n && !e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c2,-90,0,false)};

            model = model3(u, d, n, e, s, w, c3t1, c3t2, c3angle);
            if(model != null) return new ConfiguredModel[]{model};

            if(u && d && !n && e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c4x)};
            if(!u && !d)                     return new ConfiguredModel[]{new ConfiguredModel(c4x,90,0,false)};
            if(u && d && n && !e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c4x,0,90,false)};

            if(u && d && n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c4angle)};
            if(!u && n && !e)                return new ConfiguredModel[]{new ConfiguredModel(c4angle,90,0,false)};
            if(u && !d && n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,0,false)};
            if(u && d && !n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,-90,false)};
            if(u && d && n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,90,false)};
            if(u && d && !n && !w)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,180,false)};
            if(u && !d && n && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,180,false)};
            if(!u && n && s && !w)      return new ConfiguredModel[]{new ConfiguredModel(c4angle,90,180,false)};
            if(u && !d && !n)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,-90,false)};
            if(!u && !n)                return new ConfiguredModel[]{new ConfiguredModel(c4angle,-270,-90,false)};
            if(u && !d && !s)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,90,false)};
            if(!u && !s)                return new ConfiguredModel[]{new ConfiguredModel(c4angle,-270,-270,false)};

            return new ConfiguredModel[]{middle};
        }, LOCKED_STATE);
    }

    private ConfiguredModel cornerModel2(boolean u, boolean d, boolean n, boolean e, boolean s, boolean w, ModelFile file_ue){
        if(u && !d && !n && e && !s && !w) return new ConfiguredModel(file_ue);
        if(u && !d && !n && !e && !s && w) return new ConfiguredModel(file_ue,0,180,false);
        if(!u && d && !n && e && !s && !w) return new ConfiguredModel(file_ue,180,0,false);
        if(!u && d && !n && !e && !s && w) return new ConfiguredModel(file_ue,180,180,false);
        if(u && !d && n && !e && !s && !w) return new ConfiguredModel(file_ue,0,-90,false);
        if(u && !d && !n && !e && s && !w) return new ConfiguredModel(file_ue,0,90,false);
        if(!u && d && n && !e && !s && !w) return new ConfiguredModel(file_ue,180,-90,false);
        if(!u && d && !n && !e && s && !w) return new ConfiguredModel(file_ue,180,90,false);
        if(!u && !d && n && e && !s && !w) return new ConfiguredModel(file_ue,90,0,false);
        if(!u && !d && !n && e && s && !w) return new ConfiguredModel(file_ue,-90,0,false);
        if(!u && !d && n && !e && !s && w) return new ConfiguredModel(file_ue,90,-90,false);
        if(!u && !d && !n && !e && s && w) return new ConfiguredModel(file_ue,-90,90,false);
        return null;
    }

    private ConfiguredModel model3(boolean u, boolean d, boolean n, boolean e, boolean s, boolean w, ModelFile file_wud, ModelFile file_ewu, ModelFile file_esu){
        if(u && d && !n && !e && !s && w)      return new ConfiguredModel(file_wud);
        if(u && d && !n && !e && s && !w)      return new ConfiguredModel(file_wud,0,-90,false);
        if(u && d && !n && e && !s && !w) return new ConfiguredModel(file_wud,0,-180,false);
        if(u && d && n && !e && !s && !w) return new ConfiguredModel(file_wud,0,90,false);
        if(!u && !d && n && !e && s && w)           return new ConfiguredModel(file_wud,-90,0,false);
        if(!u && !d && n && e && s && !w)      return new ConfiguredModel(file_wud,-90,180,false);

        if(u && !d && !n && e && !s && w)      return new ConfiguredModel(file_ewu);
        if(!u && !d && !n && e && s && w)                return new ConfiguredModel(file_ewu,-90,0,false);
        if(!u && d && !n && e && !s && w)      return new ConfiguredModel(file_ewu,180,0,false);
        if(!u && !d && n && e && !s && w)                return new ConfiguredModel(file_ewu,90,0,false);
        if(u && !d && n && !e && s && !w) return new ConfiguredModel(file_ewu,0,90,false);
        if(!u && d && n && !e && s && !w) return new ConfiguredModel(file_ewu,180,90,false);

        if(u && !d && !n && e && s && !w)      return new ConfiguredModel(file_esu);
        if(u && !d && !n && !e && s && w)           return new ConfiguredModel(file_esu,0,90,false);
        if(u && !d && n && e && !s && !w) return new ConfiguredModel(file_esu,90,0,false);
        if(!u && d && n && e && !s && !w) return new ConfiguredModel(file_esu,180,0,false);
        if(!u && d && !n && e && s && !w)      return new ConfiguredModel(file_esu,270,0,false);
        if(!u && d && !n && !e && s && w)           return new ConfiguredModel(file_esu,180,-180,false);
        if(!u && d && n && !e && !s && w)      return new ConfiguredModel(file_esu,-90,-180,false);
        if(u && !d && n && !e && !s && w)      return new ConfiguredModel(file_esu,90,-90,false);
        return null;
    }
}