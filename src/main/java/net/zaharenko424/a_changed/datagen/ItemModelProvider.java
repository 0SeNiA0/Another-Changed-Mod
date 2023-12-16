package net.zaharenko424.a_changed.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.a_changed.AChanged;

import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(CARDBOARD.getId());
        basicItem(DARK_LATEX_ITEM.getId());
        basicItem(DARK_LATEX_BUCKET.getId());
        basicItem(HAZMAT_HELMET.getId());
        basicItem(HAZMAT_CHESTPLATE.getId());
        basicItem(HAZMAT_LEGGINGS.getId());
        basicItem(HAZMAT_BOOTS.getId());
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