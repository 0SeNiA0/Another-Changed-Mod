package net.zaharenko424.a_changed.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EmptyEnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerCell extends Item {

    public PowerCell(@NotNull Properties pProperties) {
        super(pProperties.durability(100).setNoRepair());
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        IEnergyStorage storage = pStack.getCapability(Capabilities.ENERGY).orElse(EmptyEnergyStorage.INSTANCE);
        tooltip.add(Component.literal("EU: "+ Utils.formatEnergy(storage.getEnergyStored())+"/"+Utils.formatEnergy(storage.getMaxEnergyStored())));
    }
}