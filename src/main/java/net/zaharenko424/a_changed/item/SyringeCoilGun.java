package net.zaharenko424.a_changed.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.capability.energy.EnergyConsumer;
import net.zaharenko424.a_changed.menu.SyringeCoilGunMenu;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SyringeCoilGun extends AbstractSyringeRifle {

    public SyringeCoilGun() {
        super(new Properties().rarity(Rarity.RARE).durability(100));
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    boolean hasAmmo(@NotNull IItemHandler handler) {
        for(int i = 0; i < 4; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    @Override
    AbstractTransfurType useFirst(@NotNull IItemHandler handler, boolean simulate) {
        for(int i = 0; i < 4; i++){
            if(!handler.getStackInSlot(i).isEmpty()) return TransfurManager.getTransfurType(
                    Objects.requireNonNull(LatexSyringeItem.decodeTransfur(handler.extractItem(i, 1, simulate))));
        }
        return null;
    }

    @Override
    boolean hasFuel(@NotNull ItemStack rifle, @NotNull IItemHandler inventory) {
        return rifle.getCapability(Capabilities.EnergyStorage.ITEM).getEnergyStored() >= 2000;
    }

    @Override
    void consumeFuel(@NotNull ItemStack rifle, @NotNull IItemHandler handler) {
        ((EnergyConsumer)rifle.getCapability(Capabilities.EnergyStorage.ITEM)).consumeEnergy(2000);
    }

    @Override
    int velocity() {
        return 5;
    }

    @Override
    float accuracy() {
        return 1;
    }

    @Override
    void playSound(Level level, Player player) {}

    @Override
    int cooldown() {
        return 10;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
        IItemHandler inventory = stack.getCapability(Capabilities.ItemHandler.ITEM);

        tooltip.add(Component.translatable("tooltip.a_changed.syringe_rifle_energy",
                stack.isEmpty() || stack.getCapability(Capabilities.EnergyStorage.ITEM) == null ? 0
                        : Mth.floorDiv(stack.getCapability(Capabilities.EnergyStorage.ITEM).getEnergyStored(), 2000)).withStyle(ChatFormatting.GREEN));

        int count = 0;
        for(int i = 0; i < 4; i++){
            if(!inventory.getStackInSlot(i).isEmpty()) count++;
        }
        tooltip.add(Component.translatable("tooltip.a_changed.syringe_rifle_shots", count).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.empty());

        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        tooltip.add(Component.literal("EU: "+ Utils.formatEnergy(storage.getEnergyStored()) + "/" + Utils.formatEnergy(storage.getMaxEnergyStored())).withStyle(ChatFormatting.DARK_GREEN));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new SyringeCoilGunMenu(pContainerId, pPlayerInventory, pPlayer.getMainHandItem());
    }
}