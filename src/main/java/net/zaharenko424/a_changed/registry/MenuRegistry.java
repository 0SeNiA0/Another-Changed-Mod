package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.menu.DNAExtractorMenu;
import net.zaharenko424.a_changed.menu.GeneratorMenu;
import net.zaharenko424.a_changed.menu.LatexPurifierMenu;

import static net.zaharenko424.a_changed.AChanged.MODID;

public class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<DNAExtractorMenu>> DNA_EXTRACTOR_MENU = MENU_TYPES
            .register("dna_extractor", ()-> IMenuTypeExtension.create(DNAExtractorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorMenu>> GENERATOR_MENU = MENU_TYPES
            .register("generator", ()-> IMenuTypeExtension.create(GeneratorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LatexPurifierMenu>> LATEX_PURIFIER_MENU = MENU_TYPES
            .register("latex_purifier", ()-> IMenuTypeExtension.create(LatexPurifierMenu::new));
}
