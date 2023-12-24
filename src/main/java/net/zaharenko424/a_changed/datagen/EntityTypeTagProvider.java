package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends EntityTypeTagsProvider {
    public EntityTypeTagProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, p_256572_, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_255894_) {
        tag(AChanged.SEWAGE_SYSTEM_CONSUMABLE).add(EntityType.SLIME);
        tag(AChanged.TRANSFURRABLE_TAG).add(EntityType.DROWNED,EntityType.PLAYER,EntityType.HUSK,EntityType.VILLAGER
                        ,EntityType.ZOMBIE,EntityType.ZOMBIE_VILLAGER,EntityType.WANDERING_TRADER)
                .addTags(EntityTypeTags.RAIDERS,EntityTypeTags.SKELETONS);
    }
}