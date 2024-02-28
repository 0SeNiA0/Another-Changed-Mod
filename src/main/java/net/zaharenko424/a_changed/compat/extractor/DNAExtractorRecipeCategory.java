package net.zaharenko424.a_changed.compat.extractor;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.screen.machines.DNAExtractorScreen;
import net.zaharenko424.a_changed.recipe.DNAExtractorRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRecipeCategory implements IRecipeCategory<DNAExtractorRecipe> {

    public static final RecipeType<DNAExtractorRecipe> TYPE = RecipeType
            .create(AChanged.MODID, "dna_extractor", DNAExtractorRecipe.class);

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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull DNAExtractorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 75, 5).addIngredients(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 3).addItemStack(recipe.getResultItem());
        builder.setShapeless(10,60);
    }
}