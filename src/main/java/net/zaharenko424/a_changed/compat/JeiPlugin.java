package net.zaharenko424.a_changed.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.*;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.neoforge.network.ConnectionToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.screen.machines.CompressorScreen;
import net.zaharenko424.a_changed.client.screen.machines.DNAExtractorScreen;
import net.zaharenko424.a_changed.client.screen.machines.LatexEncoderScreen;
import net.zaharenko424.a_changed.client.screen.machines.LatexPurifierScreen;
import net.zaharenko424.a_changed.compat.encoder.LatexEncoderRecipeCategory;
import net.zaharenko424.a_changed.compat.encoder.LatexEncoderTransferHandler;
import net.zaharenko424.a_changed.compat.extractor.DNAExtractorRecipeCategory;
import net.zaharenko424.a_changed.compat.extractor.DNAExtractorTransferHandler;
import net.zaharenko424.a_changed.menu.machines.CompressorMenu;
import net.zaharenko424.a_changed.menu.machines.DNAExtractorMenu;
import net.zaharenko424.a_changed.menu.machines.LatexEncoderMenu;
import net.zaharenko424.a_changed.menu.machines.LatexPurifierMenu;
import net.zaharenko424.a_changed.recipe.CompressorRecipe;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.recipe.LatexEncoderRecipe;
import net.zaharenko424.a_changed.recipe.LatexPurifierRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return AChanged.resourceLoc("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ItemRegistry.BLOOD_SYRINGE.asItem(), ItemRegistry.DNA_SAMPLE.asItem(),
                ItemRegistry.LATEX_SYRINGE_ITEM.asItem());
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registration) {
        registration.register(GenderIngredient.TYPE, Arrays.stream(Gender.values()).toList(),
                GenderIngredient.HELPER, GenderIngredient.RENDERER);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CompressorRecipeCategory(), new DNAExtractorRecipeCategory(),
                new LatexEncoderRecipeCategory(), new LatexPurifierRecipeCategory());
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.getIngredientManager().removeIngredientsAtRuntime(GenderIngredient.TYPE, Arrays.stream(Gender.values()).toList());

        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(CompressorRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(CompressorRecipe.Type.INSTANCE)));

        registration.addRecipes(DNAExtractorRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(DNAExtractorRecipe.Type.INSTANCE)));

        registration.addRecipes(LatexEncoderRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(LatexEncoderRecipe.Type.INSTANCE)));

        registration.addRecipes(LatexPurifierRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(LatexPurifierRecipe.Type.INSTANCE)));
    }

    private <T extends Recipe<?>> List<T> unWrapRecipes(@NotNull List<RecipeHolder<T>> holders){
        return holders.stream().map(RecipeHolder::value).collect(Collectors.toList());
    }

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration registration) {
        IJeiHelpers helpers = registration.getJeiHelpers();
        IConnectionToServer connectionToServer = new ConnectionToServer();
        IStackHelper stackHelper = helpers.getStackHelper();
        IRecipeTransferHandlerHelper transferHelper = registration.getTransferHelper();

        registration.addRecipeTransferHandler(CompressorMenu.class, MenuRegistry.COMPRESSOR_MENU.get(),
                CompressorRecipeCategory.TYPE, 37, 1, 0, 36);

        registration.addRecipeTransferHandler(new DNAExtractorTransferHandler(connectionToServer, stackHelper, transferHelper,
                transferHelper.createBasicRecipeTransferInfo(DNAExtractorMenu.class, MenuRegistry.DNA_EXTRACTOR_MENU.get(),
                        DNAExtractorRecipeCategory.TYPE, 36, 1, 0, 36)), DNAExtractorRecipeCategory.TYPE);

        registration.addRecipeTransferHandler(new LatexEncoderTransferHandler(connectionToServer, stackHelper, transferHelper,
                transferHelper.createBasicRecipeTransferInfo(LatexEncoderMenu.class, null,
                        LatexEncoderRecipeCategory.TYPE, 36, 7, 0, 36)), LatexEncoderRecipeCategory.TYPE);

        registration.addRecipeTransferHandler(LatexPurifierMenu.class, MenuRegistry.LATEX_PURIFIER_MENU.get(),
                LatexPurifierRecipeCategory.TYPE, 37, 1, 0, 36);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ItemRegistry.COMPRESSOR_ITEM.toStack(), CompressorRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemRegistry.DNA_EXTRACTOR_ITEM.toStack(), DNAExtractorRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemRegistry.LATEX_ENCODER_ITEM.toStack(), LatexEncoderRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemRegistry.LATEX_PURIFIER_ITEM.toStack(), LatexPurifierRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemRegistry.SYRINGE_ITEM.toStack());
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CompressorScreen.class, 80, 35, 23, 16, CompressorRecipeCategory.TYPE);
        registration.addRecipeClickArea(DNAExtractorScreen.class, 82, 36, 16, 16, DNAExtractorRecipeCategory.TYPE);
        registration.addRecipeClickArea(LatexEncoderScreen.class, 83, 36, 38, 12, LatexEncoderRecipeCategory.TYPE);
        registration.addRecipeClickArea(LatexPurifierScreen.class, 82, 37, 18, 11, LatexPurifierRecipeCategory.TYPE);
    }
}