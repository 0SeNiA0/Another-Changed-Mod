package net.zaharenko424.testmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import net.zaharenko424.testmod.commands.Transfur;
import net.zaharenko424.testmod.commands.UnTransfur;
import net.zaharenko424.testmod.item.LatexItem;
import net.zaharenko424.testmod.item.LatexSyringeItem;
import net.zaharenko424.testmod.item.OrangeJuiceItem;
import net.zaharenko424.testmod.item.SyringeItem;
import net.zaharenko424.testmod.network.PacketHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(TestMod.MODID)
public class TestMod {

    public static final String MODID = "testmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    //Registries
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    //Blocks & BlockItems
    public static final RegistryObject<Block> WHITE_LATEX_BLOCK = BLOCKS.register("white_latex_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).strength(6,1).sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Item> WHITE_LATEX_BLOCK_ITEM = ITEMS.register("white_latex_block", () -> new BlockItem(WHITE_LATEX_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Block> DARK_LATEX_BLOCK = BLOCKS.register("dark_latex_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(6,1).sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Item> DARK_LATEX_BLOCK_ITEM = ITEMS.register("dark_latex_block", () -> new BlockItem(DARK_LATEX_BLOCK.get(), new Item.Properties()));

    //Items
    public static final RegistryObject<Item> ORANGE_ITEM = ITEMS.register("orange", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(2f).build())));
    public static final RegistryObject<OrangeJuiceItem> ORANGE_JUICE_ITEM = ITEMS.register("orange_juice", ()-> new OrangeJuiceItem(new Item.Properties()));
    public static final RegistryObject<LatexItem> WHITE_LATEX_ITEM = ITEMS.register("white_latex", ()-> new LatexItem(new Item.Properties(),"white_latex"));
    public static final RegistryObject<LatexItem> DARK_LATEX_ITEM = ITEMS.register("dark_latex", ()-> new LatexItem(new Item.Properties(),"dark_latex"));
    public static final RegistryObject<SyringeItem> SYRINGE_ITEM=ITEMS.register("syringe", ()-> new SyringeItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<LatexSyringeItem> LATEX_SYRINGE_ITEM=ITEMS.register("latex_syringe", ()-> new LatexSyringeItem(new Item.Properties().stacksTo(1)));

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
            }).build());

    public TestMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        PacketHandler.init();
    }

    @SubscribeEvent
    public void onRegisterCommands(@NotNull RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher=event.getDispatcher();
        Transfur.register(dispatcher);
        UnTransfur.register(dispatcher);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.@NotNull PlayerLoggedInEvent event){
        TransfurManager.updatePlayer(event.getEntity());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
