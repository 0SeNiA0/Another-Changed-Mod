package net.zaharenko424.a_changed.registry;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.cmrs.CustomBEWLR;
import net.zaharenko424.a_changed.item.ArmorMaterials;
import net.zaharenko424.a_changed.item.*;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.registry.BlockRegistry.*;
import static net.zaharenko424.a_changed.registry.FluidRegistry.*;

public class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> ABSOLUTE_SOLVER = ITEMS.register("absolute_solver", ()-> new Item(new Item.Properties()){
        @Override
        public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return CustomBEWLR.getInstance();
                }
            });
        }
    });

    public static final DeferredItem<BuildersWand> BUILDERS_WAND = ITEMS.register("builders_wand", ()-> new BuildersWand(new Item.Properties()));

    //BlockItems
    public static final DeferredItem<BlockItem> AIR_CONDITIONER_ITEM = ITEMS.registerSimpleBlockItem(AIR_CONDITIONER);
    public static final DeferredItem<BlockItem> BIG_LAB_DOOR_ITEM = ITEMS.registerSimpleBlockItem(BIG_LAB_DOOR);
    public static final DeferredItem<BlockItem> BIG_LAB_LAMP_ITEM = ITEMS.registerSimpleBlockItem(BIG_LAB_LAMP);
    public static final DeferredItem<BlockItem> BIG_LIBRARY_DOOR_ITEM = ITEMS.registerSimpleBlockItem(BIG_LIBRARY_DOOR);
    public static final DeferredItem<BlockItem> BIG_MAINTENANCE_DOOR_ITEM = ITEMS.registerSimpleBlockItem(BIG_MAINTENANCE_DOOR);
    public static final DeferredItem<BlockItem> BLUE_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BLUE_LAB_TILE);
    public static final DeferredItem<BlockItem> BLUE_LAB_TILE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(BLUE_LAB_TILE_SLAB);
    public static final DeferredItem<BlockItem> BLUE_LAB_TILE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(BLUE_LAB_TILE_STAIRS);
    public static final DeferredItem<BlockItem> BOLTED_BLUE_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BOLTED_BLUE_LAB_TILE);
    public static final DeferredItem<BlockItem> BOLTED_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BOLTED_LAB_TILE);
    public static final DeferredItem<BlockItem> BROKEN_CUP_ITEM = ITEMS.registerSimpleBlockItem(BROKEN_CUP);
    public static final DeferredItem<BlockItem> BROKEN_FLASK_ITEM = ITEMS.registerSimpleBlockItem(BROKEN_FLASK);
    public static final DeferredItem<BlockItem> BROWN_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(BROWN_LAB_BLOCK);
    public static final DeferredItem<BlockItem> CANNED_ORANGES_ITEM = ITEMS.register(CANNED_ORANGES.getId().getPath(), ()-> new BlockItem(CANNED_ORANGES.get(), new Item.Properties().durability(4)));
    public static final DeferredItem<BlockItem> CARDBOARD_BOX_ITEM = ITEMS.registerSimpleBlockItem(CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> CARPET_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CARPET_BLOCK);
    public static final DeferredItem<BlockItem> CHAIR_ITEM = ITEMS.registerSimpleBlockItem(CHAIR);
    public static final DeferredItem<BlockItem> COMPUTER_ITEM = ITEMS.registerSimpleBlockItem(COMPUTER);
    public static final DeferredItem<BlockItem> CONNECTED_BLUE_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(CONNECTED_BLUE_LAB_TILE);
    public static final DeferredItem<BlockItem> CONNECTED_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(CONNECTED_LAB_TILE);
    public static final DeferredItem<BlockItem> CRYO_CHAMBER_ITEM = ITEMS.registerSimpleBlockItem(CRYO_CHAMBER);
    public static final DeferredItem<BlockItem> CUP_ITEM = ITEMS.registerSimpleBlockItem(CUP);
    public static final DeferredItem<BlockItem> DANGER_SIGN_ITEM = ITEMS.registerSimpleBlockItem(DANGER_SIGN);
    public static final DeferredItem<BlockItem> DARK_LATEX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_BLOCK);
    public static final DeferredItem<BlockItem> DARK_LATEX_CRYSTAL_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_CRYSTAL);
    public static final DeferredItem<BlockItem> DARK_LATEX_ICE_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_CRYSTAL_ICE);
    public static final DeferredItem<BlockItem> DARK_LATEX_PUDDLE_F_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_PUDDLE_F);
    public static final DeferredItem<BlockItem> DARK_LATEX_PUDDLE_M_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_PUDDLE_M);
    public static final DeferredItem<BlockItem> EXPOSED_PIPES_ITEM = ITEMS.registerSimpleBlockItem(EXPOSED_PIPES);
    public static final DeferredItem<BlockItem> FLASK_ITEM = ITEMS.registerSimpleBlockItem(FLASK);
    public static final DeferredItem<BlockItem> GAS_TANK_ITEM = ITEMS.register("gas_tank", ()-> new GasCanisterItem(GAS_TANK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GREEN_CRYSTAL_ITEM = ITEMS.registerSimpleBlockItem(GREEN_CRYSTAL);
    public static final DeferredItem<BlockItem> HAZARD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_BLOCK);
    public static final DeferredItem<BlockItem> HAZARD_SLAB_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_SLAB);
    public static final DeferredItem<BlockItem> HAZARD_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_STAIRS);
    public static final DeferredItem<BlockItem> HAZARD_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_LAB_BLOCK);
    public static final DeferredItem<BlockItem> IV_RACK_ITEM = ITEMS.registerSimpleBlockItem(IV_RACK);
    public static final DeferredItem<BlockItem> KEYPAD_ITEM = ITEMS.registerSimpleBlockItem(KEYPAD);
    public static final DeferredItem<BlockItem> LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(LAB_BLOCK);
    public static final DeferredItem<BlockItem> LAB_SLAB_ITEM = ITEMS.registerSimpleBlockItem(LAB_SLAB);
    public static final DeferredItem<BlockItem> LAB_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(LAB_STAIRS);
    public static final DeferredItem<BlockItem> LAB_DOOR_ITEM = ITEMS.registerSimpleBlockItem(LAB_DOOR);
    public static final DeferredItem<BlockItem> LAB_LAMP_ITEM = ITEMS.registerSimpleBlockItem(LAB_LAMP);
    public static final DeferredItem<BlockItem> LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(LAB_TILE);
    public static final DeferredItem<BlockItem> LAB_TILE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(LAB_TILE_SLAB);
    public static final DeferredItem<BlockItem> LAB_TILE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(LAB_TILE_STAIRS);
    public static final DeferredItem<BlockItem> LASER_EMITTER_ITEM = ITEMS.registerSimpleBlockItem(LASER_EMITTER);
    public static final DeferredItem<BlockItem> LATEX_CONTAINER_ITEM = ITEMS.registerSimpleBlockItem(LATEX_CONTAINER);
    public static final DeferredItem<BlockItem> LATEX_RESISTANT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(LATEX_RESISTANT_BLOCK);
    public static final DeferredItem<BlockItem> LATEX_RESISTANT_GLASS_ITEM = ITEMS.registerSimpleBlockItem(LATEX_RESISTANT_GLASS);
    public static final DeferredItem<BlockItem> LATEX_RESISTANT_GLASS_PANE_ITEM = ITEMS.registerSimpleBlockItem(LATEX_RESISTANT_GLASS_PANE);
    public static final DeferredItem<BlockItem> LIBRARY_DOOR_ITEM = ITEMS.registerSimpleBlockItem(LIBRARY_DOOR);
    public static final DeferredItem<BlockItem> MAINTENANCE_DOOR_ITEM = ITEMS.registerSimpleBlockItem(MAINTENANCE_DOOR);
    public static final DeferredItem<BlockItem> METAL_BOX_ITEM = ITEMS.registerSimpleBlockItem(METAL_BOX);
    public static final DeferredItem<BlockItem> METAL_CAN_ITEM = ITEMS.registerSimpleBlockItem(METAL_CAN);
    public static final DeferredItem<BlockItem> NOTE_ITEM = ITEMS.registerSimpleBlockItem(NOTE);
    public static final DeferredItem<BlockItem> NOTEPAD_ITEM = ITEMS.registerSimpleBlockItem(NOTEPAD);
    public static final DeferredItem<BlockItem> ORANGE_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_LAB_BLOCK);
    public static final DeferredItem<BlockItem> ORANGE_LAB_SLAB_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_LAB_SLAB);
    public static final DeferredItem<BlockItem> ORANGE_LAB_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_LAB_STAIRS);
    public static final DeferredItem<BlockItem> ORANGE_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_LEAVES);
    public static final DeferredItem<BlockItem> ORANGE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_SAPLING);
    public static final DeferredItem<BlockItem> PIPE_ITEM = ITEMS.registerSimpleBlockItem(PIPE);
    public static final DeferredItem<BlockItem> ROTATING_CHAIR_ITEM = ITEMS.registerSimpleBlockItem(ROTATING_CHAIR);
    public static final DeferredItem<BlockItem> SCANNER_ITEM = ITEMS.registerSimpleBlockItem(SCANNER);
    public static final DeferredItem<BlockItem> SMALL_CARDBOARD_BOX_ITEM = ITEMS.registerSimpleBlockItem(SMALL_CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> SMART_SEWAGE_SYSTEM_ITEM = ITEMS.registerSimpleBlockItem(SMART_SEWAGE_SYSTEM);
    public static final DeferredItem<BlockItem> STRIPED_ORANGE_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(STRIPED_ORANGE_LAB_BLOCK);
    public static final DeferredItem<BlockItem> TABLE_ITEM = ITEMS.registerSimpleBlockItem(TABLE);
    public static final DeferredItem<BlockItem> TALL_CARDBOARD_BOX_ITEM = ITEMS.registerSimpleBlockItem(TALL_CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> TEST_TUBES_ITEM = ITEMS.registerSimpleBlockItem(TEST_TUBES);
    public static final DeferredItem<BlockItem> TRAFFIC_CONE_ITEM = ITEMS.register(TRAFFIC_CONE.getId().getPath(), ()-> new BlockItem(TRAFFIC_CONE.get(), new Item.Properties()){
        private static final UUID id = UUID.fromString("b4aaae90-bae7-455b-8c10-6506e8224123");
        @Override
        public boolean canEquip(@NotNull ItemStack stack, @NotNull EquipmentSlot armorType, @NotNull Entity entity) {
            return armorType == EquipmentSlot.HEAD;
        }

        @Override
        public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
            if(slot == EquipmentSlot.HEAD) return ImmutableMultimap.of(Attributes.ARMOR, new AttributeModifier(id, "armor", 2, AttributeModifier.Operation.ADDITION));
            return super.getAttributeModifiers(slot, stack);
        }
    });
    public static final DeferredItem<BlockItem> TV_SCREEN_ITEM = ITEMS.registerSimpleBlockItem(TV_SCREEN);
    public static final DeferredItem<BlockItem> VENT_DUCT_ITEM = ITEMS.registerSimpleBlockItem(VENT_DUCT);
    public static final DeferredItem<BlockItem> VENT_HATCH_ITEM = ITEMS.registerSimpleBlockItem(VENT_HATCH);
    public static final DeferredItem<BlockItem> VENT_WALL_ITEM = ITEMS.registerSimpleBlockItem(VENT_WALL);
    public static final DeferredItem<BlockItem> WHITE_LATEX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(WHITE_LATEX_BLOCK);
    public static final DeferredItem<BlockItem> WHITE_LATEX_PUDDLE_F_ITEM = ITEMS.registerSimpleBlockItem(WHITE_LATEX_PUDDLE_F);
    public static final DeferredItem<BlockItem> WHITE_LATEX_PUDDLE_M_ITEM = ITEMS.registerSimpleBlockItem(WHITE_LATEX_PUDDLE_M);
    public static final DeferredItem<BlockItem> YELLOW_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(YELLOW_LAB_BLOCK);
    public static final DeferredItem<BlockItem> YELLOW_LAB_SLAB_ITEM = ITEMS.registerSimpleBlockItem(YELLOW_LAB_SLAB);
    public static final DeferredItem<BlockItem> YELLOW_LAB_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(YELLOW_LAB_STAIRS);

    //Machine BlockItems
    public static final DeferredItem<BlockItem> CAPACITOR_ITEM = ITEMS.registerSimpleBlockItem(CAPACITOR);
    public static final DeferredItem<BlockItem> COMPRESSOR_ITEM = ITEMS.registerSimpleBlockItem(COMPRESSOR);
    public static final DeferredItem<BlockItem> COPPER_WIRE_ITEM = ITEMS.registerSimpleBlockItem(COPPER_WIRE);
    public static final DeferredItem<Wrench> COPPER_WRENCH = ITEMS.register("copper_wrench", ()-> new Wrench(AChanged.COPPER, new Item.Properties()));
    public static final DeferredItem<BlockItem> DERELICT_LATEX_ENCODER_ITEM = ITEMS.registerSimpleBlockItem(DERELICT_LATEX_ENCODER);
    public static final DeferredItem<BlockItem> DERELICT_LATEX_PURIFIER_ITEM = ITEMS.registerSimpleBlockItem(DERELICT_LATEX_PURIFIER);
    public static final DeferredItem<BlockItem> DNA_EXTRACTOR_ITEM = ITEMS.registerSimpleBlockItem(DNA_EXTRACTOR);
    public static final DeferredItem<BlockItem> GENERATOR_ITEM = ITEMS.registerSimpleBlockItem(GENERATOR);
    public static final DeferredItem<BlockItem> LATEX_ENCODER_ITEM = ITEMS.registerSimpleBlockItem(LATEX_ENCODER);
    public static final DeferredItem<BlockItem> LATEX_PURIFIER_ITEM = ITEMS.registerSimpleBlockItem(LATEX_PURIFIER);

    //Wood BlockItems
    public static final DeferredItem<BlockItem> ORANGE_BUTTON_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_BUTTON);
    public static final DeferredItem<BlockItem> ORANGE_DOOR_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_DOOR);
    public static final DeferredItem<BlockItem> ORANGE_FENCE_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_FENCE);
    public static final DeferredItem<BlockItem> ORANGE_FENCE_GATE_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_FENCE_GATE);
    public static final DeferredItem<HangingSignItem> ORANGE_HANGING_SIGN_ITEM = ITEMS.register("orange_hanging_sign", ()-> new HangingSignItem(ORANGE_HANGING_SIGN.get(), ORANGE_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<BlockItem> ORANGE_PLANKS_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_PLANKS);
    public static final DeferredItem<BlockItem> ORANGE_PRESSURE_PLATE_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_PRESSURE_PLATE);
    public static final DeferredItem<SignItem> ORANGE_SIGN_ITEM = ITEMS.register("orange_sign", ()-> new SignItem(new Item.Properties().stacksTo(16), ORANGE_SIGN.get(), ORANGE_WALL_SIGN.get()));
    public static final DeferredItem<BlockItem> ORANGE_SLAB_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_SLAB);
    public static final DeferredItem<BlockItem> ORANGE_STAIRS_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_STAIRS);
    public static final DeferredItem<BlockItem> ORANGE_TRAPDOOR_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_TRAPDOOR);
    public static final DeferredItem<BlockItem> ORANGE_TREE_LOG_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_TREE_LOG);
    public static final DeferredItem<BlockItem> ORANGE_WOOD_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_WOOD);
    public static final DeferredItem<BlockItem> STRIPPED_ORANGE_LOG_ITEM = ITEMS.registerSimpleBlockItem(STRIPPED_ORANGE_LOG);
    public static final DeferredItem<BlockItem> STRIPPED_ORANGE_WOOD_ITEM = ITEMS.registerSimpleBlockItem(STRIPPED_ORANGE_WOOD);

    //Items
    public static final DeferredItem<Item> DARK_LATEX_BASE = ITEMS.registerSimpleItem("dark_latex_base", new Item.Properties().food(new FoodProperties.Builder().fast().nutrition(1).saturationMod(1).build()).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> WHITE_LATEX_BASE = ITEMS.registerSimpleItem("white_latex_base", new Item.Properties().food(new FoodProperties.Builder().fast().nutrition(1).saturationMod(1).build()).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<ArmorItem> BLACK_LATEX_SHORTS = ITEMS.register("black_latex_shorts", ()-> new ArmorItem(ArmorMaterials.LATEX, ArmorItem.Type.LEGGINGS, new Item.Properties().setNoRepair()));
    public static final DeferredItem<BloodSyringe> BLOOD_SYRINGE = ITEMS.register("blood_syringe", BloodSyringe::new);
    public static final DeferredItem<Item> CARDBOARD = ITEMS.registerSimpleItem("cardboard");
    public static final DeferredItem<CompressedAirCanister> COMPRESSED_AIR_CANISTER = ITEMS.register("compressed_air_canister", CompressedAirCanister::new);
    public static final DeferredItem<Item> COPPER_COIL = ITEMS.registerSimpleItem("copper_coil");
    public static final DeferredItem<Item> COPPER_PLATE = ITEMS.registerSimpleItem("copper_plate");
    public static final DeferredItem<Item> DARK_LATEX_CRYSTAL_SHARD = ITEMS.registerSimpleItem("dark_latex_crystal_shard");
    public static final DeferredItem<LatexItem> DARK_LATEX_ITEM = ITEMS.register("dark_latex", ()-> new LatexItem(TransfurRegistry.DARK_LATEX_WOLF_M_TF, Latex.DARK));
    public static final DeferredItem<DNASample> DNA_SAMPLE = ITEMS.register("dna_sample", DNASample::new);
    public static final DeferredItem<Item> EMPTY_CANISTER = ITEMS.registerSimpleItem("empty_canister");
    public static final DeferredItem<Item> GOLDEN_PLATE = ITEMS.registerSimpleItem("golden_plate");
    public static final DeferredItem<Item> GREEN_CRYSTAL_SHARD = ITEMS.registerSimpleItem("green_crystal_shard");
    public static final DeferredItem<HazmatArmorItem> HAZMAT_HELMET = ITEMS.register("hazmat_helmet", ()-> new HazmatArmorItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_CHESTPLATE = ITEMS.register("hazmat_chestplate", ()-> new HazmatArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_LEGGINGS = ITEMS.register("hazmat_leggings", ()-> new HazmatArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_BOOTS = ITEMS.register("hazmat_boots", ()-> new HazmatArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<Item> IRON_PLATE = ITEMS.registerSimpleItem("iron_plate");
    public static final DeferredItem<Item> LATEX_ENCODER_COMPONENTS = ITEMS.registerSimpleItem("latex_encoder_components");
    public static final DeferredItem<LatexManipulator> LATEX_MANIPULATOR = ITEMS.register("latex_manipulator", LatexManipulator::new);
    public static final DeferredItem<Item> LATEX_PURIFIER_COMPONENTS = ITEMS.registerSimpleItem("latex_purifier_components");
    public static final DeferredItem<Item> LATEX_RESISTANT_COATING = ITEMS.registerSimpleItem("latex_resistant_coating");
    public static final DeferredItem<Item> LATEX_RESISTANT_COMPOUND = ITEMS.registerSimpleItem("latex_resistant_compound");
    public static final DeferredItem<Item> LATEX_RESISTANT_FABRIC = ITEMS.registerSimpleItem("latex_resistant_fabric");
    public static final DeferredItem<LatexSyringeItem> LATEX_SYRINGE_ITEM = ITEMS.register("latex_syringe", LatexSyringeItem::new);
    public static final DeferredItem<Item> ORANGE_ITEM = ITEMS.register("orange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(.3f).build())));
    public static final DeferredItem<OrangeJuiceItem> ORANGE_JUICE_ITEM = ITEMS.register("orange_juice", ()-> new OrangeJuiceItem(new Item.Properties()));
    public static final DeferredItem<PneumaticSyringeRifle> PNEUMATIC_SYRINGE_RIFLE = ITEMS.register("pneumatic_syringe_rifle", PneumaticSyringeRifle::new);
    public static final DeferredItem<PowerCell> POWER_CELL = ITEMS.register("power_cell", ()-> new PowerCell(new Item.Properties()));
    public static final DeferredItem<StabilizedLatexSyringeItem> STABILIZED_LATEX_SYRINGE = ITEMS.register("stabilized_latex_syringe", StabilizedLatexSyringeItem::new);
    public static final DeferredItem<StateKey> STATE_KEY = ITEMS.register("state_key", ()-> new StateKey(new Item.Properties()));
    public static final DeferredItem<StunBaton> STUN_BATON = ITEMS.register("stun_baton", StunBaton::new);
    public static final DeferredItem<SyringeItem> SYRINGE_ITEM = ITEMS.register("syringe", SyringeItem::new);
    public static final DeferredItem<SyringeCoilGun> SYRINGE_COIL_GUN = ITEMS.register("syringe_coil_gun", SyringeCoilGun::new);
    public static final DeferredItem<UnTransfurBottle> UNTRANSFUR_BOTTLE_ITEM = ITEMS.register("untransfur_bottle", UnTransfurBottle::new);
    public static final DeferredItem<Item> UNTRANSFUR_SYNTHESIZER_COMPONENTS = ITEMS.registerSimpleItem("untransfur_synthesizer_components");
    public static final DeferredItem<UnTransfurSyringeItem> UNIVERSAL_UNTRANSFUR_SYRINGE_ITEM = ITEMS.register("universal_untransfur_syringe", UnTransfurSyringeItem::new);
    public static final DeferredItem<SpecializedUnTransfurSyringe> DARK_LATEX_UNTRANSFUR_SYRINGE_ITEM = ITEMS.register("dark_latex_untransfur_syringe", ()-> new SpecializedUnTransfurSyringe(new Item.Properties(), Latex.DARK));
    public static final DeferredItem<SpecializedUnTransfurSyringe> WHITE_LATEX_UNTRANSFUR_SYRINGE_ITEM = ITEMS.register("white_latex_untransfur_syringe", ()-> new SpecializedUnTransfurSyringe(new Item.Properties(), Latex.WHITE));
    public static final DeferredItem<LatexItem> WHITE_LATEX_ITEM = ITEMS.register("white_latex", ()-> new LatexItem(TransfurRegistry.WHITE_LATEX_WOLF_M_TF,Latex.WHITE));
    public static final DeferredItem<BucketItem> LATEX_SOLVENT_BUCKET = ITEMS.register("latex_solvent_bucket", ()->new BucketItem(LATEX_SOLVENT_STILL,new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BucketItem> WHITE_LATEX_BUCKET = ITEMS.register("white_latex_bucket", ()-> new BucketItem(WHITE_LATEX_STILL,new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BucketItem> DARK_LATEX_BUCKET = ITEMS.register("dark_latex_bucket", ()-> new BucketItem(DARK_LATEX_STILL,new Item.Properties().stacksTo(1)));

    //Spawn eggs//TODO add latex goop egg
    public static final DeferredItem<SpawnEggItem> BEI_FENG_EGG = ITEMS.register("bei_feng_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.BEI_FENG, 5334429, 2763306, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> BENIGN_EGG = ITEMS.register("benign_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.BENIGN, 2171169, 2171169, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> DARK_LATEX_WOLF_F_EGG = ITEMS.register("dark_latex_wolf_female_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.DARK_LATEX_WOLF_FEMALE, 2763306, 6908265, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> DARK_LATEX_WOLF_M_EGG = ITEMS.register("dark_latex_wolf_male_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.DARK_LATEX_WOLF_MALE, 2763306, 6908265, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> GAS_WOLF_EGG = ITEMS.register("gas_wolf_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.GAS_WOLF, 2763306, 16777215, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> HYPNO_CAT_EGG = ITEMS.register("hypno_cat_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.HYPNO_CAT, 2763306, -344280, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> LATEX_SHARK_F_EGG = ITEMS.register("latex_shark_female_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.LATEX_SHARK_FEMALE, -6908266, -1, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> LATEX_SHARK_M_EGG = ITEMS.register("latex_shark_male_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.LATEX_SHARK_MALE, -6908266, -2302756, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> MILK_PUDDING = ITEMS.register("milk_pudding_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.MILK_PUDDING, -328966, -328966, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> PURE_WHITE_LATEX_WOLF_EGG = ITEMS.register("pure_white_latex_wolf_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.PURE_WHITE_LATEX_WOLF, 16777215, 16777215, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> ROOMBA_SPAWN_EGG = ITEMS.register("roomba_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.ROOMBA_ENTITY, -1973791, -37291, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> SNOW_LEOPARD_F_EGG = ITEMS.register("snow_leopard_female_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.SNOW_LEOPARD_FEMALE, -6513508, -263173, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> SNOW_LEOPARD_M_EGG = ITEMS.register("snow_leopard_male_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.SNOW_LEOPARD_MALE, -6513508, -263173, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> WHITE_LATEX_WOLF_F_EGG = ITEMS.register("white_latex_wolf_female_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.WHITE_LATEX_WOLF_FEMALE, 16777215, 13619151, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> WHITE_LATEX_WOLF_M_EGG = ITEMS.register("white_latex_wolf_male_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.WHITE_LATEX_WOLF_MALE, 16777215, 13619151, spawnEgg()));
    public static final DeferredItem<SpawnEggItem> YUFENG_DRAGON_EGG = ITEMS.register("yufeng_dragon_spawn_egg", ()-> new DeferredSpawnEggItem(EntityRegistry.YUFENG_DRAGON, -13686230, -14408668, spawnEgg()));

    private static Item.@NotNull Properties spawnEgg(){
        return new Item.Properties().rarity(Rarity.UNCOMMON);
    }
}