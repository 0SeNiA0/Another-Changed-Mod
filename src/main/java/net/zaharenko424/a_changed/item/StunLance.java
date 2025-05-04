package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.registry.ArmorMaterialRegistry;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StunLance extends SwordItem {

    private static final ResourceLocation modifier = AChanged.resourceLoc("lance_range");

    public StunLance() {
        super(ArmorMaterialRegistry.ITEM_TIER, new Properties().rarity(Rarity.UNCOMMON)
                .attributes(createAttributes(ArmorMaterialRegistry.ITEM_TIER, 2, -3f)
                        .withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(modifier, 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        if(stack.has(ComponentRegistry.ENABLED)) return super.getBarColor(stack);
        return -4795971;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.setDamageValue(stack.getMaxDamage());
        return stack;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if(level.isClientSide || usedHand != InteractionHand.MAIN_HAND) return super.use(level, player, usedHand);
        ItemStack stunLance = player.getMainHandItem();

        if(stunLance.has(ComponentRegistry.ENABLED)){
            stunLance.remove(ComponentRegistry.ENABLED);
        } else {
            if(stunLance.getCapability(Capabilities.EnergyStorage.ITEM).getEnergyStored() >= 500)
                stunLance.set(ComponentRegistry.ENABLED, Unit.INSTANCE);
            else return InteractionResultHolder.fail(stunLance);
        }

        return InteractionResultHolder.consume(stunLance);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if(!stack.has(ComponentRegistry.ENABLED)) return true;

        ExtendedEnergyStorage storage = (ExtendedEnergyStorage) stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if(!(attacker instanceof Player player) || !player.isCreative()) storage.addEnergy(-500);
        if(storage.getEnergyStored() < 500) stack.remove(ComponentRegistry.ENABLED);

        target.addEffect(new MobEffectInstance(MobEffectRegistry.ELECTROCUTED_DEBUFF, 80, 0, false, false));

        if(!(attacker instanceof Player player)) return true;

        player.getCooldowns().addCooldown(stack.getItem(), 20);

        double entityReachSq = Mth.square(player.entityInteractionRange()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update this to use canReach, since it uses closest-corner checks.
        for(LivingEntity living : player.level()
                .getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(.5, 0.25, .5))) {
            if (living != player
                    && living != target
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