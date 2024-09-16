package net.zaharenko424.a_changed.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.*;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.neoforge.network.ConnectionToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
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
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.util.Utils;
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
        registration.registerSubtypeInterpreter(ItemRegistry.BLOOD_SYRINGE.asItem(), (ingredient, context) ->
                ingredient.has(ComponentRegistry.BLOOD_TYPE) ? ingredient.get(ComponentRegistry.BLOOD_TYPE).toString() : IIngredientSubtypeInterpreter.NONE);

        registration.registerSubtypeInterpreter(ItemRegistry.DNA_SAMPLE.asItem(), (ingredient, context) ->
                ingredient.has(ComponentRegistry.DNA_TYPE) ? ingredient.get(ComponentRegistry.DNA_TYPE).toString() : IIngredientSubtypeInterpreter.NONE);

        registration.registerSubtypeInterpreter(ItemRegistry.LATEX_SYRINGE.asItem(), ((ingredient, context) ->
                ingredient.has(ComponentRegistry.TRANSFUR_TYPE) ? ingredient.get(ComponentRegistry.TRANSFUR_TYPE).toString() : IIngredientSubtypeInterpreter.NONE));
        registration.registerSubtypeInterpreter(ItemRegistry.STABILIZED_LATEX_SYRINGE.asItem(), ((ingredient, context) ->
                ingredient.has(ComponentRegistry.TRANSFUR_TYPE) ? ingredient.get(ComponentRegistry.TRANSFUR_TYPE).toString() : IIngredientSubtypeInterpreter.NONE));
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registration) {
        registration.register(GenderIngredient.TYPE, Arrays.stream(Gender.values()).toList(),
                GenderIngredient.HELPER, GenderIngredient.RENDERER);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CompressorRecipeCategory(guiHelper), new DNAExtractorRecipeCategory(),
                new LatexEncoderRecipeCategory(), new LatexPurifierRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.getIngredientManager().removeIngredientsAtRuntime(GenderIngredient.TYPE, Arrays.stream(Gender.values()).toList());

        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(CompressorRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(RecipeRegistry.COMPRESSOR_RECIPE.get())));

        registration.addRecipes(DNAExtractorRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(RecipeRegistry.DNA_EXTRACTOR_RECIPE.get())));

        registration.addRecipes(LatexEncoderRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(RecipeRegistry.LATEX_ENCODER_RECIPE.get())));

        registration.addRecipes(LatexPurifierRecipeCategory.TYPE,
                unWrapRecipes(manager.getAllRecipesFor(RecipeRegistry.LATEX_PURIFIER_RECIPE.get())));
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
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CompressorScreen.class, 80, 35, 23, 16, CompressorRecipeCategory.TYPE);
        registration.addRecipeClickArea(DNAExtractorScreen.class, 82, 36, 16, 16, DNAExtractorRecipeCategory.TYPE);
        registration.addRecipeClickArea(LatexEncoderScreen.class, 83, 36, 38, 12, LatexEncoderRecipeCategory.TYPE);
        registration.addRecipeClickArea(LatexPurifierScreen.class, 82, 37, 18, 11, LatexPurifierRecipeCategory.TYPE);
    }

    public static void drawEnergyConsumption(int energyPerTick, GuiGraphics guiGraphics, int width, int y){
        if (energyPerTick > 0) {
            String timeString = Utils.formatEnergy(energyPerTick) + " EU/t";
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, width - stringWidth, y, 0xFF808080, false);
        }
    }

    public static void drawProcessingTime(int processingTime, GuiGraphics guiGraphics, int width, int y){
        if (processingTime > 0) {
            int cookTimeSeconds = processingTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, width - stringWidth, y, 0xFF808080, false);
        }
    }
}