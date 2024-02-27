package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.capability.item.PneumaticSyringeRifleItemHandlerCapability;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractSyringeRifle extends Item implements MenuProvider {

    public AbstractSyringeRifle(@NotNull Properties pProperties) {
        super(pProperties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(@NotNull LivingEntity entityLiving, @NotNull InteractionHand hand, @NotNull ItemStack rifle) {
                PneumaticSyringeRifleItemHandlerCapability.PneumaticSyringeRifleItemHandler handler = rifle.getData(AttachmentRegistry.SYRINGE_RIFLE_ITEM_HANDLER);
                return hasAmmo(handler) && hasFuel(handler.getStackInSlot(0)) ? HumanoidModel.ArmPose.BOW_AND_ARROW
                        : HumanoidModel.ArmPose.ITEM;
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(level.isClientSide || hand != InteractionHand.MAIN_HAND) return super.use(level, player, hand);
        ItemStack rifle = player.getMainHandItem();

        if(player.isCrouching()){
            player.openMenu(this);
            return InteractionResultHolder.success(rifle);
        }

        IItemHandler handler = rifle.getCapability(Capabilities.ItemHandler.ITEM);

        if(!hasFuel(handler.getStackInSlot(0)) || !hasAmmo(handler)) return InteractionResultHolder.fail(rifle);//no energy/air or ammo

        if(!player.isCreative()) consumeFuel(handler);

        //shoot projectile
        SyringeProjectile syringe = new SyringeProjectile(level, player, useFirst(handler, player.isCreative()));
        syringe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 3, 1.2f);
        level.addFreshEntity(syringe);

        playSound(level, player);

        player.getCooldowns().addCooldown(rifle.getItem(), 20);
        return InteractionResultHolder.consume(rifle);
    }

    public boolean hasAmmo(@NotNull IItemHandler handler){
        for(int i = 1; i < 9; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    AbstractTransfurType useFirst(IItemHandler handler, boolean simulate){
        for(int i = 1; i < 9; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return TransfurManager.getTransfurType(
                    Objects.requireNonNull(LatexSyringeItem.decodeTransfur(handler.extractItem(i, 1, simulate))));
        }
        return null;
    }

    boolean hasFuel(@NotNull ItemStack fuelStack) {
        return !fuelStack.isEmpty();
    }

    abstract void consumeFuel(IItemHandler handler);

    abstract void playSound(Level level, Player player);

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
        IItemHandler inventory = stack.getCapability(Capabilities.ItemHandler.ITEM);
        appendHoverText(inventory.getStackInSlot(0), tooltip);
        int shots = 0;
        for(int i = 1; i < 9; i++){
            if(!inventory.getStackInSlot(i).isEmpty()) shots++;
        }
        tooltip.add(Component.translatable("tooltip.a_changed.syringe_rifle_shots", shots).withStyle(ChatFormatting.GRAY));
    }

    abstract void appendHoverText(ItemStack fuel, List<Component> tooltip);

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }
}