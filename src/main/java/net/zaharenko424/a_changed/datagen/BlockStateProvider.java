package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.block.blocks.CryoChamber;
import net.zaharenko424.a_changed.block.blocks.LaserEmitter;
import net.zaharenko424.a_changed.block.blocks.LatexPuddle;
import net.zaharenko424.a_changed.block.blocks.Table;
import net.zaharenko424.a_changed.block.boxes.SmallCardboardBox;
import net.zaharenko424.a_changed.block.doors.Abstract2By2Door;
import net.zaharenko424.a_changed.block.doors.Abstract3By3Door;
import net.zaharenko424.a_changed.block.machines.AbstractMachine;
import net.zaharenko424.a_changed.block.machines.Capacitor;
import net.zaharenko424.a_changed.block.machines.WireBlock;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.StateProperties;
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
        rotatedDoublePartBlockWithItem(AIR_CONDITIONER,"air_conditioner");
        threeByThreeDoorWithItem(BIG_LAB_DOOR);
        threeByThreeDoorWithItem(BIG_LIBRARY_DOOR);
        threeByThreeDoorWithItem(BIG_MAINTENANCE_DOOR);
        blockWithItem(BLUE_LAB_TILE);
        blockWithItem(BOLTED_BLUE_LAB_TILE);
        blockWithItem(BOLTED_LAB_TILE);
        simpleBlock(BOOK_STACK.get());
        horizontalDirectionalBlockWithItem(BROKEN_CUP);
        horizontalDirectionalBlockWithItem(BROKEN_FLASK);
        blockWithItem(BROWN_LAB_BLOCK);
        allDirectionalBlock(CAPACITOR);
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
        machineLikeWithItem(DNA_EXTRACTOR, true);
        simpleBlockWithItemExisting(FLASK);
        rotatedDoublePartBlockWithItem(GAS_TANK,"gas_tank");
        machineLikeWithItem(GENERATOR, false);
        doublePartCrossWithItem(GREEN_CRYSTAL);
        blockWithItem(HAZARD_BLOCK);
        pillarWithItem(HAZARD_LAB_BLOCK, blockLoc(LAB_BLOCK.getId()));
        rotatedDoublePartBlockWithItem(IV_RACK, "iv_rack");
        keypadWithItem();
        blockWithItem(LAB_BLOCK);
        twoByTwoDoorWithItem(LAB_DOOR);
        blockWithItem(LAB_TILE);
        laserWithItem();
        doublePartYBlockWithItem(LATEX_CONTAINER);
        machineLikeWithItem(LATEX_ENCODER, false);
        machineLikeWithItem(LATEX_PURIFIER, false);
        simpleBlock(LATEX_SOLVENT_BLOCK.get(),models().getBuilder(LATEX_SOLVENT_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/latex_solvent_still"));
        twoByTwoDoorWithItem(LIBRARY_DOOR);
        twoByTwoDoorWithItem(MAINTENANCE_DOOR);
        rotatedDoublePartBlockWithItem(METAL_BOX,"metal_box");
        horizontalDirectionalBlockWithItem(BlockRegistry.NOTE);
        horizontalDirectionalBlockWithItem(NOTEPAD);

        ResourceLocation planks = blockLoc(ORANGE_PLANKS.getId());
        buttonBlock(ORANGE_BUTTON.get(), planks);
        ResourceLocation door = blockLoc(ORANGE_DOOR.getId());
        doorBlock(ORANGE_DOOR.get(), door.withSuffix("_bottom"), door.withSuffix("_top"));
        fenceBlock(ORANGE_FENCE.get(), planks);
        fenceGateBlock(ORANGE_FENCE_GATE.get(), planks);
        hangingSignBlock(ORANGE_HANGING_SIGN, ORANGE_WALL_HANGING_SIGN, planks);
        blockWithItem(ORANGE_LAB_BLOCK);
        leavesWithItem(ORANGE_LEAVES);
        simpleBlockWithItem(ORANGE_PLANKS.get(), cubeAll(ORANGE_PLANKS.get()));
        pressurePlateBlock(ORANGE_PRESSURE_PLATE.get(), planks);
        crossWithItem(ORANGE_SAPLING);
        signBlock(ORANGE_SIGN.get(), ORANGE_WALL_SIGN.get(), planks);
        slabBlock(ORANGE_SLAB.get(), planks, planks);
        stairsBlock(ORANGE_STAIRS.get(), planks);
        trapdoorBlock(ORANGE_TRAPDOOR.get(), planks, true);

        logWithItem(ORANGE_TREE_LOG, null, null);
        logWithItem(ORANGE_WOOD, blockLoc(ORANGE_TREE_LOG.getId()), blockLoc(ORANGE_TREE_LOG.getId()));
        pipe();
        simpleBlockWithItem(POTTED_ORANGE_SAPLING.get(), models().singleTexture(POTTED_ORANGE_SAPLING.getId().getPath(),
                new ResourceLocation("flower_pot_cross"), "plant", blockTexture(ORANGE_SAPLING.get())).renderType("cutout"));
        horizontalDirectionalBlockWithItem(SCANNER);
        smallCardboardBoxPileWithItem();
        smartSewageSystemWithItem();
        pillarWithItem(STRIPED_ORANGE_LAB_BLOCK,blockLoc(ORANGE_LAB_BLOCK.getId()));
        logWithItem(STRIPPED_ORANGE_LOG, null, blockLoc(ORANGE_TREE_LOG.getId()).withSuffix("_top"));
        logWithItem(STRIPPED_ORANGE_WOOD, blockLoc(STRIPPED_ORANGE_LOG.getId()), blockLoc(STRIPPED_ORANGE_LOG.getId()));
        tableModel();
        rotatedDoublePartBlockWithItem(TALL_CARDBOARD_BOX,"tall_box");
        horizontalDirectionalBlockWithItem(TEST_TUBES);
        simpleBlockWithItem(TRAFFIC_CONE.get(),models().getExistingFile(blockLoc(TRAFFIC_CONE.getId())));
        ventDuct();
        ventHatchWithItem();
        pillarWithItem(VENT_WALL,null);
        blockWithItem(WHITE_LATEX_BLOCK);
        simpleBlock(WHITE_LATEX_FLUID_BLOCK.get(), models().getBuilder(WHITE_LATEX_FLUID_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/white_latex_still"));
        latexPuddleWithItem(WHITE_LATEX_PUDDLE_F, true);
        latexPuddleWithItem(WHITE_LATEX_PUDDLE_M, true);
        blockWithItem(YELLOW_LAB_BLOCK);
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
            if(!n && e && !s && !w) return horizontalRotatedModel(pipe_n, Direction.EAST);
            if(!n && !e && s && !w) return horizontalRotatedModel(pipe_n, Direction.SOUTH);
            if(!n && !e && !s && w) return horizontalRotatedModel(pipe_n, Direction.WEST);

            if(n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(pipe_ne)};
            if(!n && e && s && !w) return horizontalRotatedModel(pipe_ne, Direction.EAST);
            if(!n && !e && s) return horizontalRotatedModel(pipe_ne, Direction.SOUTH);
            if(n && !e && !s) return horizontalRotatedModel(pipe_ne, Direction.WEST);

            if(!n && e && !s) return horizontalRotatedModel(pipe_ns, Direction.EAST);
            return new ConfiguredModel[]{new ConfiguredModel(pipe_ns)};
        });
        itemModels().getBuilder(id.getPath()).parent(pipe_n);
    }

    private final ResourceLocation WIDE_CROSS = blockLoc(AChanged.resourceLoc("wide_cross"));

    private @NotNull ResourceLocation blockLoc(ResourceLocation loc){
        return loc.withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    private void blockWithItem(DeferredBlock<?> block){
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void allDirectionalBlock(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile file = models().getExistingFile(id);
        getVariantBuilder(block.get()).forAllStates(state -> rotatedModel(file, state.getValue(Capacitor.FACING)));
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

    private void doublePartYBlockWithItem(DeferredBlock<?> block){
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
                return horizontalRotatedModel(file_active, direction);
            }
            return horizontalRotatedModel(file, direction);
        });
        simpleBlockItem(block.get(), file);
    }

    private void hangingSignBlock(DeferredBlock<?> signBlock, DeferredBlock<?> wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(signBlock.getId().getPath(), texture);
        simpleBlock(signBlock.get(), sign);
        simpleBlock(wallSignBlock.get(), sign);
    }

    private void horizontalDirectionalBlockWithItem(DeferredBlock<? extends HorizontalDirectionalBlock> block){
        ModelFile file = models().getExistingFile(blockLoc(block.getId()));
        simpleBlockItem(block.get(), file);
        getVariantBuilder(block.get())
                .forAllStates(state-> horizontalRotatedModel(file, state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    private ConfiguredModel[] horizontalRotatedModel(ModelFile file, Direction direction){
        return switch (direction){
            default -> new ConfiguredModel[]{new ConfiguredModel(file)};
            case EAST -> new ConfiguredModel[]{new ConfiguredModel(file, 0, 90, false)};
            case SOUTH -> new ConfiguredModel[]{new ConfiguredModel(file, 0, 180, false)};
            case WEST -> new ConfiguredModel[]{new ConfiguredModel(file, 0, 270, false)};
        };
    }

    private ConfiguredModel[] rotatedModel(ModelFile file, Direction direction){
        return switch (direction){
            case UP -> new ConfiguredModel[]{new ConfiguredModel(file, -90, 0, false)};
            case DOWN -> new ConfiguredModel[]{new ConfiguredModel(file, 90, 0, false)};
            default -> horizontalRotatedModel(file, direction);
        };
    }

    private void keypadWithItem(){
        ResourceLocation id = KEYPAD.getId();
        ModelFile file = models().getExistingFile(blockLoc(id));
        ModelFile file_unlocked = models().getExistingFile(blockLoc(id).withSuffix("_unlocked"));
        getVariantBuilder(KEYPAD.get()).forAllStates(state -> {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
            return state.getValue(StateProperties.UNLOCKED) ? horizontalRotatedModel(file_unlocked, direction) : horizontalRotatedModel(file, direction);
        });
        itemModels().getBuilder(id.getPath()).parent(file);
    }

    private void laserWithItem(){
        ResourceLocation id = LASER_EMITTER.getId();
        ResourceLocation loc = blockLoc(id);
        ModelFile active = models().getExistingFile(loc.withSuffix("_active"));
        ModelFile inactive = models().getExistingFile(loc.withSuffix("_inactive"));
        getVariantBuilder(LASER_EMITTER.get())
                .forAllStates(state ->
                        rotatedModel(state.getValue(StateProperties.ACTIVE) ? active : inactive, state.getValue(LaserEmitter.FACING)));
        itemModels().getBuilder(id.getPath()).parent(inactive);
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
            if(!n && e && !s && !w) return horizontalRotatedModel(puddleN, Direction.EAST);
            if(!n && !e && s && !w) return horizontalRotatedModel(puddleN, Direction.SOUTH);
            if(!n && !e && !s && w) return horizontalRotatedModel(puddleN, Direction.WEST);

            if(n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(puddleNE)};
            if(!n && e && s && !w) return horizontalRotatedModel(puddleNE, Direction.EAST);
            if(!n && !e && s) return horizontalRotatedModel(puddleNE, Direction.SOUTH);
            if(n && !e && !s) return horizontalRotatedModel(puddleNE, Direction.WEST);

            if(n && !e && !w) return new ConfiguredModel[]{new ConfiguredModel(puddleNS)};
            if(!n && e && !s) return horizontalRotatedModel(puddleNS, Direction.EAST);

            if(n && e && s) return new ConfiguredModel[]{new ConfiguredModel(puddleNES)};
            if(!n && e) return horizontalRotatedModel(puddleNES, Direction.EAST);
            if(n && !e) return horizontalRotatedModel(puddleNES, Direction.SOUTH);
            if(n) return horizontalRotatedModel(puddleNES, Direction.WEST);

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

    private void pillarWithItem(DeferredBlock<?> block, @Nullable ResourceLocation up){
        ResourceLocation loc = blockLoc(block.getId());
        ResourceLocation top = up != null ? up : loc.withSuffix("_up");
        ModelFile file = models().cube(block.getId().getPath(), top, top, loc, loc, loc, loc).texture("particle",loc);
        simpleBlockWithItem(block.get(), file);
    }

    private void rotatedDoublePartBlockWithItem(DeferredBlock<?> block, @Nullable String subFolder){
        ResourceLocation id = block.getId();
        if(subFolder != null) id = id.withPrefix(subFolder+"/");
        ModelFile part0 = models().getExistingFile(blockLoc(id).withSuffix("_0"));
        ModelFile part1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModel(state.getValue(StateProperties.PART2) == 1 ? part1 : part0, state.getValue(HorizontalDirectionalBlock.FACING))
        );
    }

    private void simpleBlockWithItemExisting(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile model = models().getExistingFile(blockLoc(id));
        getVariantBuilder(block.get()).forAllStates(state -> new ConfiguredModel[]{new ConfiguredModel(model)});
        itemModels().getBuilder(id.getPath()).parent(model);
    }

    private void simpleItem(ResourceLocation id, ResourceLocation texture){
        itemModels().getBuilder(id.getPath()).parent(models().getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", texture);
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
                        default -> horizontalRotatedModel(file1, direction);
                        case 2 -> horizontalRotatedModel(file2, direction);
                        case 3 -> horizontalRotatedModel(file3, direction);
                    };
                });
        itemModels().getBuilder(SMALL_CARDBOARD_BOX.getId().getPath()).parent(file1);
    }

    private void smartSewageSystemWithItem(){
        ResourceLocation id = SMART_SEWAGE_SYSTEM.getId();
        ResourceLocation loc = blockLoc(LAB_BLOCK.getId());
        ResourceLocation top = blockLoc(id);
        String path = id.getPath();
        ModelFile file = models().cube(path, top, top, loc, loc, loc, loc).texture("particle", loc);
        getVariantBuilder(SMART_SEWAGE_SYSTEM.get()).forAllStates(state ->
                horizontalRotatedModel(file, state.getValue(HorizontalDirectionalBlock.FACING)));
        itemModels().getBuilder(path).parent(file);
    }

    private void tableModel(){
        ResourceLocation id = blockLoc(TABLE.getId());
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
        getVariantBuilder(block.get()).forAllStates(state -> horizontalRotatedModel(models()
                .getExistingFile(loc.withSuffix("_" + state.getValue(StateProperties.PART12)
                        + (state.getValue(CryoChamber.OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }

    private void threeByThreeDoorWithItem(DeferredBlock<? extends Abstract3By3Door> block){
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModel(models().getExistingFile(id.withSuffix("_" + state.getValue(PART9) + (state.getValue(OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));

    }

    private void twoByTwoDoorWithItem(DeferredBlock<? extends Abstract2By2Door> block){
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModel(models().getExistingFile(id.withSuffix("_" + state.getValue(PART4) + (state.getValue(OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }

    private void ventDuct(){
        ResourceLocation id = VENT_DUCT.getId();
        ResourceLocation loc = blockLoc(id.withPrefix("vent_duct/"));
        /*ModelFile duct_n = models().getExistingFile(loc.withSuffix("_n"));
        ModelFile duct_nu = models().getExistingFile(loc.withSuffix("_nu"));

        ModelFile duct_ne = models().getExistingFile(loc.withSuffix("_ne"));
        ModelFile duct_neu = models().getExistingFile(loc.withSuffix("_neu"));

        ModelFile duct_nw = models().getExistingFile(loc.withSuffix("_nw"));
        ModelFile duct_ns = models().getExistingFile(loc.withSuffix("_ns"));

        ModelFile duct_nsu = models().getExistingFile(loc.withSuffix("_nsu"));
        ModelFile duct_nwu = models().getExistingFile(loc.withSuffix("_nwu"));

        ModelFile duct_new = models().getExistingFile(loc.withSuffix("_new"));
        ModelFile duct_newu = models().getExistingFile(loc.withSuffix("_newu"));

        ModelFile duct_nesw = models().getExistingFile(loc.withSuffix("_nesw"));
        ModelFile duct_neswu = models().getExistingFile(loc.withSuffix("_neswu"));

        ModelFile duct_n_ = models().getExistingFile(loc.withSuffix("_n_"));
        ModelFile duct_nu_ = models().getExistingFile(loc.withSuffix("_nu_"));

        ModelFile duct_ne_ = models().getExistingFile(loc.withSuffix("_ne_"));
        ModelFile duct_neu_ = models().getExistingFile(loc.withSuffix("_neu_"));

        ModelFile duct_nw_ = models().getExistingFile(loc.withSuffix("_nw_"));
        ModelFile duct_nwu_ = models().getExistingFile(loc.withSuffix("_nwu_"));

        ModelFile duct_ns_ = models().getExistingFile(loc.withSuffix("_ns_"));
        ModelFile duct_nsu_ = models().getExistingFile(loc.withSuffix("_nsu_"));


        ModelFile duct_new_ = models().getExistingFile(loc.withSuffix("_new_"));
        ModelFile duct_newu_ = models().getExistingFile(loc.withSuffix("_newu_"));

        ModelFile duct_nesw_ = models().getExistingFile(loc.withSuffix("_nesw_"));
        ModelFile duct_neswu_ = models().getExistingFile(loc.withSuffix("_neswu_"));*/

        ModelFile frame = models().getExistingFile(loc);
        //ModelFile frame_s = models().getExistingFile(loc.withSuffix("_s"));
        ModelFile wall = models().getExistingFile(loc.withSuffix("_wall"));
        //ModelFile wall_s = models().getExistingFile(loc.withSuffix("_wall_s"));
        //ModelFile wall_b = models().getExistingFile(loc.withSuffix("_wall_b"));
//will have to use variant builder and a bunch of models. make models for all states facing north to just have to rotate?
        /*getVariantBuilder(VENT_DUCT.get()).forAllStates(state -> {
            boolean u = state.getValue(UP);
            boolean d = state.getValue(DOWN);
            boolean n = state.getValue(NORTH);
            boolean e = state.getValue(EAST);
            boolean s = state.getValue(SOUTH);
            boolean w = state.getValue(WEST);
            boolean bars = state.getValue(BARS);
            boolean middle = state.getValue(IN_MIDDLE);

            int sidesConnected = 0;
            boolean[] ar = {u, d, n, e, s, w};
            for(boolean bool : ar){
                if(bool) sidesConnected++;
            }

            List<ConfiguredModel> models = new ArrayList<>();
            ModelFile wall_tmp;

            if(sidesConnected == 2 && middle){
                models.add(new ConfiguredModel(frame_s));
                wall_tmp = bars ? wall_b : wall_s;
            } else {
                models.add(new ConfiguredModel(frame));
                wall_tmp = wall;
            }

            if(!u) models.add(new ConfiguredModel(wall_tmp, -90, 0, false));
            if(!d) models.add(new ConfiguredModel(wall_tmp, 90, 0, false));
            if(!n) models.add(new ConfiguredModel(wall_tmp));
            if(!e) models.add(new ConfiguredModel(wall_tmp, 0, 90, false));
            if(!s) models.add(new ConfiguredModel(wall_tmp, 0, 180, false));
            if(!w) models.add(new ConfiguredModel(wall_tmp, 0, 270, false));

            return new ConfiguredModel[]{};
        });*/

        getMultipartBuilder(VENT_DUCT.get()).part().modelFile(frame).addModel().end()
                .part().modelFile(wall).rotationX(90).addModel().condition(DOWN, false).end()
                .part().modelFile(wall).rotationX(-90).addModel().condition(UP, false).end()
                .part().modelFile(wall).addModel().condition(NORTH,false).end()
                .part().modelFile(wall).rotationY(90).addModel().condition(EAST,false).end()
                .part().modelFile(wall).rotationY(180).addModel().condition(SOUTH,false).end()
                .part().modelFile(wall).rotationY(270).addModel().condition(WEST,false).end();
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
        getVariantBuilder(block.get()).forAllStates(state -> {
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

            if(u && !d && !n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2angle)};
            if(u && !d && !n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c2angle,0,180,false)};
            if(!u && d && !n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,0,false)};
            if(!u && d && !n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,180,false)};
            if(u && !d && n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2angle,0,-90,false)};
            if(u && !d && !n && !e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c2angle,0,90,false)};
            if(!u && d && n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,-90,false)};
            if(!u && d && !n && !e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,90,false)};
            if(!u && !d && n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2angle,90,0,false)};
            if(!u && !d && !n && e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2angle,-90,0,false)};
            if(!u && !d && n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c2angle,90,-90,false)};
            if(!u && !d && !n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c2angle,-90,90,false)};

            if(u && d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2)};
            if(!u && !d && !n && !s)           return new ConfiguredModel[]{new ConfiguredModel(c2,-90,90,false)};
            if(!u && !d && n && !e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c2,-90,0,false)};

            if(u && d && !n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c3t1)};
            if(u && d && !n && !e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c3t1,0,-90,false)};
            if(u && d && !n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c3t1,0,-180,false)};
            if(u && d && n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c3t1,0,90,false)};
            if(!u && !d && n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c3t1,-90,0,false)};
            if(!u && !d && n && s && !w)      return new ConfiguredModel[]{new ConfiguredModel(c3t1,-90,180,false)};

            if(u && !d && !n && e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c3t2)};
            if(!u && !d && !n)                return new ConfiguredModel[]{new ConfiguredModel(c3t2,-90,0,false)};
            if(!u && d && !n && e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c3t2,180,0,false)};
            if(!u && !d && !s)                return new ConfiguredModel[]{new ConfiguredModel(c3t2,90,0,false)};
            if(u && !d && n && !e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c3t2,0,90,false)};
            if(!u && d && n && !e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c3t2,180,90,false)};

            if(u && !d && !n && e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c3angle)};
            if(u && !d && !n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c3angle,0,90,false)};
            if(u && !d && n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c3angle,90,0,false)};
            if(!u && d && n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c3angle,180,0,false)};
            if(!u && d && !n && e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c3angle,270,0,false)};

            if(!u && d && !n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c3angle,180,-180,false)};
            if(!u && d && n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c3angle,-90,-180,false)};
            if(u && !d && n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c3angle,90,-90,false)};

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
        });
    }
}