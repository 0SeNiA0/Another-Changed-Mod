package net.zaharenko424.a_changed.entity.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class ProcessingMachine <IT extends ItemStackHandler, ET extends ExtendedEnergyStorage> extends AbstractMachineEntity<IT, ET> {

    protected boolean enabled = true;
    protected int progress;
    protected int energyConsumption;
    protected int recipeProcessingTime;

    protected final int pullEnergyFromSlot;
    private boolean inventoryChangeRequired = false;
    private boolean energyChangeRequired = false;

    public ProcessingMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, -1);
    }

    public ProcessingMachine(BlockEntityType<?> type, BlockPos pos, BlockState state, int pullEnergyFromSlot) {
        super(type, pos, state);
        this.pullEnergyFromSlot = pullEnergyFromSlot;
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
    protected boolean setActive(boolean active) {//Reset progress if not active
        boolean changed = super.setActive(active);
        if(changed && !active){
            progress = 0;
            if(!hasRecipe()) {
                energyConsumption = 0;
                recipeProcessingTime = 0;
            }
            changeCounter++;
        }

        return changed;
    }

    protected void inventoryChanged(){
        inventoryChangeRequired = false;
        changeCounter++;
    }

    protected void awaitInventoryChanges(){
        inventoryChangeRequired = true;
    }

    protected void energyLevelChanged(){
        energyChangeRequired = false;
        changeCounter++;
    }

    protected void awaitEnergyChanges(){
        energyChangeRequired = true;
    }

    @Override
    public void tick() {
        if(pullEnergyFromSlot != -1) consumeEnergyFrom(inventory.getStackInSlot(pullEnergyFromSlot));

        if(enabled) {
            if (!energyChangeRequired && !inventoryChangeRequired) machineTick();
        } else setActive(false);

        updateIfChanged();
    }

    protected abstract void machineTick();

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
        if(enabled){
            if(progress > 0) tag.putInt("progress", progress);
            tag.putInt("energyConsumption", energyConsumption);
            tag.putInt("recipeProcessingTime", recipeProcessingTime);
        }
    }
}