package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class BloodSyringe extends Item {

    public BloodSyringe() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack syringe, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(syringe, context, tooltipComponents, tooltipFlag);
        if(syringe.has(ComponentRegistry.BLOOD_TYPE)){
            MutableComponent component = Component.translatable("tooltip.a_changed.blood_syringe", syringe.get(ComponentRegistry.BLOOD_TYPE)).withStyle(ChatFormatting.DARK_RED);
            if(syringe.has(ComponentRegistry.BLOOD_OWNER_NAME)) component.append(" (").append(syringe.get(ComponentRegistry.BLOOD_OWNER_NAME)).append(")");
            tooltipComponents.add(component);
        } else tooltipComponents.add(Component.literal("Invalid tag!").withColor(Color.RED.getRGB()));
    }

    public static @NotNull ItemStack encodeEntity(@NotNull LivingEntity entity){
        return encodeEntityType(entity.getType(), entity instanceof Player || entity.hasCustomName() ? entity.getDisplayName() : null);
    }

    public static @NotNull ItemStack encodeEntityType(@NotNull EntityType<?> entity, @Nullable Component name){
        ItemStack syringe = ItemRegistry.BLOOD_SYRINGE.get().getDefaultInstance();

        syringe.set(ComponentRegistry.BLOOD_TYPE, BuiltInRegistries.ENTITY_TYPE.getKey(entity));

        if(name != null) syringe.set(ComponentRegistry.BLOOD_OWNER_NAME, name);
        return syringe;
    }

    public static @Nullable ResourceLocation decodeEntity(@NotNull ItemStack syringe){
        if(!(syringe.getItem() instanceof BloodSyringe)) throw new IllegalArgumentException("syringe must be an instance of BloodSyringe!");
        if(!syringe.has(ComponentRegistry.BLOOD_TYPE)) return null;
        return syringe.get(ComponentRegistry.BLOOD_TYPE);
    }
}