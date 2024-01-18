package net.zaharenko424.a_changed.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.function.Supplier;

public class VoxelShapeCache {
    private final HashMap<Pair<Direction, Integer>, VoxelShape> shapes = new HashMap<>();

    public VoxelShape getShape(Direction direction, int id, VoxelShape baseShape){
        return shapes.computeIfAbsent(Pair.of(direction, id), pair ->
                direction != Direction.NORTH ? Utils.rotateShape(direction, baseShape) : baseShape);
    }

    public VoxelShape getShape(Direction direction, int id, Supplier<VoxelShape> supplier){
        Pair<Direction, Integer> pair = Pair.of(direction, id);
        return shapes.containsKey(pair) ? shapes.get(pair) : getShape(direction, id, supplier.get());
    }
}