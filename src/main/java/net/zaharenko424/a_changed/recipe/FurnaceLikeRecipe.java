package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public abstract class FurnaceLikeRecipe <C extends Container> implements SimpleRecipe<C> {

    protected final Ingredient ingredient;
    protected final ItemStack result;

    public FurnaceLikeRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container) {
        int amount = 0;
        ItemStack item = container.getItem(1);
        for(ItemStack stack : ingredient.getItems()){
            if(!ItemStack.isSameItem(item, stack)) continue;
            amount = stack.getCount();
            break;
        }
        container.removeItem(1, amount);
        return result.copy();
    }

    public Ingredient getIngredient(){
        return ingredient;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return result;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        if(level.isClientSide) return false;
        return Utils.test(container.getItem(1), ingredient);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }
}