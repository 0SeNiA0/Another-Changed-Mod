package net.zaharenko424.testmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.*;
import net.zaharenko424.testmod.commands.Transfur;
import net.zaharenko424.testmod.commands.UnTransfur;
import net.zaharenko424.testmod.entity.WhiteLatexBeast;
import net.zaharenko424.testmod.entity.transfurTypes.AbstractTransfurType;
import net.zaharenko424.testmod.entity.transfurTypes.WhiteLatex;
import net.zaharenko424.testmod.item.LatexItem;
import net.zaharenko424.testmod.item.LatexSyringeItem;
import net.zaharenko424.testmod.item.OrangeJuiceItem;
import net.zaharenko424.testmod.item.SyringeItem;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(TestMod.MODID)
public class TestMod {

    public static final String MODID = "testmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    //Registries
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<AbstractTransfurType> TRANSFUR_TYPES = DeferredRegister.create(resourceLocation("transfur_registry"),MODID);

    public static final Supplier<IForgeRegistry<AbstractTransfurType>> REGISTRY = TRANSFUR_TYPES.makeRegistry(RegistryBuilder::new);//TODO

    //Blocks & BlockItems
    public static final RegistryObject<Block> WHITE_LATEX_BLOCK = BLOCKS.register("white_latex_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(6,1).sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Item> WHITE_LATEX_BLOCK_ITEM = ITEMS.register("white_latex_block", () -> new BlockItem(WHITE_LATEX_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> DARK_LATEX_BLOCK = BLOCKS.register("dark_latex_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(6,1).sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Item> DARK_LATEX_BLOCK_ITEM = ITEMS.register("dark_latex_block", () -> new BlockItem(DARK_LATEX_BLOCK.get(), new Item.Properties()));

    //Items
    public static final RegistryObject<Item> ORANGE_ITEM = ITEMS.register("orange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(2f).build())));
    public static final RegistryObject<OrangeJuiceItem> ORANGE_JUICE_ITEM = ITEMS.register("orange_juice", ()-> new OrangeJuiceItem(new Item.Properties()));
    public static final RegistryObject<LatexItem> WHITE_LATEX_ITEM = ITEMS.register("white_latex", ()-> new LatexItem(new Item.Properties(),resourceLocation("white_latex")));
    public static final RegistryObject<LatexItem> DARK_LATEX_ITEM = ITEMS.register("dark_latex", ()-> new LatexItem(new Item.Properties(),resourceLocation("dark_latex")));
    public static final RegistryObject<SyringeItem> SYRINGE_ITEM=ITEMS.register("syringe", ()-> new SyringeItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<LatexSyringeItem> LATEX_SYRINGE_ITEM=ITEMS.register("latex_syringe", ()-> new LatexSyringeItem(new Item.Properties().stacksTo(1)));

    //Transfur types
    public static final RegistryObject<WhiteLatex> WHITE_LATEX_TRANSFUR = TRANSFUR_TYPES.register("white_latex", ()-> new WhiteLatex(resourceLocation("white_latex"),resourceLocation("white_latex_beast")));

    //Entities
    public static final RegistryObject<EntityType<WhiteLatexBeast>> WHITE_LATEX_BEAST = ENTITIES.register("white_latex_beast",()-> EntityType.Builder.<WhiteLatexBeast>of((a, b)->new WhiteLatexBeast(a,b,WHITE_LATEX_TRANSFUR.get()), MobCategory.CREATURE).build(MODID+":white_latex_beast"));

    //Spawn eggs
    public static final RegistryObject<DeferredSpawnEggItem> TEST_SPAWN_EGG = ITEMS.register("test_spawn_egg", ()-> new DeferredSpawnEggItem(WHITE_LATEX_BEAST,0x6d81a1,0x22302f,new Item.Properties()));

    //Creative tabs
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .icon(() -> SYRINGE_ITEM.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.testmod.example_tab"))
            .displayItems((parameters, output) -> {
            output.accept(ORANGE_ITEM.get());
            output.accept(ORANGE_JUICE_ITEM.get());
            output.accept(WHITE_LATEX_ITEM.get());
            output.accept(DARK_LATEX_ITEM.get());
            output.accept(SYRINGE_ITEM.get());
            output.accept(LATEX_SYRINGE_ITEM.get());

            output.accept(WHITE_LATEX_BLOCK_ITEM.get());
            output.accept(DARK_LATEX_BLOCK_ITEM.get());

            output.accept(TEST_SPAWN_EGG.get());
            }).build());

    @Contract("_ -> new")
    private static @NotNull ResourceLocation resourceLocation(@NotNull String path){
        return new ResourceLocation(MODID,path);
    }

    public TestMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        TRANSFUR_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(@NotNull RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();
        Transfur.register(dispatcher);
        UnTransfur.register(dispatcher);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.@NotNull PlayerLoggedInEvent event){
        TransfurManager.updatePlayer(event.getEntity());
    }
}