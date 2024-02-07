package net.zaharenko424.a_changed.capability;

import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ExtendedEnergyStorage extends EnergyStorage {

    public ExtendedEnergyStorage(int capacity) {
        super(capacity);
    }

    public ExtendedEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public boolean isEmpty(){
        return  energy == 0;
    }

    public boolean isFull(){
        return energy == capacity;
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
        if(isEmpty() || !canExtract() || !receiver.canReceive()) return 0;
        int canGive = extractEnergy(amount, true);
        int canReceive = receiver.receiveEnergy(amount, true);
        int toSend = Math.min(canGive, canReceive);
        if(simulate) return toSend;
        extractEnergy(toSend, false);
        receiver.receiveEnergy(toSend, false);
        return toSend;
    }
}