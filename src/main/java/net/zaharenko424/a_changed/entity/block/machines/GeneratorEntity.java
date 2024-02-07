package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.EmptyEnergyStorage;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.block.blocks.Generator;
import net.zaharenko424.a_changed.capability.EnergyGenerator;
import net.zaharenko424.a_changed.menu.GeneratorMenu;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(2);
    private final LazyOptional<ItemStackHandler> invOptional = LazyOptional.of(()-> inventory);

    private final EnergyGenerator energyGenerator = new EnergyGenerator(10000, 0, 100, 0);
    private final LazyOptional<EnergyStorage> energyOptional = LazyOptional.of(()-> energyGenerator);

    private int burnTicks;
    private int maxBurnTicks;

    public GeneratorEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.GENERATOR_ENTITY.get(), pPos, pBlockState);
    }

    public boolean isEmpty(){
        return energyGenerator.isEmpty();
    }

    public int getEnergy(){
        return energyGenerator.getEnergyStored();
    }

    public int getCapacity(){
        return energyGenerator.getMaxEnergyStored();
    }

    public ItemStackHandler getInventory(){
        return inventory;
    }

    public int getBurnTicks(){
        return burnTicks;
    }

    public int getMaxBurnTicks(){
        return maxBurnTicks;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new GeneratorMenu(pContainerId, inventory, this);
    }

    public void tick(){
        boolean changed = false;
        BlockState state = getBlockState();
        if(burnTicks > 0){
            burnTicks--;
            if(!energyGenerator.isFull()) energyGenerator.generateEnergy(15);
            changed = true;
        } else if(!energyGenerator.isFull()) {
            int burnTime = CommonHooks.getBurnTime(inventory.getStackInSlot(0), null);
            if(burnTime > 0) {
                if(!state.getValue(Generator.LIT)) setLit(true);
                burnTicks = burnTime;
                maxBurnTicks = burnTime;
                inventory.extractItem(0, 1, false);
                changed = true;
            } else {
                if(state.getValue(Generator.LIT)) setLit(false);
            }
        } else {
            if(state.getValue(Generator.LIT)) setLit(false);
        }

        if(!energyGenerator.isEmpty() && !inventory.getStackInSlot(1).isEmpty()){
            energyGenerator.transferEnergyTo(inventory.getStackInSlot(1).getCapability(Capabilities.ENERGY).orElse(EmptyEnergyStorage.INSTANCE), 100, false);
            changed = true;
        }

        BlockEntity entity;
        for(Direction direction : Direction.values()){
            if(energyGenerator.isEmpty()) break;
            entity = level.getBlockEntity(worldPosition.relative(direction));
            if(entity == null) continue;
            entity.getCapability(Capabilities.ENERGY, direction.getOpposite()).ifPresent(storage ->
                    energyGenerator.transferEnergyTo(storage, 100, false));
            changed = true;
        }

        if(changed) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private void setLit(boolean lit){
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(Generator.LIT, lit));
    }

    public void remove(){
        Block.popResource(level, worldPosition, inventory.getStackInSlot(0));
        Block.popResource(level, worldPosition, inventory.getStackInSlot(1));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.generator");
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
        energyGenerator.deserializeNBT(tag.get("energyStorage"));
        if(tag.contains("burnTicks")){
            burnTicks = tag.getInt("burnTicks");
            maxBurnTicks = tag.getInt("maxBurnTicks");
        } else {
            burnTicks = 0;
            maxBurnTicks = 0;
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        save(tag);
    }

    void save(@NotNull CompoundTag tag){
        tag.put("inventory", inventory.serializeNBT());
        tag.put("energyStorage", energyGenerator.serializeNBT());
        if(burnTicks > 0) {
            tag.putInt("burnTicks", burnTicks);
            tag.putInt("maxBurnTicks", maxBurnTicks);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
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