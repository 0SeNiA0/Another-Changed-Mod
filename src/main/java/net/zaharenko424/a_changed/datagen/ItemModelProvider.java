package net.zaharenko424.a_changed.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
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
        basicItem(BUILDERS_WAND.getId());

        basicItem(BLACK_LATEX_SHORTS.getId());
        basicItem(BLOOD_SYRINGE.getId());
        basicItem(CARDBOARD.getId());
        basicItem(COMPRESSED_AIR_CANISTER.getId());
        basicItem(COPPER_COIL.getId());
        basicItem(COPPER_PLATE.getId());
        basicItem(DARK_LATEX_BASE.getId());
        basicItem(DARK_LATEX_BUCKET.getId());
        basicItem(DARK_LATEX_CRYSTAL_SHARD.getId());
        basicItem(DARK_LATEX_ITEM.getId());
        basicItem(DNA_SAMPLE.getId());
        basicItem(EMPTY_CANISTER.getId());
        basicItem(GOLDEN_PLATE.getId());
        basicItem(GREEN_CRYSTAL_SHARD.getId());
        basicItem(HAZMAT_HELMET.getId());
        basicItem(HAZMAT_CHESTPLATE.getId());
        basicItem(HAZMAT_LEGGINGS.getId());
        basicItem(HAZMAT_BOOTS.getId());
        basicItem(IRON_PLATE.getId());
        basicItem(LATEX_ENCODER_COMPONENTS.getId());
        basicItem(LATEX_MANIPULATOR.getId());
        basicItem(LATEX_PURIFIER_COMPONENTS.getId());
        basicItem(LATEX_RESISTANT_COATING.getId());
        basicItem(LATEX_RESISTANT_COMPOUND.getId());
        basicItem(LATEX_RESISTANT_FABRIC.getId());
        basicItemBlockTexture(LATEX_RESISTANT_GLASS_PANE_ITEM, BlockRegistry.LATEX_RESISTANT_GLASS).renderType("translucent");
        basicItem(LATEX_SOLVENT_BUCKET.getId());
        basicItem(LATEX_SYRINGE_ITEM.getId());

        ResourceLocation planks = blockLoc(BlockRegistry.ORANGE_PLANKS);
        buttonInventory(ORANGE_BUTTON_ITEM.getId().getPath(), planks);
        basicItem(ORANGE_DOOR_ITEM.getId());
        fenceInventory(ORANGE_FENCE_ITEM.getId().getPath(), planks);
        fenceGate(ORANGE_FENCE_GATE_ITEM.getId().getPath(), planks);
        basicItem(ORANGE_HANGING_SIGN_ITEM.getId());
        basicItem(ORANGE_ITEM.getId());
        pressurePlate(ORANGE_PRESSURE_PLATE_ITEM.getId().getPath(), planks);
        basicItem(ORANGE_SIGN_ITEM.getId());
        trapdoorBottom(ORANGE_TRAPDOOR_ITEM.getId().getPath(), planks);

        basicItem(ORANGE_JUICE_ITEM.getId());
        basicItem(POWER_CELL.getId());
        basicItem(STATE_KEY.getId());
        basicItem(SYRINGE_ITEM.getId());
        basicItem(UNTRANSFUR_BOTTLE_ITEM.getId());
        basicItem(UNTRANSFUR_SYNTHESIZER_COMPONENTS.getId());
        basicItem(UNIVERSAL_UNTRANSFUR_SYRINGE_ITEM.getId());
        basicItem(DARK_LATEX_UNTRANSFUR_SYRINGE_ITEM.getId());
        basicItem(WHITE_LATEX_UNTRANSFUR_SYRINGE_ITEM.getId());
        basicItem(WHITE_LATEX_BASE.getId());
        basicItem(WHITE_LATEX_BUCKET.getId());
        basicItem(WHITE_LATEX_ITEM.getId());
        basicItem(COPPER_WRENCH.getId());

        spawnEgg(BEI_FENG_EGG);
        spawnEgg(BENIGN_EGG);
        spawnEgg(DARK_LATEX_WOLF_F_EGG);
        spawnEgg(DARK_LATEX_WOLF_M_EGG);
        spawnEgg(GAS_WOLF_EGG);
        spawnEgg(HYPNO_CAT_EGG);
        spawnEgg(LATEX_SHARK_F_EGG);
        spawnEgg(LATEX_SHARK_M_EGG);
        spawnEgg(PURE_WHITE_LATEX_WOLF_EGG);
        spawnEgg(SNOW_LEOPARD_F_EGG);
        spawnEgg(SNOW_LEOPARD_M_EGG);
        spawnEgg(WHITE_LATEX_WOLF_F_EGG);
        spawnEgg(WHITE_LATEX_WOLF_M_EGG);
    }

    private @NotNull ResourceLocation blockLoc(@NotNull DeferredBlock<?> block){
        return block.getId().withPrefix(BLOCK_FOLDER + "/");
    }

    private @NotNull ItemModelBuilder basicItemBlockTexture(@NotNull DeferredItem<?> item, @NotNull DeferredBlock<?> block){
        return getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", blockLoc(block));
    }

    protected void spawnEgg(@NotNull DeferredItem<SpawnEggItem> egg){
        withExistingParent(egg.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}