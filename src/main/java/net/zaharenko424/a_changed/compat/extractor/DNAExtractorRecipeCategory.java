package net.zaharenko424.a_changed.compat.extractor;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.client.screen.machines.DNAExtractorScreen;
import net.zaharenko424.a_changed.compat.JeiPlugin;
import net.zaharenko424.a_changed.entity.block.machines.DNAExtractorEntity;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRecipeCategory implements IRecipeCategory<DNAExtractorRecipe> {

    public static final RecipeType<DNAExtractorRecipe> TYPE = new RecipeType<>(RecipeRegistry.DNA_EXTRACTOR_RECIPE.getId(), DNAExtractorRecipe.class);

    @Override
    public @NotNull RecipeType<DNAExtractorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.dna_extractor");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 162;
            }

            @Override
            public int getHeight() {
                return 76;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.blit(DNAExtractorScreen.TEXTURE, xOffset, yOffset, 162, 76, 7, 5, 162, 76, 256, 166);
            }
        };
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return new IDrawable() {
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
    public void draw(@NotNull DNAExtractorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        JeiPlugin.drawEnergyConsumption(DNAExtractorEntity.energyConsumption, guiGraphics, getWidth() - 96, 64);
        JeiPlugin.drawProcessingTime(DNAExtractorEntity.maxProgress, guiGraphics, getWidth() - 32, 64);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull DNAExtractorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 75, 5).addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 3).addItemStack(RecipeUtil.getResultItem(recipe));
        builder.setShapeless(10,60);
    }
}