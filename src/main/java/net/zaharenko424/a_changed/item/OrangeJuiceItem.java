package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class OrangeJuiceItem extends Item {

    public OrangeJuiceItem(@NotNull Item.Properties properties) {
        super(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(.5f).build()).stacksTo(16));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        Player player = (Player) entity;
        if(!player.getAbilities().instabuild) player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        return entity.eat(level, stack);
    }
}