package net.zaharenko424.a_changed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.worldgen.Biomes;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class BiomeTagProvider extends BiomeTagsProvider {
    public BiomeTagProvider(PackOutput p_255800_, CompletableFuture<HolderLookup.Provider> p_256205_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_255800_, p_256205_, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256485_) {
        tag(BiomeTags.IS_OVERWORLD).add(Biomes.DARK_LATEX_BIOME, Biomes.WHITE_LATEX_BIOME);
    }
}