package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        tag(Tags.Items.ARMORS_HELMETS).add(HAZMAT_HELMET.get());
        tag(Tags.Items.ARMORS_CHESTPLATES).add(HAZMAT_CHESTPLATE.get());
        tag(Tags.Items.ARMORS_LEGGINGS).add(BLACK_LATEX_SHORTS.get(), HAZMAT_LEGGINGS.get());
        tag(Tags.Items.ARMORS_BOOTS).add(HAZMAT_BOOTS.get());
        tag(Tags.Items.GEMS).add(DARK_LATEX_CRYSTAL_SHARD.get());
        tag(ItemTags.SAPLINGS).add(ORANGE_SAPLING_ITEM.get());
    }
}