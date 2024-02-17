package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public class WireNetworkCache {

    private final HashMap<Pair<IEnergyStorage, BlockEntity>, Integer> consumers = new HashMap<>();
    private final HashMap<Pair<IEnergyStorage, BlockEntity>, Integer> providers = new HashMap<>();

    private final HashSet<AbstractProxyWire> visitedWires = new HashSet<>();

    private int totalConsume, totalProvide;

    private long cachedTick;

    public void addConsumer(@NotNull IEnergyStorage storage, @NotNull BlockEntity entity){
        int i = storage.receiveEnergy(Integer.MAX_VALUE, true);
        consumers.put(Pair.of(storage, entity), i);
        totalConsume += i;
    }

    public HashMap<Pair<IEnergyStorage, BlockEntity>, Integer> getConsumersMapped(){
        return consumers;
    }

    public int getTotalConsume(){
        return totalConsume;
    }

    public void addProvider(@NotNull IEnergyStorage storage, @NotNull BlockEntity entity){
        int i = storage.extractEnergy(Integer.MAX_VALUE, true);
        providers.put(Pair.of(storage, entity), i);
        totalProvide += i;
    }

    public HashMap<Pair<IEnergyStorage, BlockEntity>, Integer> getProvidersMapped(){
        return providers;
    }

    public int getTotalProvide(){
        return totalProvide;
    }

    public boolean isVisited(AbstractProxyWire wire){
        return visitedWires.contains(wire);
    }

    public void addVisited(AbstractProxyWire wire){
        visitedWires.add(wire);
    }

    public boolean isValid(long currentTick){
        return cachedTick == currentTick;
    }

    public boolean isWorthTicking(){
        return !consumers.isEmpty() && !providers.isEmpty();
    }

    public void update(long tick){
        if(cachedTick == tick) return;
        cachedTick = tick;
        consumers.clear();
        providers.clear();
        totalConsume = 0;
        totalProvide = 0;
        visitedWires.clear();
    }
}