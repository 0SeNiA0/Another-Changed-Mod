package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.menu.PneumaticSyringeRifleMenu;
import net.zaharenko424.a_changed.menu.SyringeCoilGunMenu;
import net.zaharenko424.a_changed.menu.machines.*;

import static net.zaharenko424.a_changed.AChanged.MODID;

public class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CapacitorMenu>> CAPACITOR_MENU = MENU_TYPES
            .register("capacitor", ()-> IMenuTypeExtension.create(CapacitorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<CompressorMenu>> COMPRESSOR_MENU = MENU_TYPES
            .register("compressor", ()-> IMenuTypeExtension.create(CompressorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DNAExtractorMenu>> DNA_EXTRACTOR_MENU = MENU_TYPES
            .register("dna_extractor", ()-> IMenuTypeExtension.create(DNAExtractorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorMenu>> GENERATOR_MENU = MENU_TYPES
            .register("generator", ()-> IMenuTypeExtension.create(GeneratorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LatexEncoderMenu>> LATEX_ENCODER_MENU = MENU_TYPES
            .register("latex_encoder", ()-> IMenuTypeExtension.create(LatexEncoderMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LatexPurifierMenu>> LATEX_PURIFIER_MENU = MENU_TYPES
            .register("latex_purifier", ()-> IMenuTypeExtension.create(LatexPurifierMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<PneumaticSyringeRifleMenu>> PNEUMATIC_SYRINGE_RIFLE_MENU = MENU_TYPES
            .register("pneumatic_syringe_rifle", ()-> IMenuTypeExtension.create((a, b, c) -> new PneumaticSyringeRifleMenu(a, b)));
    public static final DeferredHolder<MenuType<?>, MenuType<SyringeCoilGunMenu>> SYRINGE_COIL_GUN_MENU = MENU_TYPES
            .register("syringe_coil_gun", ()-> IMenuTypeExtension.create((a, b, c) -> new SyringeCoilGunMenu(a, b)));
}