package net.zaharenko424.a_changed.capability;

public class EnergyGenerator extends ExtendedEnergyStorage {

    public EnergyGenerator(int capacity) {
        super(capacity);
    }

    public EnergyGenerator(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyGenerator(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public EnergyGenerator(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    /**
     * Adds ("generates") specified amount of energy
     * @param amount amount of energy to generate
     */
    public void generateEnergy(int amount){
        energy += Math.min(capacity - energy, amount);
    }
}