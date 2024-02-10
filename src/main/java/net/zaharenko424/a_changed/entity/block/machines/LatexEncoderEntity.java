package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.EnergyConsumer;
import org.jetbrains.annotations.Nullable;

public class LatexEncoderEntity extends AbstractMachineEntity<ItemStackHandler, EnergyConsumer> {

    public LatexEncoderEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState, new ItemStackHandler(8), new EnergyConsumer(50000, 256, 0));
    }

    @Override
    public void tick() {

    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }
}