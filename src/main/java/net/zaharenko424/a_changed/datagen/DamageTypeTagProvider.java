package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagProvider extends DamageTypeTagsProvider {
    public DamageTypeTagProvider(PackOutput p_270719_, CompletableFuture<HolderLookup.Provider> p_270256_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_270719_, p_270256_, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_270108_) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(DamageSources.electricity);
        tag(DamageTypeTags.NO_KNOCKBACK).add(DamageSources.electricity, DamageSources.transfur);
    }
}