package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.zaharenko424.a_changed.capability.energy.EnergyConsumer;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StunBaton extends SwordItem {

    public StunBaton() {
        super(Tiers.WOOD, 3, -2.4f, new Properties().durability(100));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        if(pStack.getOrCreateTag().contains("enabled")) return super.getBarColor(pStack);
        return -4795971;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(pLevel.isClientSide || pUsedHand != InteractionHand.MAIN_HAND) return super.use(pLevel, pPlayer, pUsedHand);
        ItemStack stunBaton = pPlayer.getMainHandItem();

        CompoundTag tag = stunBaton.getOrCreateTag();
        if(!tag.contains("enabled")){
            if(stunBaton.getCapability(Capabilities.EnergyStorage.ITEM).getEnergyStored() >= 500)
                tag.putByte("enabled", (byte) 0);
            else return InteractionResultHolder.fail(stunBaton);
        } else tag.remove("enabled");

        return InteractionResultHolder.consume(stunBaton);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        CompoundTag tag = stack.getOrCreateTag();
        if(!tag.contains("enabled")) return true;
        EnergyConsumer storage = (EnergyConsumer) stack.getCapability(Capabilities.EnergyStorage.ITEM);
        storage.consumeEnergy(500);

        pTarget.addEffect(new MobEffectInstance(MobEffectRegistry.ELECTROCUTED_DEBUFF.get(), 80, 0, false, false));

        if(!(pAttacker instanceof Player player)) return true;
        player.getCooldowns().addCooldown(stack.getItem(), 20);

        double entityReachSq = Mth.square(player.getEntityReach()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update this to use canReach, since it uses closest-corner checks.
        for(LivingEntity living : player.level()
                .getEntitiesOfClass(LivingEntity.class, pTarget.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
            if (living != player
                    && living != pTarget
                    && !player.isAlliedTo(living)
                    && (!(living instanceof ArmorStand) || !((ArmorStand)living).isMarker())
                    && player.distanceToSqr(living) < entityReachSq) {
                living.addEffect(new MobEffectInstance(MobEffectRegistry.ELECTROCUTED_DEBUFF.get(), 80, 0, false, false));
            }
        }

        if(storage.getEnergyStored() < 500) tag.remove("enabled");
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        IEnergyStorage storage = pStack.getCapability(Capabilities.EnergyStorage.ITEM);
        tooltip.add(Component.literal("EU: "+ Utils.formatEnergy(storage.getEnergyStored()) + "/" + Utils.formatEnergy(storage.getMaxEnergyStored())).withStyle(ChatFormatting.DARK_GREEN));
        if(pStack.getOrCreateTag().contains("enabled")){
            tooltip.add(Component.translatable("tooltip.a_changed.stun_baton_on").withStyle(ChatFormatting.DARK_GREEN));
        } else tooltip.add(Component.translatable("tooltip.a_changed.stun_baton_off").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment) {
        return false;
    }
}