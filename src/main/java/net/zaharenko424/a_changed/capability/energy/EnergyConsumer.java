package net.zaharenko424.a_changed.capability.energy;

public class EnergyConsumer extends ExtendedEnergyStorage {

    public EnergyConsumer(int capacity) {
        super(capacity);
    }

    public EnergyConsumer(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyConsumer(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public EnergyConsumer(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    /**
     * Consumes specified amount of energy
     * @param amount amount of energy to consume
     */
    public void consumeEnergy(int amount){
        if(amount < 0) return;
        energy -= Math.min(energy, amount);
    }
}