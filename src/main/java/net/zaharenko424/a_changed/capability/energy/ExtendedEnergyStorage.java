package net.zaharenko424.a_changed.capability.energy;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class ExtendedEnergyStorage implements IEnergyStorage, INBTSerializable<Tag> {

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public ExtendedEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public ExtendedEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    public boolean isEmpty(){
        return energy == 0;
    }

    public boolean isFull(){
        return energy >= capacity;
    }

    public int getMaxReceive(){
        return maxReceive;
    }

    public int getMaxExtract(){
        return maxExtract;
    }

    /**
     * Only used to override maxReceive/maxExtract checks
     * @param amount amount of energy to add
     */
    public void addEnergy(int amount){
        setEnergy(energy + amount);
    }

    protected void setEnergy(int amount){
        energy = amount;
        onEnergyChanged();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive < 0 || !canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if(simulate) return energyReceived;

        setEnergy(energy + energyReceived);
        return energyReceived;
    }

    /**
     * Receives energy from provided IEnergyStorage
     * @param sender Storage to receive energy from
     * @param amount Maximum amount of energy to be sent.
     * @param simulate If TRUE, the insertion will only be simulated.
     * @return Amount of energy that was (or would have been, if simulated) transferred from provided storage to this.
     */
    public int receiveEnergyFrom(IEnergyStorage sender, int amount, boolean simulate){
        if(amount < 0 || isFull() || !canReceive() || !sender.canExtract()) return 0;
        int canGive = sender.extractEnergy(amount, true);
        int canReceive = receiveEnergy(amount, true);
        int toSend = Math.min(canGive, canReceive);
        if(simulate) return toSend;
        sender.extractEnergy(toSend, false);
        receiveEnergy(toSend, false);
        return toSend;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract < 0 || !canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if(simulate) return energyExtracted;

        setEnergy(energy - energyExtracted);
        return energyExtracted;
    }

    /**
     * Sends energy to provided IEnergyStorage.
     * @param receiver Storage to send energy to.
     * @param amount Maximum amount of energy to be sent.
     * @param simulate If TRUE, the insertion will only be simulated.
     * @return
    Amount of energy that was (or would have been, if simulated) transferred from this storage to provided one.
     */
    public int transferEnergyTo(IEnergyStorage receiver, int amount, boolean simulate){
        if(amount < 0 || isEmpty() || !canExtract() || !receiver.canReceive()) return 0;
        int canGive = extractEnergy(amount, true);
        int canReceive = receiver.receiveEnergy(amount, true);
        int toSend = Math.min(canGive, canReceive);
        if(simulate) return toSend;
        extractEnergy(toSend, false);
        receiver.receiveEnergy(toSend, false);
        return toSend;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    public void onEnergyChanged(){}

    @Override
    public Tag serializeNBT(HolderLookup.@NotNull Provider lookup) {
        return IntTag.valueOf(getEnergyStored());
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider lookup, @NotNull Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Tag, provided for deserialization, is not an IntTag!");
        energy = intNbt.getAsInt();
    }
}