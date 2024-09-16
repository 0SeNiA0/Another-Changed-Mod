package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StunBaton extends SwordItem {

    public StunBaton() {
        super(Tiers.WOOD, new Properties().durability(100).rarity(Rarity.UNCOMMON)
                .attributes(createAttributes(Tiers.WOOD, 3, -2.4f)));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        if(pStack.has(ComponentRegistry.ENABLED)) return super.getBarColor(pStack);
        return -4795971;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(pLevel.isClientSide || pUsedHand != InteractionHand.MAIN_HAND) return super.use(pLevel, pPlayer, pUsedHand);
        ItemStack stunBaton = pPlayer.getMainHandItem();

        if(stunBaton.has(ComponentRegistry.ENABLED)){
            stunBaton.remove(ComponentRegistry.ENABLED);
        } else {
            if(stunBaton.getCapability(Capabilities.EnergyStorage.ITEM).getEnergyStored() >= 500)
                stunBaton.set(ComponentRegistry.ENABLED, Unit.INSTANCE);
            else return InteractionResultHolder.fail(stunBaton);
        }

        return InteractionResultHolder.consume(stunBaton);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        if(!stack.has(ComponentRegistry.ENABLED)) return true;

        ExtendedEnergyStorage storage = (ExtendedEnergyStorage) stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if(!(pAttacker instanceof Player player) || !player.isCreative()) storage.addEnergy(-500);
        if(storage.getEnergyStored() < 500) stack.remove(ComponentRegistry.ENABLED);

        pTarget.addEffect(new MobEffectInstance(MobEffectRegistry.ELECTROCUTED_DEBUFF, 80, 0, false, false));

        if(!(pAttacker instanceof Player player)) return true;

        player.getCooldowns().addCooldown(stack.getItem(), 20);

        double entityReachSq = Mth.square(player.entityInteractionRange()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update this to use canReach, since it uses closest-corner checks.
        for(LivingEntity living : player.level()
                .getEntitiesOfClass(LivingEntity.class, pTarget.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
            if (living != player
                    && living != pTarget
                    && !player.isAlliedTo(living)
                    && (!(living instanceof ArmorStand) || !((ArmorStand)living).isMarker())
                    && player.distanceToSqr(living) < entityReachSq) {
                living.addEffect(new MobEffectInstance(MobEffectRegistry.ELECTROCUTED_DEBUFF, 80, 0, false, false));
            }
        }

        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        tooltipComponents.add(Component.literal("EU: "+ Utils.formatEnergy(storage.getEnergyStored()) + "/" + Utils.formatEnergy(storage.getMaxEnergyStored())).withStyle(ChatFormatting.DARK_GREEN));
        if(stack.has(ComponentRegistry.ENABLED)){
            tooltipComponents.add(Component.translatable("tooltip.a_changed.stun_baton_on").withStyle(ChatFormatting.DARK_GREEN));
        } else tooltipComponents.add(Component.translatable("tooltip.a_changed.stun_baton_off").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }
}