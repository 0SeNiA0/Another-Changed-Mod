package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagProvider extends DamageTypeTagsProvider {
    public DamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookup, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider lookup) {
        tag(DamageTypeTags.NO_KNOCKBACK).add(DamageSources.assimilation, DamageSources.electricity, DamageSources.solvent,
                DamageSources.syringe, DamageSources.transfur, DamageSources.transfurKill, DamageSources.untransfur,
                DamageSources.untransfurKill);
        tag(Tags.DamageTypes.NO_FLINCH).add(DamageSources.transfur, DamageSources.transfurKill, DamageSources.untransfur,
                DamageSources.untransfurKill);

        tag(DamageTypeTags.BYPASSES_ARMOR).add(DamageSources.assimilation, DamageSources.electricity, DamageSources.syringe,
                DamageSources.transfurKill, DamageSources.untransfurKill);
        tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(DamageSources.assimilation, DamageSources.transfurKill, DamageSources.untransfurKill);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(DamageSources.assimilation, DamageSources.transfurKill, DamageSources.untransfurKill);
        tag(Tags.DamageTypes.IS_TECHNICAL).add(DamageSources.assimilation, DamageSources.transfurKill, DamageSources.untransfurKill);
    }
}