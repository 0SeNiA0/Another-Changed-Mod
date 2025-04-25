package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class ProcessingMachine <IT extends ItemStackHandler, ET extends ExtendedEnergyStorage> extends AbstractMachineEntity<IT, ET> {

    protected boolean enabled = true;
    protected int progress;
    protected int energyConsumption;
    protected int recipeProcessingTime;

    protected int changeCounter;

    public ProcessingMachine(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public boolean isEnabled(){
        return enabled;
    }

    public int getProgress(){
        return progress;
    }

    public int getEnergyConsumption(){
        return energyConsumption;
    }

    public int getRecipeProcessingTime() {
        return recipeProcessingTime;
    }

    public abstract boolean hasRecipe();

    public void setData(int index, int data){
        if(index == 0) {
            enabled = data == 1;
            update();
        }
    }

    @Override
    protected void setActive(boolean active) {//Reset progress if not active
        if(!active) {
            progress = 0;
            if(!hasRecipe()) {
                energyConsumption = 0;
                recipeProcessingTime = 0;
            }
        }
        super.setActive(active);
    }

    protected void updateIfChanged(){
        if(changeCounter > 0) {
            super.update();
            changeCounter = 0;
        }
    }

    @Override
    protected void update() {
        super.update();
        changeCounter = 0;
    }

    protected void consumeEnergyFrom(@NotNull ItemStack item){
        if(item.isEmpty()) return;
        IEnergyStorage storage = item.getCapability(Capabilities.EnergyStorage.ITEM);
        if(storage == null) return;
        if(energyStorage.receiveEnergyFrom(storage, energyStorage.getMaxReceive(), false) > 0) changeCounter++;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.loadAdditional(tag, lookup);
        enabled = tag.getBoolean("enabled");
        if(enabled){
            progress = tag.getInt("progress");
            energyConsumption = tag.getInt("energyConsumption");
            recipeProcessingTime = tag.getInt("recipeProcessingTime");
        } else progress = 0;
    }

    @Override
    void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        super.save(tag, lookup);
        tag.putBoolean("enabled", enabled);
        if(enabled && progress > 0){
            tag.putInt("progress", progress);
            tag.putInt("energyConsumption", energyConsumption);
            tag.putInt("recipeProcessingTime", recipeProcessingTime);
        }
    }
}