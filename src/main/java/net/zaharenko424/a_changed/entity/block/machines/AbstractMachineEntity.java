package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zaharenko424.a_changed.util.StateProperties.ACTIVE;

public abstract class AbstractMachineEntity <IT extends ItemStackHandler, ET extends ExtendedEnergyStorage> extends BlockEntity implements MenuProvider {

    protected final IT inventory;
    protected final ET energyStorage;

    public AbstractMachineEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.inventory = initInv();
        energyStorage = initEnergy();
    }

    abstract IT initInv();

    abstract ET initEnergy();

    public int getEnergy(){
        return energyStorage.getEnergyStored();
    }

    public int getCapacity(){
        return energyStorage.getMaxEnergyStored();
    }

    public IT getInventory(){
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

    public static boolean checkItemEnergyCap(@NotNull ItemStack stack){
        return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookup) {
        CompoundTag tag = new CompoundTag();
        save(tag, lookup);
        return tag;
    }

    /*
    *    handleUpdateTag is a scam. override is ignored
    */

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        inventory.deserializeNBT(lookup, tag.getCompound("inventory"));
        energyStorage.deserializeNBT(lookup, tag.get("energyStorage"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.saveAdditional(tag, lookup);
        save(tag, lookup);
    }

    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup){
        tag.put("inventory", inventory.serializeNBT(lookup));
        tag.put("energyStorage", energyStorage.serializeNBT(lookup));
    }

    public @Nullable <CT> CT getCapability(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        if(cap == Capabilities.ItemHandler.BLOCK){
            return getItemCap(cap, side);
        }
        if(cap == Capabilities.EnergyStorage.BLOCK){
            return getEnergyCap(cap, side);
        }
        return null;
    }

    protected <CT> CT getItemCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side){
        return (CT) inventory;
    }

    protected <CT> CT getEnergyCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side){
        return (CT) energyStorage;
    }
}