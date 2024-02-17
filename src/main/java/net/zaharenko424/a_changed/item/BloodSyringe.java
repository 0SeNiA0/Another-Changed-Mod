package net.zaharenko424.a_changed.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodSyringe extends Item {

    private static final ResourceLocation playerType = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER);

    public BloodSyringe() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        CompoundTag tag = pStack.getTag();
        if(NBTUtils.hasModTag(tag)) {
            CompoundTag modTag = NBTUtils.modTag(tag);
            ResourceLocation entityType = new ResourceLocation(modTag.getString("entity_type"));
            pTooltipComponents.add(Component.translatable("misc.a_changed.blood_syringe",
                    entityType.equals(playerType) && modTag.contains("name") ? modTag.getString("name")
                            : BuiltInRegistries.ENTITY_TYPE.get(entityType).getDescription()));
        } else pTooltipComponents.add(Component.literal("Invalid tag"));
    }

    public static @NotNull ItemStack encodeEntity(@NotNull LivingEntity entity){
        ItemStack syringe = ItemRegistry.BLOOD_SYRINGE.get().getDefaultInstance();
        CompoundTag tag = syringe.hasTag() ? syringe.getTag() : new CompoundTag();
        CompoundTag modTag = NBTUtils.modTag(tag);
        modTag.putString("entity_type", BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
        if(entity instanceof Player player) modTag.putString("name", player.getDisplayName().getString());
        syringe.setTag(tag);
        return syringe;
    }

    public static @NotNull ItemStack encodeEntityType(@NotNull EntityType<?> entity, @Nullable String playerName){
        ItemStack syringe = ItemRegistry.BLOOD_SYRINGE.get().getDefaultInstance();
        CompoundTag tag = syringe.hasTag() ? syringe.getTag() : new CompoundTag();
        CompoundTag modTag = NBTUtils.modTag(tag);
        modTag.putString("entity_type", BuiltInRegistries.ENTITY_TYPE.getKey(entity).toString());
        if(playerName != null) modTag.putString("name", playerName);
        syringe.setTag(tag);
        return syringe;
    }

    public static @Nullable ResourceLocation decodeEntity(@NotNull ItemStack syringe){
        if(!(syringe.getItem() instanceof BloodSyringe)) throw new IllegalArgumentException("syringe must be an instance of BloodSyringe!");
        if(!syringe.hasTag()) return null;
        CompoundTag modTag = NBTUtils.modTag(syringe.getTag());
        if(!modTag.contains("entity_type")) return null;
        return new ResourceLocation(modTag.getString("entity_type"));
    }
}