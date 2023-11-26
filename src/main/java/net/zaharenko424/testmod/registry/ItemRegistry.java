package net.zaharenko424.testmod.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.item.*;

import static net.zaharenko424.testmod.TestMod.*;
import static net.zaharenko424.testmod.registry.BlockRegistry.*;
import static net.zaharenko424.testmod.registry.FluidRegistry.*;
import static net.zaharenko424.testmod.registry.EntityRegistry.WHITE_LATEX_WOLF_MALE;

public final class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    //BlockItems
    public static final DeferredItem<BlockItem> BOLTED_LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(BOLTED_LAB_TILE);
    public static final DeferredItem<BlockItem> CARPET_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CARPET_BLOCK);
    public static final DeferredItem<BlockItem> DARK_LATEX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(DARK_LATEX_BLOCK);
    public static final DeferredItem<BlockItem> HAZARD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(HAZARD_BLOCK);
    public static final DeferredItem<BlockItem> LAB_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(LAB_BLOCK);
    public static final DeferredItem<BlockItem> LAB_TILE_ITEM = ITEMS.registerSimpleBlockItem(LAB_TILE);
    public static final DeferredItem<BlockItem> WHITE_LATEX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(WHITE_LATEX_BLOCK);

    //Items
    public static final DeferredItem<LatexItem> DARK_LATEX_ITEM = ITEMS.register("dark_latex", ()-> new LatexItem(resourceLocation("dark_latex_wolf")));
    public static final DeferredItem<LatexSyringeItem> LATEX_SYRINGE_ITEM=ITEMS.register("latex_syringe", LatexSyringeItem::new);
    public static final DeferredItem<Item> ORANGE_ITEM = ITEMS.register("orange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(2f).build())));
    public static final DeferredItem<OrangeJuiceItem> ORANGE_JUICE_ITEM = ITEMS.register("orange_juice", ()-> new OrangeJuiceItem(new Item.Properties()));
    public static final DeferredItem<SyringeItem> SYRINGE_ITEM=ITEMS.register("syringe", SyringeItem::new);
    public static final DeferredItem<UnTransfurBottle> UNTRANSFUR_BOTTLE_ITEM=ITEMS.register("untransfur_bottle", UnTransfurBottle::new);
    public static final DeferredItem<UnTransfurSyringeItem> UNTRANSFUR_SYRINGE_ITEM=ITEMS.register("untransfur_syringe", UnTransfurSyringeItem::new);
    public static final DeferredItem<LatexItem> WHITE_LATEX_ITEM = ITEMS.register("white_latex", ()-> new LatexItem(resourceLocation("white_latex_wolf")));

    public static final DeferredItem<BucketItem> LATEX_SOLVENT_BUCKET = ITEMS.register("latex_solvent_bucket", ()->new BucketItem(LATEX_SOLVENT_STILL,new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BucketItem> WHITE_LATEX_BUCKET = ITEMS.register("white_latex_bucket", ()-> new BucketItem(WHITE_LATEX_STILL,new Item.Properties().stacksTo(1)));
    public static final DeferredItem<BucketItem> DARK_LATEX_BUCKET = ITEMS.register("dark_latex_bucket", ()-> new BucketItem(DARK_LATEX_STILL,new Item.Properties().stacksTo(1)));

    //Spawn eggs
    public static final DeferredItem<DeferredSpawnEggItem> TEST_SPAWN_EGG = ITEMS.register("test_spawn_egg", ()-> new DeferredSpawnEggItem(WHITE_LATEX_WOLF_MALE,0x6d81a1,0x22302f,new Item.Properties()));
}