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
import net.zaharenko424.a_changed.block.blocks.Keypad;
import net.zaharenko424.a_changed.block.blocks.Table;
import net.zaharenko424.a_changed.block.boxes.SmallCardboardBox;
import net.zaharenko424.a_changed.block.doors.AbstractMultiDoor;
import net.zaharenko424.a_changed.block.doors.AbstractTwoByTwoDoor;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zaharenko424.a_changed.block.blocks.ConnectedTextureBlock.*;
import static net.zaharenko424.a_changed.registry.BlockRegistry.*;

public class BlockStateProvider extends net.neoforged.neoforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AChanged.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        rotatedDoublePartBlockWithItem(AIR_CONDITIONER,"air_conditioner");
        blockWithItem(BLUE_LAB_TILE);
        blockWithItem(BOLTED_BLUE_LAB_TILE);
        blockWithItem(BOLTED_LAB_TILE);
        simpleBlock(BOOK_STACK.get());
        blockWithItem(BROWN_LAB_BLOCK);
        horizontalDirectionalBlockWithItem(CARDBOARD_BOX);
        connectedTextureWithItem(CARPET_BLOCK,"carpet");
        horizontalDirectionalBlockWithItem(CHAIR);
        horizontalDirectionalBlockWithItem(COMPUTER);
        connectedTextureWithItem(CONNECTED_BLUE_LAB_TILE,"blue_lab_tile");
        connectedTextureWithItem(CONNECTED_LAB_TILE,"lab_tile");
        crossWithItem(CRYSTAL);
        blockWithItem(DARK_LATEX_BLOCK);
        simpleBlock(DARK_LATEX_FLUID_BLOCK.get(),models().getBuilder(DARK_LATEX_FLUID_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/dark_latex_still"));
        rotatedDoublePartBlockWithItem(GAS_TANK,"gas_tank");
        doublePartCrossWithItem(GREEN_CRYSTAL);
        blockWithItem(HAZARD_BLOCK);
        pillarWithItem(HAZARD_LAB_BLOCK,blockLoc(LAB_BLOCK.getId()));
        keypadWithItem();
        blockWithItem(LAB_BLOCK);
        twoByTwoDoorWithItem(LAB_DOOR);
        blockWithItem(LAB_TILE);
        doublePartYBlockWithItem(LATEX_CONTAINER);
        simpleBlock(LATEX_SOLVENT_BLOCK.get(),models().getBuilder(LATEX_SOLVENT_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/latex_solvent_still"));
        twoByTwoDoorWithItem(LIBRARY_DOOR);
        twoByTwoDoorWithItem(MAINTENANCE_DOOR);
        rotatedDoublePartBlockWithItem(METAL_BOX,"metal_box");
        horizontalDirectionalBlockWithItem(NOTE);
        horizontalDirectionalBlockWithItem(NOTEPAD);
        blockWithItem(ORANGE_LAB_BLOCK);
        leavesWithItem(ORANGE_LEAVES);
        crossWithItem(ORANGE_SAPLING);
        logWithItem(ORANGE_TREE_LOG);
        horizontalDirectionalBlockWithItem(SCANNER);
        smallCardboardBoxPileWithItem();
        smartSewageSystemWithItem();
        pillarWithItem(STRIPED_ORANGE_LAB_BLOCK,blockLoc(ORANGE_LAB_BLOCK.getId()));
        tableModel();
        rotatedDoublePartBlockWithItem(TALL_CARDBOARD_BOX,"tall_box");
        simpleBlockWithItem(TRAFFIC_CONE.get(),models().getExistingFile(blockLoc(TRAFFIC_CONE.getId())));
        ventWithItem();
        pillarWithItem(VENT_WALL,null);
        blockWithItem(WHITE_LATEX_BLOCK);
        simpleBlock(WHITE_LATEX_FLUID_BLOCK.get(),models().getBuilder(WHITE_LATEX_FLUID_BLOCK.getId().getPath()).texture("particle", AChanged.MODID+":block/white_latex_still"));
        blockWithItem(YELLOW_LAB_BLOCK);
    }

    private final ResourceLocation WIDE_CROSS = blockLoc(AChanged.resourceLoc("wide_cross"));

    private @NotNull ResourceLocation blockLoc(@NotNull ResourceLocation loc){
        return loc.withPrefix(ModelProvider.BLOCK_FOLDER+"/");
    }

    private void blockWithItem(@NotNull DeferredBlock<?> block){
        simpleBlockWithItem(block.get(),cubeAll(block.get()));
    }

    private void crossWithItem(@NotNull DeferredBlock<?> block){
        ResourceLocation id=block.getId();
        simpleBlock(block.get(),models().cross(id.getPath(),blockLoc(id)).renderType("cutout"));
        simpleItem(id,blockLoc(id));
    }

    private void doublePartCrossWithItem(@NotNull DeferredBlock<?> block){
        ResourceLocation id=block.getId();
        ModelFile part0= wideCross(id.getPath()+"_0",blockLoc(id).withSuffix("_0"));
        ModelFile part1= wideCross(id.getPath()+"_1",blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                state.getValue(StateProperties.PART2)==0?new ConfiguredModel[]{new ConfiguredModel(part0)}:new ConfiguredModel[]{new ConfiguredModel(part1)});
        simpleItem(id,blockLoc(id).withSuffix("_1"));
    }

    private ModelFile cube(@NotNull ResourceLocation textureLoc,String modelId, String str0, String str1, String str2, String str3, String str5, String str4){
        return models().cube(textureLoc+modelId,textureLoc.withSuffix(str0),textureLoc.withSuffix(str1),textureLoc.withSuffix(str2),textureLoc.withSuffix(str3),textureLoc.withSuffix(str4),textureLoc.withSuffix(str5)).texture("particle",textureLoc.withSuffix("0c"));
    }

    private void doublePartYBlockWithItem(@NotNull DeferredBlock<?> block){
        ResourceLocation id=block.getId();
        ModelFile part0= models().getExistingFile(blockLoc(id));
        ModelFile part_1= models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                new ConfiguredModel[]{new ConfiguredModel(state.getValue(StateProperties.PART2) == 1 ? part_1 : part0)}
        );
        itemModels().getBuilder(id.getPath()).parent(part0);
    }

    private void horizontalDirectionalBlockWithItem(@NotNull DeferredBlock<? extends HorizontalDirectionalBlock> block){
        ModelFile file= models().getExistingFile(blockLoc(block.getId()));
        simpleBlockItem(block.get(),file);
        getVariantBuilder(block.get())
                .forAllStates(state-> horizontalRotatedModel(file, state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    private ConfiguredModel[] horizontalRotatedModel(@NotNull ModelFile file, @NotNull Direction direction){
        return switch (direction){
            default -> new ConfiguredModel[]{new ConfiguredModel(file)};
            case EAST -> new ConfiguredModel[]{new ConfiguredModel(file, 0, 90, false)};
            case SOUTH -> new ConfiguredModel[]{new ConfiguredModel(file, 0, 180, false)};
            case WEST -> new ConfiguredModel[]{new ConfiguredModel(file, 0, 270, false)};
        };
    }

    private void keypadWithItem(){
        ResourceLocation id=KEYPAD.getId();
        ModelFile file= models().getExistingFile(blockLoc(id));
        ModelFile file_unlocked= models().getExistingFile(blockLoc(id).withSuffix("_unlocked"));
        getVariantBuilder(KEYPAD.get()).forAllStates(state ->{
            Direction direction=state.getValue(HorizontalDirectionalBlock.FACING);
            return state.getValue(Keypad.UNLOCKED)?horizontalRotatedModel(file_unlocked,direction):horizontalRotatedModel(file,direction);
        });
        itemModels().getBuilder(id.getPath()).parent(file);
    }

    private void leavesWithItem(@NotNull DeferredBlock<?> leaves){
        ResourceLocation id=leaves.getId();
        simpleBlockWithItem(leaves.get(),models().withExistingParent(id.getPath(),"block/leaves").texture("all",blockLoc(id)));
    }

    private void logWithItem(@NotNull DeferredBlock<RotatedPillarBlock> block){
        ResourceLocation id=block.getId();
        ResourceLocation side=blockLoc(id);
        ResourceLocation end=blockLoc(id).withSuffix("_top");
        ModelFile vertical=models().cubeColumn(id.getPath(), side, end);
        ModelFile horizontal=models().cubeColumnHorizontal(id.getPath() + "_horizontal", side, end);
        getVariantBuilder(block.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        itemModels().getBuilder(id.getPath()).parent(vertical);
    }

    private void pillarWithItem(@NotNull DeferredBlock<?> block, @Nullable ResourceLocation up){
        ResourceLocation loc=blockLoc(block.getId());
        ResourceLocation top=up!=null?up:loc.withSuffix("_up");
        ModelFile file=models().cube(block.getId().getPath(),top,top,loc,loc,loc,loc).texture("particle",loc);
        simpleBlockWithItem(block.get(),file);
    }

    private void rotatedDoublePartBlockWithItem(@NotNull DeferredBlock<?> block, @Nullable String subFolder){
        ResourceLocation id=block.getId();
        if(subFolder!=null) id=id.withPrefix(subFolder+"/");
        ModelFile part0= models().getExistingFile(blockLoc(id).withSuffix("_0"));
        ModelFile part1= models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModel(state.getValue(StateProperties.PART2)==1?part1:part0,state.getValue(HorizontalDirectionalBlock.FACING))
        );
        itemModels().getBuilder(block.getId().getPath()).parent(models().getExistingFile(blockLoc(id)));
    }

    private void simpleItem(@NotNull ResourceLocation id,@NotNull ResourceLocation texture){
        itemModels().getBuilder(id.getPath()).parent(models().getExistingFile(new ResourceLocation("item/generated"))).texture("layer0",texture);
    }

    private void smallCardboardBoxPileWithItem(){
        ResourceLocation id=blockLoc(SMALL_CARDBOARD_BOX.getId().withPrefix("box_pile/"));
        ModelFile file1= models().getExistingFile(id.withSuffix("_1"));
        ModelFile file2= models().getExistingFile(id.withSuffix("_2"));
        ModelFile file3= models().getExistingFile(id.withSuffix("_3"));
        getVariantBuilder(SMALL_CARDBOARD_BOX.get())
                .forAllStates(state->{
                    Direction direction=state.getValue(HorizontalDirectionalBlock.FACING);
                    return switch (state.getValue(SmallCardboardBox.BOX_AMOUNT)){
                        default -> horizontalRotatedModel(file1,direction);
                        case 2 -> horizontalRotatedModel(file2,direction);
                        case 3 -> horizontalRotatedModel(file3,direction);
                    };
                });
        itemModels().getBuilder(SMALL_CARDBOARD_BOX.getId().getPath()).parent(file1);
    }

    private void smartSewageSystemWithItem(){
        ResourceLocation id=SMART_SEWAGE_SYSTEM.getId();
        ResourceLocation loc=blockLoc(LAB_BLOCK.getId());
        ResourceLocation top=blockLoc(id);
        String path=id.getPath();
        ModelFile file=models().cube(path,top,top,loc,loc,loc,loc).texture("particle",loc);
        getVariantBuilder(SMART_SEWAGE_SYSTEM.get()).forAllStates(state -> horizontalRotatedModel(file,state.getValue(HorizontalDirectionalBlock.FACING)));
        itemModels().getBuilder(path).parent(file);
    }

    private void tableModel(){
        ResourceLocation id=blockLoc(TABLE.getId());
        ModelFile file1= models().getExistingFile(id.withSuffix("_top"));
        ModelFile file2= models().getExistingFile(id.withSuffix("_leg"));
        getMultipartBuilder(TABLE.get()).part().modelFile(file1).addModel().end()
                .part().modelFile(file2).addModel().condition(Table.LEG_1,true).end()
                .part().modelFile(file2).rotationY(90).addModel().condition(Table.LEG_2,true).end()
                .part().modelFile(file2).rotationY(180).addModel().condition(Table.LEG_3,true).end()
                .part().modelFile(file2).rotationY(270).addModel().condition(Table.LEG_4,true).end();
    }

    private void twoByTwoDoorWithItem(@NotNull DeferredBlock<? extends AbstractTwoByTwoDoor> block){
        ResourceLocation id=blockLoc(block.getId().withPrefix(block.getId().getPath()+"/"));
        ModelFile part_0= models().getExistingFile(id.withSuffix("_0"));
        ModelFile part_0_open= models().getExistingFile(id.withSuffix("_0_open"));
        ModelFile part_1= models().getExistingFile(id.withSuffix("_1"));
        ModelFile part_1_open= models().getExistingFile(id.withSuffix("_1_open"));
        ModelFile part_2= models().getExistingFile(id.withSuffix("_2"));
        ModelFile part_2_open= models().getExistingFile(id.withSuffix("_2_open"));
        ModelFile part_3= models().getExistingFile(id.withSuffix("_3"));
        ModelFile part_3_open= models().getExistingFile(id.withSuffix("_3_open"));
        getVariantBuilder(block.get()).forAllStates(state->{
            boolean open=state.getValue(AbstractMultiDoor.OPEN);
            Direction direction=state.getValue(HorizontalDirectionalBlock.FACING);
            return switch(state.getValue(AbstractTwoByTwoDoor.PART)){
                default -> horizontalRotatedModel(open ? part_0_open : part_0,direction);
                case 1 -> horizontalRotatedModel(open?part_1_open:part_1,direction);
                case 2 -> horizontalRotatedModel(open?part_2_open:part_2,direction);
                case 3 -> horizontalRotatedModel(open?part_3_open:part_3,direction);
            };
        });
        itemModels().getBuilder(block.getId().getPath()).parent(models().getExistingFile(id));
    }

    private void ventWithItem(){
        ResourceLocation id=VENT.getId();
        ModelFile top=models().getExistingFile(blockLoc(id).withSuffix("_top"));
        ModelFile bottom=models().getExistingFile(blockLoc(id).withSuffix("_bottom"));
        ModelFile open=models().getExistingFile(blockLoc(id).withSuffix("_open"));
        trapdoorBlock(VENT.get(),bottom,top,open,true);
        itemModels().getBuilder(id.getPath()).parent(bottom);
    }

    private ModelFile wideCross(String path,ResourceLocation texture){
        return models().withExistingParent(path,WIDE_CROSS).texture("1",texture);
    }

    private void connectedTextureWithItem(@NotNull DeferredBlock<?> block, String subFolder){
        ResourceLocation textureLoc=block.getId().withPrefix("block/"+subFolder+"/");
        ConfiguredModel c0=new ConfiguredModel(models().cubeAll(textureLoc+"_c0",textureLoc.withSuffix("0c")));
        itemModels().getBuilder(block.getId().getPath()).parent(c0.model);
        ModelFile c1=cube(textureLoc,"_c1","0c","4c","1c_u","1c_u","1c_u","1c_u");
        ModelFile c2=cube(textureLoc,"_c2","4c","4c","2c_ud","2c_ud","2c_ud","2c_ud");
        ModelFile c2angle=cube(textureLoc,"_c2angle","1c_e","4c","2c_uw","2c_ue","1c_u","4c");
        ModelFile c3t1=cube(textureLoc,"_c3t1","4c","4c","3c_ued","3c_uwd","4c","2c_ud");
        ModelFile c3t2=cube(textureLoc,"_c3t2","2c_we","4c","3c_uwe","3c_uwe","4c","4c");
        ModelFile c3angle=cube(textureLoc,"_c3angle","2c_ue","4c","2c_uw","4c","2c_ue","4c");
        ModelFile c4x=cube(textureLoc,"_c4x","4c","4c","4c","4c","4c","4c");
        ModelFile c4angle=cube(textureLoc,"_c4angle","4c","4c","4c","3c_uwd","4c","3c_ued");
        ConfiguredModel middle=new ConfiguredModel(models().cubeAll(textureLoc+"_middle",textureLoc.withSuffix("4c")));
        getVariantBuilder(block.get()).forAllStates(state->{
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{c0};

            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c1)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c1,180,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c1,90,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c1,-90,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c1,90,-90,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c1,90,90,false)};

            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,0,180,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,0,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,180,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,0,-90,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,0,90,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,-90,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,180,90,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,90,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,-90,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,90,-90,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2angle,-90,90,false)};

            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2,-90,90,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c2,-90,0,false)};

            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t1)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t1,0,-90,false)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t1,0,-180,false)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t1,0,90,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t1,-90,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t1,-90,180,false)};

            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t2)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t2,-90,0,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t2,180,0,false)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t2,90,0,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t2,0,90,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3t2,180,90,false)};

            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,0,90,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,90,0,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,180,0,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,270,0,false)};

            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,180,-180,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,-90,-180,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c3angle,90,-90,false)};

            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4x)};
            if(!state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4x,90,0,false)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4x,0,90,false)};

            if(state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,90,0,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,0,false)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&!state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,-90,false)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,90,false)};
            if(state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,180,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,180,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&!state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,90,180,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,-90,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&!state.getValue(NORTH)&&state.getValue(EAST)&&state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-270,-90,false)};
            if(state.getValue(UP)&&!state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,90,false)};
            if(!state.getValue(UP)&&state.getValue(DOWN)&&state.getValue(NORTH)&&state.getValue(EAST)&&!state.getValue(SOUTH)&&state.getValue(WEST)) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-270,-270,false)};
            return new ConfiguredModel[]{middle};
        });
    }
}