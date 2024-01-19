package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.item.LatexSyringeItem;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.registry.ItemRegistry.*;
import static net.zaharenko424.a_changed.registry.TransfurRegistry.TRANSFUR_REGISTRY;

public class CreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID);

    static {
        CREATIVE_MODE_TABS.register("item_tab", () -> CreativeModeTab.builder()
                .icon(() -> LAB_BLOCK_ITEM.get().getDefaultInstance())
                .title(Component.translatable("itemGroup.a_changed.main"))
                .displayItems((parameters, output) -> {
                    output.accept(ORANGE_ITEM);
                    output.accept(ORANGE_JUICE_ITEM);
                    output.accept(ORANGE_LEAVES_ITEM);
                    output.accept(ORANGE_SAPLING_ITEM);
                    output.accept(ORANGE_TREE_LOG_ITEM);
                    output.accept(WHITE_LATEX_ITEM);
                    output.accept(DARK_LATEX_ITEM);
                    output.accept(WHITE_LATEX_BLOCK_ITEM);
                    output.accept(DARK_LATEX_BLOCK_ITEM);
                    output.accept(HAZARD_BLOCK_ITEM);
                    output.accept(HAZARD_LAB_BLOCK_ITEM);
                    output.accept(BLUE_LAB_TILE_ITEM);
                    output.accept(BOLTED_BLUE_LAB_TILE_ITEM);
                    output.accept(BOLTED_LAB_TILE_ITEM);
                    output.accept(BROWN_LAB_BLOCK_ITEM);
                    output.accept(CARPET_BLOCK_ITEM);
                    output.accept(CONNECTED_BLUE_LAB_TILE_ITEM);
                    output.accept(CONNECTED_LAB_TILE_ITEM);
                    output.accept(LAB_BLOCK_ITEM);
                    output.accept(LAB_TILE_ITEM);
                    output.accept(ORANGE_LAB_BLOCK_ITEM);
                    output.accept(SMART_SEWAGE_SYSTEM_ITEM);
                    output.accept(STRIPED_ORANGE_LAB_BLOCK_ITEM);
                    output.accept(VENT_WALL_ITEM);
                    output.accept(YELLOW_LAB_BLOCK_ITEM);

                    output.accept(AIR_CONDITIONER_ITEM);
                    output.accept(BROKEN_CUP_ITEM);
                    output.accept(BROKEN_FLASK_ITEM);
                    output.accept(CARDBOARD_BOX_ITEM);
                    output.accept(CHAIR_ITEM);
                    output.accept(COMPUTER_ITEM);
                    output.accept(CRYO_CHAMBER_ITEM);
                    output.accept(CUP_ITEM);
                    output.accept(DANGER_SIGN_ITEM);
                    output.accept(DARK_LATEX_ICE_ITEM);
                    output.accept(DARK_LATEX_CRYSTAL_ITEM);
                    output.accept(DARK_LATEX_CRYSTAL_SHARD);
                    output.accept(FLASK_ITEM);
                    output.accept(GREEN_CRYSTAL_ITEM);
                    output.accept(GREEN_CRYSTAL_SHARD);
                    output.accept(GAS_TANK_ITEM);
                    output.accept(IV_RACK_ITEM);
                    output.accept(KEYPAD_ITEM);
                    output.accept(LAB_DOOR_ITEM);
                    output.accept(BIG_LAB_DOOR_ITEM);
                    output.accept(LASER_EMITTER_ITEM);
                    output.accept(LATEX_CONTAINER_ITEM);
                    output.accept(LIBRARY_DOOR_ITEM);
                    output.accept(BIG_LIBRARY_DOOR_ITEM);
                    output.accept(MAINTENANCE_DOOR_ITEM);
                    output.accept(BIG_MAINTENANCE_DOOR_ITEM);
                    output.accept(METAL_BOX_ITEM);
                    output.accept(NOTE_ITEM);
                    output.accept(NOTEPAD_ITEM);
                    output.accept(PIPE_ITEM);
                    output.accept(SCANNER_ITEM);
                    output.accept(SMALL_CARDBOARD_BOX_ITEM);
                    output.accept(TABLE_ITEM);
                    output.accept(TALL_CARDBOARD_BOX_ITEM);
                    output.accept(TEST_TUBES_ITEM);
                    output.accept(TRAFFIC_CONE_ITEM);
                    output.accept(WHITE_LATEX_PUDDLE_F_ITEM);
                    output.accept(WHITE_LATEX_PUDDLE_M_ITEM);
                    output.accept(VENT_ITEM);

                    output.accept(LATEX_SOLVENT_BUCKET);
                    output.accept(WHITE_LATEX_BUCKET);
                    output.accept(DARK_LATEX_BUCKET);
                }).build());

        CREATIVE_MODE_TABS.register("transfurs", ()-> CreativeModeTab.builder()
                .icon(() -> SYRINGE_ITEM.get().getDefaultInstance())
                .title(Component.translatable("itemGroup.a_changed.transfurs"))
                .displayItems((parameters, output) -> {
                    output.accept(HAZMAT_HELMET);
                    output.accept(HAZMAT_CHESTPLATE);
                    output.accept(HAZMAT_LEGGINGS);
                    output.accept(HAZMAT_BOOTS);

                    output.accept(BLACK_LATEX_SHORTS);

                    output.accept(SYRINGE_ITEM);
                    output.accept(UNTRANSFUR_SYRINGE_ITEM);
                    output.accept(UNTRANSFUR_BOTTLE_ITEM);

                    TRANSFUR_REGISTRY.stream().forEach((tf)->output.accept(LatexSyringeItem.encodeTransfur(tf.id)));

                    output.accept(BEI_FENG_EGG);
                    output.accept(BENIGN_EGG);
                    output.accept(DARK_LATEX_WOLF_F_EGG);
                    output.accept(DARK_LATEX_WOLF_M_EGG);
                    output.accept(GAS_WOLF_EGG);
                    output.accept(PURE_WHITE_LATEX_WOLF_EGG);
                    output.accept(WHITE_LATEX_WOLF_F_EGG);
                    output.accept(WHITE_LATEX_WOLF_M_EGG);
                }).build());
    }
}