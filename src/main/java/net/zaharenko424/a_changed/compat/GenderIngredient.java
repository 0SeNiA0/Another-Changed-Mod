package net.zaharenko424.a_changed.compat;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.screen.machines.LatexEncoderScreen;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class GenderIngredient {

    public static final IIngredientType<Gender> TYPE = new IIngredientType<>() {

        @Override
        public @NotNull Class<? extends Gender> getIngredientClass() {
            return Gender.class;
        }
    };

    static final IIngredientHelper<Gender> HELPER = new IIngredientHelper<>() {

        @Override
        public @NotNull IIngredientType<Gender> getIngredientType() {
            return GenderIngredient.TYPE;
        }

        @Override
        public @NotNull String getDisplayName(@NotNull Gender ingredient) {
            return ingredient.toString();
        }

        @Override
        public @NotNull String getUniqueId(@NotNull Gender ingredient, @NotNull UidContext context) {
            return ingredient.toString();
        }

        @Override
        public @NotNull ResourceLocation getResourceLocation(@NotNull Gender ingredient) {
            return AChanged.resourceLoc(ingredient.toString().toLowerCase());
        }

        @Override
        public @NotNull Gender copyIngredient(@NotNull Gender ingredient) {
            return ingredient;
        }

        @Override
        public @NotNull String getErrorInfo(@Nullable Gender ingredient) {
            return "Error info. GenderIngredient";
        }
    };

    static final IIngredientRenderer<Gender> RENDERER = new IIngredientRenderer<>() {

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull Gender ingredient) {
            if(ingredient != Gender.NONE)
                guiGraphics.blit(LatexEncoderScreen.TEXTURE, (ingredient.ordinal() == 0 ? 122 : 142), 56, 0,
                        176, 24, 18, 18, 256, 166);
        }

        @Override
        public @NotNull @Unmodifiable List<Component> getTooltip(@NotNull Gender ingredient, @NotNull TooltipFlag tooltipFlag) {
            return List.of();
        }
    };
}