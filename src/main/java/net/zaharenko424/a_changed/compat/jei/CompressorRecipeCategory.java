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
import net.zaharenko424.a_changed.client.screen.machines.CompressorScreen;
import net.zaharenko424.a_changed.recipe.CompressorRecipe;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class CompressorRecipeCategory implements IRecipeCategory<CompressorRecipe> {

    public static final RecipeType<CompressorRecipe> TYPE = new RecipeType<>(RecipeRegistry.COMPRESSOR_RECIPE.getId(), CompressorRecipe.class);

    private final IGuiHelper guiHelper;
    private final IDrawable icon;

    public CompressorRecipeCategory(IGuiHelper guiHelper){
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
                guiGraphics.renderItem(ItemRegistry.COMPRESSOR_ITEM.toStack(), xOffset, yOffset);
            }
        };
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
    public void draw(@NotNull CompressorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(CompressorScreen.TEXTURE, 0, 0, 162, 76, 7, 5, 162, 76, 256, 166);

        JeiPlugin.drawEnergyConsumption(recipe.getEnergyConsumption(), guiGraphics, getWidth() - 120, 64);
        JeiPlugin.drawProcessingTime(recipe.getProcessingTime(), guiGraphics, getWidth() - 100, 64);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CompressorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 30).addIngredients(recipe.getIngredients().getFirst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 30).addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull CompressorRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addWidget(new ProcessingArrowRecipeWidget(recipe.getProcessingTime(), new ScreenPosition(69, 30),
                guiHelper.drawableBuilder(CompressorScreen.TEXTURE, 176, 0, 24, 16).setTextureSize(256,166)));
    }
}