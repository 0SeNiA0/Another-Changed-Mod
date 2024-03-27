package net.zaharenko424.a_changed.capability.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyStorageWrapper implements IEnergyStorage {

    private final IEnergyStorage storage;
    private final int maxReceive;
    private final int maxExtract;

    public EnergyStorageWrapper(IEnergyStorage storage, int maxReceive, int maxExtract) {
        this.storage = storage;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }


    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(!canReceive()) return 0;
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(!canExtract()) return 0;
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0 && storage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0 && storage.canReceive();
    }
}