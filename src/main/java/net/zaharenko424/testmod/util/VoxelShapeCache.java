package net.zaharenko424.testmod.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class VoxelShapeCache {
    private final HashMap<Pair<Direction,Integer>, VoxelShape> shapes=new HashMap<>();
    public VoxelShape getShape(Direction direction, int i, VoxelShape baseShape){
        return shapes.computeIfAbsent(Pair.of(direction,i),pair -> direction!=Direction.NORTH?Utils.rotateShape(direction, baseShape):baseShape);
    }
}