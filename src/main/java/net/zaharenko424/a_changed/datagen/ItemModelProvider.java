package net.zaharenko424.a_changed.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.registry.ItemRegistry.*;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AChanged.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(BLACK_LATEX_SHORTS.get());
        basicItem(CARDBOARD.getId());
        basicItem(DARK_LATEX_BASE.getId());
        basicItem(DARK_LATEX_BUCKET.getId());
        basicItem(DARK_LATEX_CRYSTAL_SHARD.getId());
        basicItem(DARK_LATEX_ITEM.getId());
        basicItem(DNA_SAMPLE.getId());
        basicItem(GREEN_CRYSTAL_SHARD.getId());
        basicItem(HAZMAT_HELMET.getId());
        basicItem(HAZMAT_CHESTPLATE.getId());
        basicItem(HAZMAT_LEGGINGS.getId());
        basicItem(HAZMAT_BOOTS.getId());
        basicItem(LATEX_SOLVENT_BUCKET.getId());
        basicItem(LATEX_SYRINGE_ITEM.getId());

        ResourceLocation planks = BlockRegistry.ORANGE_PLANKS.getId().withPrefix(ModelProvider.BLOCK_FOLDER+"/");
        buttonInventory(ORANGE_BUTTON_ITEM.getId().getPath(), planks);
        basicItem(ORANGE_DOOR_ITEM.getId());
        fenceInventory(ORANGE_FENCE_ITEM.getId().getPath(), planks);
        fenceGate(ORANGE_FENCE_GATE_ITEM.getId().getPath(), planks);
        basicItem(ORANGE_HANGING_SIGN_ITEM.getId());
        basicItem(ORANGE_ITEM.getId());
        pressurePlate(ORANGE_PRESSURE_PLATE_ITEM.getId().getPath(), planks);
        basicItem(ORANGE_SIGN_ITEM.getId());
        slab(ORANGE_SLAB_ITEM.getId().getPath(), planks, planks, planks);
        stairs(ORANGE_STAIRS_ITEM.getId().getPath(), planks, planks, planks);
        trapdoorBottom(ORANGE_TRAPDOOR_ITEM.getId().getPath(), planks);

        basicItem(ORANGE_JUICE_ITEM.getId());
        basicItem(POWER_CELL.getId());
        basicItem(SYRINGE_ITEM.getId());
        basicItem(UNTRANSFUR_BOTTLE_ITEM.getId());
        basicItem(UNTRANSFUR_SYRINGE_ITEM.getId());
        basicItem(WHITE_LATEX_BASE.getId());
        basicItem(WHITE_LATEX_BUCKET.getId());
        basicItem(WHITE_LATEX_ITEM.getId());

        spawnEgg(BEI_FENG_EGG);
        spawnEgg(BENIGN_EGG);
        spawnEgg(DARK_LATEX_WOLF_F_EGG);
        spawnEgg(DARK_LATEX_WOLF_M_EGG);
        spawnEgg(GAS_WOLF_EGG);
        spawnEgg(PURE_WHITE_LATEX_WOLF_EGG);
        spawnEgg(WHITE_LATEX_WOLF_F_EGG);
        spawnEgg(WHITE_LATEX_WOLF_M_EGG);
    }

    protected void spawnEgg(@NotNull DeferredItem<SpawnEggItem> egg){
        withExistingParent(egg.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}