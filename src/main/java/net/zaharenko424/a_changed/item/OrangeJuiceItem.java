package net.zaharenko424.a_changed.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.NotNull;

public class OrangeJuiceItem extends Item {

    public OrangeJuiceItem(@NotNull Item.Properties properties) {
        super(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(.5f).usingConvertsTo(Items.GLASS_BOTTLE).build()).stacksTo(16));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }
}