package net.zaharenko424.a_changed.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSyringeRifle extends Item implements MenuProvider {

    public AbstractSyringeRifle(@NotNull Properties pProperties) {
        super(pProperties);
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

        if(!hasFuel(rifle, handler) || !hasAmmo(handler)) return InteractionResultHolder.fail(rifle);//no energy/air or ammo

        if(!player.isCreative()) consumeFuel(rifle, handler);

        //shoot projectile
        SyringeProjectile syringe = new SyringeProjectile(level, player, useFirst(handler, player.isCreative()));
        syringe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, velocity(), accuracy());
        level.addFreshEntity(syringe);

        playSound(level, player);

        player.getCooldowns().addCooldown(rifle.getItem(), cooldown());

        CriteriaTriggers.SHOT_CROSSBOW.trigger((ServerPlayer) player, rifle);

        return InteractionResultHolder.consume(rifle);
    }

    public abstract boolean hasAmmo(@NotNull IItemHandler handler);

    abstract TransfurType useFirst(@NotNull IItemHandler handler, boolean simulate);

    public abstract boolean hasFuel(ItemStack rifle, @NotNull IItemHandler inventory);

    abstract void consumeFuel(ItemStack rifle, IItemHandler handler);

    abstract int velocity();

    abstract float accuracy();

    abstract void playSound(Level level, Player player);

    abstract int cooldown();

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }
}