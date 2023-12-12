package net.zaharenko424.testmod.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.item.*;

import static net.zaharenko424.testmod.TestMod.MODID;
import static net.zaharenko424.testmod.registry.BlockRegistry.*;
import static net.zaharenko424.testmod.registry.FluidRegistry.*;

public final class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    //public static final DeferredItem<BlockItem> PIPE_ITEM = ITEMS.registerSimpleBlockItem(PIPE);
    //BlockItems
    public static final DeferredItem<BlockItem> AIR_CONDITIONER_ITEM = ITEMS.registerSimpleBlockItem(AIR_CONDITIONER);
    public static final DeferredItem<BlockItem> BLUE_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BLUE_LAB_TILE);
    public static final DeferredItem<BlockItem> BOLTED_BLUE_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BOLTED_BLUE_LAB_TILE);
    public static final DeferredItem<BlockItem> BOLTED_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BOLTED_LAB_TILE);
    public static final DeferredItem<BlockItem> BROWN_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(BROWN_LAB_BLOCK);
    public static final DeferredItem<BlockItem> CARDBOARD_BOX_ITEM = ITEMS.registerSimpleBlockItem(CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> CARPET_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CARPET_BLOCK);
    public static final DeferredItem<BlockItem> CHAIR_ITEM = ITEMS.registerSimpleBlockItem(CHAIR);
    public static final DeferredItem<BlockItem> CONNECTED_BLUE_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(CONNECTED_BLUE_LAB_TILE);
    public static final DeferredItem<BlockItem> CONNECTED_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(CONNECTED_LAB_TILE);
    public static final DeferredItem<BlockItem> DARK_LATEX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_BLOCK);
    public static final DeferredItem<BlockItem> HAZARD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_BLOCK);
    public static final DeferredItem<BlockItem> HAZARD_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_LAB_BLOCK);
    public static final DeferredItem<BlockItem> KEYPAD_ITEM = ITEMS.registerSimpleBlockItem(KEYPAD);
    public static final DeferredItem<BlockItem> LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(LAB_BLOCK);
    public static final DeferredItem<BlockItem> LAB_DOOR_ITEM = ITEMS.registerSimpleBlockItem(LAB_DOOR);
    public static final DeferredItem<BlockItem> LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(LAB_TILE);
    public static final DeferredItem<BlockItem> LATEX_CONTAINER_ITEM = ITEMS.registerSimpleBlockItem(LATEX_CONTAINER);
    public static final DeferredItem<BlockItem> LIBRARY_DOOR_ITEM = ITEMS.registerSimpleBlockItem(LIBRARY_DOOR);
    public static final DeferredItem<BlockItem> MAINTENANCE_DOOR_ITEM = ITEMS.registerSimpleBlockItem(MAINTENANCE_DOOR);
    public static final DeferredItem<BlockItem> METAL_BOX_ITEM = ITEMS.registerSimpleBlockItem(METAL_BOX);
    public static final DeferredItem<BlockItem> NOTE_ITEM = ITEMS.registerSimpleBlockItem(NOTE);
    public static final DeferredItem<BlockItem> NOTEPAD_ITEM = ITEMS.registerSimpleBlockItem(NOTEPAD);
    public static final DeferredItem<BlockItem> ORANGE_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_LAB_BLOCK);
    public static final DeferredItem<BlockItem> ORANGE_LEAVES_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_LEAVES);
    public static final DeferredItem<BlockItem> ORANGE_SAPLING_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_SAPLING);
    public static final DeferredItem<BlockItem> ORANGE_TREE_LOG_ITEM = ITEMS.registerSimpleBlockItem(ORANGE_TREE_LOG);
    public static final DeferredItem<BlockItem> SCANNER_ITEM = ITEMS.registerSimpleBlockItem(SCANNER);
    public static final DeferredItem<BlockItem> SMALL_CARDBOARD_BOX_ITEM = ITEMS.registerSimpleBlockItem(SMALL_CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> SMART_SEWAGE_SYSTEM_ITEM = ITEMS.registerSimpleBlockItem(SMART_SEWAGE_SYSTEM);
    public static final DeferredItem<BlockItem> STRIPED_ORANGE_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(STRIPED_ORANGE_LAB_BLOCK);
    public static final DeferredItem<BlockItem> TABLE_ITEM = ITEMS.registerSimpleBlockItem(TABLE);
    public static final DeferredItem<BlockItem> TALL_CARDBOARD_BOX_ITEM = ITEMS.registerSimpleBlockItem(TALL_CARDBOARD_BOX);
    public static final DeferredItem<BlockItem> TRAFFIC_CONE_ITEM = ITEMS.registerSimpleBlockItem(TRAFFIC_CONE);
    public static final DeferredItem<BlockItem> VENT_ITEM = ITEMS.registerSimpleBlockItem(VENT);
    public static final DeferredItem<BlockItem> VENT_WALL_ITEM = ITEMS.registerSimpleBlockItem(VENT_WALL);
    public static final DeferredItem<BlockItem> WHITE_LATEX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(WHITE_LATEX_BLOCK);
    public static final DeferredItem<BlockItem> YELLOW_LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(YELLOW_LAB_BLOCK);

    //Items
    public static final DeferredItem<Item> CARDBOARD = ITEMS.registerSimpleItem("cardboard");
    public static final DeferredItem<LatexItem> DARK_LATEX_ITEM = ITEMS.register("dark_latex", ()-> new LatexItem(TransfurRegistry.DARK_LATEX_WOLF_M_TF));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_HELMET = ITEMS.register("hazmat_helmet", ()-> new HazmatArmorItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_CHESTPLATE = ITEMS.register("hazmat_chestplate", ()-> new HazmatArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_LEGGINGS = ITEMS.register("hazmat_leggings", ()-> new HazmatArmorItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredItem<HazmatArmorItem> HAZMAT_BOOTS = ITEMS.register("hazmat_boots", ()-> new HazmatArmorItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<LatexSyringeItem> LATEX_SYRINGE_ITEM=ITEMS.register("latex_syringe", LatexSyringeItem::new);
    public static final DeferredItem<Item> ORANGE_ITEM = ITEMS.register("orange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(2f).build())));
    public static final DeferredItem<OrangeJuiceItem> ORANGE_JUICE_ITEM = ITEMS.register("orange_juice", ()-> new OrangeJuiceItem(new Item.Properties()));
    public static final DeferredItem<SyringeItem> SYRINGE_ITEM=ITEMS.register("syringe", SyringeItem::new);
    public static final DeferredItem<UnTransfurBottle> UNTRANSFUR_BOTTLE_ITEM=ITEMS.register("untransfur_bottle", UnTransfurBottle::new);
    public static final DeferredItem<UnTransfurSyringeItem> UNTRANSFUR_SYRINGE_ITEM=ITEMS.register("untransfur_syringe", UnTransfurSyringeItem::new);
    public static final DeferredItem<LatexItem> WHITE_LATEX_ITEM = ITEMS.register("white_latex", ()-> new LatexItem(TransfurRegistry.WHITE_LATEX_WOLF_M_TF));

    public static final DeferredItem<BucketItem> LATEX_SOLVENT_BUCKET = ITEMS.register("latex_solvent_bucket", ()->new BucketItem(LATEX_SOLVENT_STILL,new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BucketItem> WHITE_LATEX_BUCKET = ITEMS.register("white_latex_bucket", ()-> new BucketItem(WHITE_LATEX_STILL,new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BucketItem> DARK_LATEX_BUCKET = ITEMS.register("dark_latex_bucket", ()-> new BucketItem(DARK_LATEX_STILL,new Item.Properties().stacksTo(1)));
}