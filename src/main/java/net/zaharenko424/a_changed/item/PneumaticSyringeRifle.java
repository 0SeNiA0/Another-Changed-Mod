package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.menu.PneumaticSyringeRifleMenu;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PneumaticSyringeRifle extends AbstractSyringeRifle {

    public PneumaticSyringeRifle() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1), 3, 20);
    }

    @Override
    public boolean hasAmmo(@NotNull IItemHandler handler) {
        for(int i = 1; i < 9; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    @Override
    ItemStack useFirst(@NotNull IItemHandler handler, boolean simulate) {
        for(int i = 1; i < 9; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return simulate ? handler.extractItem(i, 1, true).copy()
                    : handler.extractItem(i, 1, false);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasFuel(@NotNull ItemStack rifle, @NotNull IItemHandler inventory) {
        ItemStack canister = inventory.getStackInSlot(0);
        return !canister.isEmpty() && canister.is(ItemRegistry.COMPRESSED_AIR_CANISTER);
    }

    @Override
    void consumeFuel(ItemStack rifle, @NotNull IItemHandler handler) {
        handler.insertItem(0,
                CompressedAirCanister.consumeAir(handler.extractItem(0, 1, false)), false);
    }

    @Override
    float inaccuracy(@NotNull Player player) {
        return 1.2f;
    }

    @Override
    void playSound(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.blockPosition(), SoundRegistry.PNEUMATIC_RIFLE.get(), SoundSource.PLAYERS, .6f, .6f);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        IItemHandler inventory = stack.getCapability(Capabilities.ItemHandler.ITEM);
        ItemStack canister = inventory.getStackInSlot(0);

        tooltipComponents.add(Component.translatable("tooltip.a_changed.syringe_rifle.air",
                canister.isEmpty() || !(canister.getItem() instanceof CompressedAirCanister) ? 0
                        : canister.getMaxDamage() - canister.getDamageValue()).withStyle(ChatFormatting.GRAY));

        int count = 0;
        for(int i = 1; i < 9; i++){
            if(!inventory.getStackInSlot(i).isEmpty()) count++;
        }
        tooltipComponents.add(Component.translatable("tooltip.a_changed.syringe_rifle.shots", count).withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new PneumaticSyringeRifleMenu(containerId, playerInventory, player.getMainHandItem());
    }
}