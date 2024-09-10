package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class ItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> PLATES_COPPER = ItemTags.create(ResourceLocation.parse("c:plates/copper"));
    public static final TagKey<Item> PLATES_GOLD = ItemTags.create(ResourceLocation.parse("c:plates/gold"));
    public static final TagKey<Item> PLATES_IRON = ItemTags.create(ResourceLocation.parse("c:plates/iron"));
    public static final TagKey<Item> WIRES_COPPER = ItemTags.create(ResourceLocation.parse("c:wires/copper"));

    public ItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        tag(ItemTags.HEAD_ARMOR).add(HAZMAT_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(HAZMAT_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR).add(BLACK_LATEX_SHORTS.get(), HAZMAT_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(HAZMAT_BOOTS.get());
        tag(ItemTags.FENCE_GATES).add(ORANGE_FENCE_GATE_ITEM.get());
        tag(Tags.Items.GEMS).add(DARK_LATEX_CRYSTAL_SHARD.get(), GREEN_CRYSTAL_SHARD.get());
        tag(PLATES_GOLD).add(GOLDEN_PLATE.get());
        tag(ItemTags.HANGING_SIGNS).add(ORANGE_HANGING_SIGN_ITEM.get());
        tag(ItemTags.LEAVES).add(ORANGE_LEAVES_ITEM.get());
        tag(ItemTags.LOGS).add(ORANGE_TREE_LOG_ITEM.get());
        tag(ItemTags.LOGS_THAT_BURN).add(ORANGE_WOOD_ITEM.get(), STRIPPED_ORANGE_LOG_ITEM.get(), STRIPPED_ORANGE_WOOD_ITEM.get());
        tag(ItemTags.PLANKS).add(ORANGE_PLANKS_ITEM.get());
        tag(PLATES_COPPER).add(COPPER_PLATE.get());
        tag(PLATES_IRON).add(IRON_PLATE.get());
        tag(ItemTags.SAPLINGS).add(ORANGE_SAPLING_ITEM.get());
        tag(ItemTags.SIGNS).add(ORANGE_SIGN_ITEM.get());
        tag(WIRES_COPPER).add(COPPER_WIRE_ITEM.get());
        tag(ItemTags.WOODEN_BUTTONS).add(ORANGE_BUTTON_ITEM.get());
        tag(ItemTags.WOODEN_DOORS).add(ORANGE_DOOR_ITEM.get());
        tag(ItemTags.WOODEN_FENCES).add(ORANGE_FENCE_ITEM.get());
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ORANGE_PRESSURE_PLATE_ITEM.get());
        tag(ItemTags.WOODEN_SLABS).add(ORANGE_SLAB_ITEM.get());
        tag(ItemTags.WOODEN_STAIRS).add(ORANGE_STAIRS_ITEM.get());
        tag(ItemTags.WOODEN_TRAPDOORS).add(ORANGE_TRAPDOOR_ITEM.get());
    }
}