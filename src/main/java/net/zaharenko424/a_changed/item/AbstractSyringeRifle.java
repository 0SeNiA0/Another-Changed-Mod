package net.zaharenko424.a_changed.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.entity.projectile.SyringeProjectile;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSyringeRifle extends Item implements MenuProvider {

    protected final float velocity;
    protected final int cooldown;

    public AbstractSyringeRifle(@NotNull Properties properties, float velocity, int cooldown) {
        super(properties);
        this.velocity = velocity;
        this.cooldown = cooldown;
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

        if(!player.isCreative()) {
            consumeFuel(rifle, handler);
            rifle.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }

        //shoot projectile (only take 1 at a time as stack inside of projectile is set to 1)
        SyringeProjectile syringe = new SyringeProjectile(level, player, useFirst(handler, player.isCreative()), rifle);
        syringe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, velocity, inaccuracy(player));
        level.addFreshEntity(syringe);

        playSound(level, player);

        player.getCooldowns().addCooldown(rifle.getItem(), cooldown);

        CriteriaTriggers.SHOT_CROSSBOW.trigger((ServerPlayer) player, rifle);

        return InteractionResultHolder.consume(rifle);
    }

    public abstract boolean hasAmmo(@NotNull IItemHandler handler);

    abstract ItemStack useFirst(@NotNull IItemHandler handler, boolean simulate);

    public abstract boolean hasFuel(ItemStack rifle, @NotNull IItemHandler inventory);

    abstract void consumeFuel(ItemStack rifle, IItemHandler handler);

    abstract float inaccuracy(Player player);

    abstract void playSound(Level level, Player player);

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }
}