package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.a_changed.registry.BlockRegistry.*;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        tag(BlockTags.CEILING_HANGING_SIGNS).add(ORANGE_HANGING_SIGN.get());
        tag(BlockTags.FENCE_GATES).add(ORANGE_FENCE_GATE.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(CARDBOARD_BOX.get(),ORANGE_TREE_LOG.get(),SMALL_CARDBOARD_BOX.get()
                ,TALL_CARDBOARD_BOX.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AIR_CONDITIONER.get(), BIG_LAB_DOOR.get(), BIG_LIBRARY_DOOR.get(),
                BIG_MAINTENANCE_DOOR.get(), BLUE_LAB_TILE.get(), BOLTED_BLUE_LAB_TILE.get(), BOLTED_LAB_TILE.get(),
                BROWN_LAB_BLOCK.get(), CAPACITOR.get(), CHAIR.get(), COMPRESSOR.get(), COMPUTER.get(), CONNECTED_BLUE_LAB_TILE.get(),
                CONNECTED_LAB_TILE.get(), CRYO_CHAMBER.get(), DARK_LATEX_CRYSTAL.get(), DARK_LATEX_CRYSTAL_ICE.get(),
                DNA_EXTRACTOR.get(), GAS_TANK.get(), GENERATOR.get(), GREEN_CRYSTAL.get(), HAZARD_BLOCK.get(), HAZARD_LAB_BLOCK.get(),
                IV_RACK.get(), KEYPAD.get(), LAB_BLOCK.get(), LAB_DOOR.get(), LAB_TILE.get(), LASER_EMITTER.get(),
                LATEX_CONTAINER.get(), LATEX_ENCODER.get(), LATEX_PURIFIER.get(), LIBRARY_DOOR.get(), MAINTENANCE_DOOR.get(),
                METAL_BOX.get(), ORANGE_LAB_BLOCK.get(), PIPE.get(), SCANNER.get(), SMART_SEWAGE_SYSTEM.get(),
                STRIPED_ORANGE_LAB_BLOCK.get(), TABLE.get(), VENT.get(), VENT_WALL.get(), YELLOW_LAB_BLOCK.get());
        tag(AChanged.LASER_TRANSPARENT).addTags(BlockTags.REPLACEABLE, Tags.Blocks.GLASS, Tags.Blocks.GLASS_PANES, BlockTags.BUTTONS)
                .add(DANGER_SIGN.get(), WHITE_LATEX_PUDDLE_F.get(), WHITE_LATEX_PUDDLE_M.get());
        tag(BlockTags.LEAVES).add(ORANGE_LEAVES.get());
        tag(BlockTags.LOGS).add(ORANGE_TREE_LOG.get());
        tag(BlockTags.LOGS_THAT_BURN).add(ORANGE_WOOD.get(), STRIPPED_ORANGE_LOG.get(), STRIPPED_ORANGE_WOOD.get());
        tag(BlockTags.PLANKS).add(ORANGE_PLANKS.get());
        tag(BlockTags.SAPLINGS).add(ORANGE_SAPLING.get());
        tag(BlockTags.STANDING_SIGNS).add(ORANGE_SIGN.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(ORANGE_WALL_HANGING_SIGN.get());
        tag(BlockTags.WOODEN_BUTTONS).add(ORANGE_BUTTON.get());
        tag(BlockTags.WOODEN_DOORS).add(ORANGE_DOOR.get());
        tag(BlockTags.WOODEN_FENCES).add(ORANGE_FENCE.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ORANGE_PRESSURE_PLATE.get());
        tag(BlockTags.WOODEN_SLABS).add(ORANGE_SLAB.get());
        tag(BlockTags.WOODEN_STAIRS).add(ORANGE_STAIRS.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ORANGE_TRAPDOOR.get());
    }
}