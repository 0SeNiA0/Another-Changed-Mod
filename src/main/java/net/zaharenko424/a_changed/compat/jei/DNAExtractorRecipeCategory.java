package net.zaharenko424.a_changed.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.client.screen.machine.DNAExtractorScreen;
import net.zaharenko424.a_changed.item.BloodSyringe;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRecipeCategory implements IRecipeCategory<DNAExtractorRecipe> {

    public static final RecipeType<DNAExtractorRecipe> TYPE = new RecipeType<>(RecipeRegistry.DNA_EXTRACTOR_RECIPE.getId(), DNAExtractorRecipe.class);

    private final IGuiHelper guiHelper;
    private final IDrawable icon;

    public DNAExtractorRecipeCategory(IGuiHelper guiHelper){
        this.guiHelper = guiHelper;
        icon = new IDrawable() {
            @Override
            public int getWidth() {
                return 16;
            }

            @Override
            public int getHeight() {
                return 16;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.renderItem(ItemRegistry.DNA_EXTRACTOR_ITEM.toStack(), xOffset, yOffset);
            }
        };
    }

    @Override
    public @NotNull RecipeType<DNAExtractorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.dna_extractor");
    }

    @Override
    public int getWidth() {
        return 162;
    }

    @Override
    public int getHeight() {
        return 76;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(@NotNull DNAExtractorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(DNAExtractorScreen.TEXTURE, 0, 0, 162, 76, 7, 5, 162, 76, 256, 166);

        JeiPlugin.drawEnergyConsumption(recipe.getEnergyConsumption(), guiGraphics, getWidth() - 96, 64);
        JeiPlugin.drawProcessingTime(recipe.getProcessingTime(), guiGraphics, getWidth() - 32, 64);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull DNAExtractorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 30).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 30).addItemStack(RecipeUtil.getResultItem(recipe));

        if(recipe.getIngredient().getItems()[0].getItem() instanceof BloodSyringe) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 127, 30).addItemStack(ItemRegistry.SYRINGE_ITEM.toStack());
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull DNAExtractorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addWidget(new ProcessingArrowRecipeWidget(recipe.getProcessingTime(), new ScreenPosition(69, 26),
                guiHelper.drawableBuilder(DNAExtractorScreen.TEXTURE, 176, 0, 24, 25).setTextureSize(256, 166)));
    }
}