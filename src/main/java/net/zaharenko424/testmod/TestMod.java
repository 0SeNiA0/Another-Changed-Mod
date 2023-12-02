package net.zaharenko424.testmod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.testmod.effects.LatexSolventEffect;
import net.zaharenko424.testmod.effects.UnTransfurEffect;
import net.zaharenko424.testmod.item.LatexSyringeItem;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static net.zaharenko424.testmod.registry.BlockEntityRegistry.BLOCK_ENTITIES;
import static net.zaharenko424.testmod.registry.BlockRegistry.BLOCKS;
import static net.zaharenko424.testmod.registry.EntityRegistry.ENTITIES;
import static net.zaharenko424.testmod.registry.FluidRegistry.FLUIDS;
import static net.zaharenko424.testmod.registry.FluidRegistry.FLUID_TYPES;
import static net.zaharenko424.testmod.registry.ItemRegistry.*;
import static net.zaharenko424.testmod.registry.TransfurRegistry.TRANSFUR_REGISTRY;
import static net.zaharenko424.testmod.registry.TransfurRegistry.TRANSFUR_TYPES;

@Mod(TestMod.MODID)
public class TestMod {

    public static final String MODID = "testmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    //Registries
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT,MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID);

    //Effects
    public static final DeferredHolder<MobEffect, UnTransfurEffect> UNTRANSFUR = EFFECTS.register("untransfur", UnTransfurEffect::new);
    public static final DeferredHolder<MobEffect, LatexSolventEffect> LATEX_SOLVENT = EFFECTS.register("latex_solvent", LatexSolventEffect::new);

    //Tags
    public static final TagKey<EntityType<?>> TRANSFURRABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, resourceLoc("transfurrable"));

    //Creative tabs
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .icon(() -> SYRINGE_ITEM.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.testmod.example_tab"))
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
            output.accept(BOLTED_LAB_TILE_ITEM);
            output.accept(BROWN_LAB_BLOCK_ITEM);
            output.accept(CARPET_BLOCK_ITEM);
            output.accept(CONNECTED_LAB_TILE_ITEM);
            output.accept(LAB_BLOCK_ITEM);
            output.accept(LAB_TILE_ITEM);
            output.accept(YELLOW_LAB_BLOCK_ITEM);

            output.accept(CARDBOARD_BOX_ITEM);
            output.accept(CHAIR_ITEM);
            output.accept(METAL_BOX_ITEM);
            output.accept(SCANNER_ITEM);
            output.accept(TABLE_ITEM);

            output.accept(LATEX_SOLVENT_BUCKET);
            output.accept(WHITE_LATEX_BUCKET);
            output.accept(DARK_LATEX_BUCKET);

            output.accept(SYRINGE_ITEM);
            output.accept(UNTRANSFUR_SYRINGE_ITEM);
            TRANSFUR_REGISTRY.stream().forEach((tf)->output.accept(LatexSyringeItem.encodeTransfur(tf.location)));
            }).build());

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resourceLoc(@NotNull String path){
        return new ResourceLocation(MODID,path);
    }

    public TestMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
        ENTITIES.register(modEventBus);
        EFFECTS.register(modEventBus);
        TRANSFUR_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}