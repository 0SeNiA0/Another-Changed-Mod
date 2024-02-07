package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.ExtendedEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zaharenko424.a_changed.util.StateProperties.ACTIVE;

public abstract class AbstractMachineEntity <T extends ExtendedEnergyStorage> extends BlockEntity implements MenuProvider {

    protected final ItemStackHandler inventory;
    protected final LazyOptional<ItemStackHandler> invOptional;

    protected final T energyStorage;
    protected final LazyOptional<T> energyOptional;

    public AbstractMachineEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, ItemStackHandler inventory, T storage) {
        super(pType, pPos, pBlockState);
        this.inventory = inventory;
        invOptional = LazyOptional.of(()->inventory);
        energyStorage = storage;
        energyOptional = LazyOptional.of(()->energyStorage);
    }

    public int getEnergy(){
        return energyStorage.getEnergyStored();
    }

    public int getCapacity(){
        return energyStorage.getMaxEnergyStored();
    }

    public ItemStackHandler getInventory(){
        return inventory;
    }

    public abstract void tick();

    protected void setActive(boolean active){
        if(getBlockState().getValue(ACTIVE) != active)
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, active));
    }

    protected void update(){
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public void onRemove(){
        for(int i = 0; i < inventory.getSlots(); i++){
            Block.popResource(level, worldPosition, inventory.extractItem(i, inventory.getSlotLimit(i), false));
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        save(tag);
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
        energyStorage.deserializeNBT(tag.get("energyStorage"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        save(tag);
    }

    void save(@NotNull CompoundTag tag){
        tag.put("inventory", inventory.serializeNBT());
        tag.put("energyStorage", energyStorage.serializeNBT());
    }

    @Override
    public @NotNull <CT> LazyOptional<CT> getCapability(@NotNull Capability<CT> cap, @Nullable Direction side) {
        if(cap == Capabilities.ITEM_HANDLER){
            return invOptional.cast();
        }
        if(cap == Capabilities.ENERGY){
            return energyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        invOptional.invalidate();
        energyOptional.invalidate();
    }
}