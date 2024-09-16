package net.zaharenko424.a_changed.compat;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.client.screen.machines.CompressorScreen;
import net.zaharenko424.a_changed.recipe.CompressorRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class CompressorRecipeCategory implements IRecipeCategory<CompressorRecipe> {

    public static final RecipeType<CompressorRecipe> TYPE = new RecipeType<>(RecipeRegistry.COMPRESSOR_RECIPE.getId(), CompressorRecipe.class);

    private final IGuiHelper guiHelper;

    public CompressorRecipeCategory(IGuiHelper guiHelper){
        this.guiHelper = guiHelper;
    }

    @Override
    public @NotNull RecipeType<CompressorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("container.a_changed.compressor");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return new IDrawable() {

            @Override
            public int getWidth() {
                return 82;
            }

            @Override
            public int getHeight() {
                return 54;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.blit(CompressorScreen.TEXTURE, xOffset, yOffset, 82, 54, 55, 16, 82, 54, 256, 166);
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
                guiGraphics.renderItem(ItemRegistry.COMPRESSOR_ITEM.toStack(), xOffset, yOffset);
            }
        };
    }

    @Override
    public void draw(@NotNull CompressorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        JeiPlugin.drawEnergyConsumption(recipe.getEnergyConsumption(), guiGraphics, getWidth() - 20, 45);
        JeiPlugin.drawProcessingTime(recipe.getProcessingTime(), guiGraphics, getWidth(), 45);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CompressorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().getFirst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(RecipeUtil.getResultItem(recipe));
    }

    //@Override//TODO use correct texture for arrow
    //public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull CompressorRecipe recipe, @NotNull IFocusGroup focuses) {
    //    builder.addWidget(new ProcessingArrowRecipeWidget(guiHelper, recipe.getProcessingTime(), new ScreenPosition(24, 18)));
    //}
}