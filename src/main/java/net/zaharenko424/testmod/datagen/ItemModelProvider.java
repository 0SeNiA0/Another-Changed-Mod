package net.zaharenko424.testmod.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.testmod.TestMod;

import static net.zaharenko424.testmod.registry.ItemRegistry.*;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TestMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(DARK_LATEX_ITEM.getId());
        basicItem(DARK_LATEX_BUCKET.getId());
        basicItem(LATEX_SOLVENT_BUCKET.getId());
        basicItem(LATEX_SYRINGE_ITEM.getId());
        basicItem(ORANGE_ITEM.getId());
        basicItem(ORANGE_JUICE_ITEM.getId());
        basicItem(SYRINGE_ITEM.getId());
        basicItem(UNTRANSFUR_BOTTLE_ITEM.getId());
        basicItem(UNTRANSFUR_SYRINGE_ITEM.getId());
        basicItem(WHITE_LATEX_ITEM.getId());
        basicItem(WHITE_LATEX_BUCKET.getId());
    }
}