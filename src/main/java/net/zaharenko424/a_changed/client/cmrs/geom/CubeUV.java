package net.zaharenko424.a_changed.client.cmrs.geom;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class CubeUV {

    final Object2ObjectArrayMap<Direction, UVData> uv = new Object2ObjectArrayMap<>();

    public CubeUV(){}

    public CubeUV(@NotNull ImmutableMap<Direction, UVData> data){
        uv.putAll(data);
    }

    public CubeUV up(float u1, float v1, float u2, float v2){
        return up(new UVData(u1, v1, u2, v2));
    }

    public CubeUV up(UVData uvData){
        uv.put(Direction.UP, uvData);
        return this;
    }

    public CubeUV down(float u1, float v1, float u2, float v2){
        return down(new UVData(u1, v1, u2, v2));
    }

    public CubeUV down(UVData uvData){
        uv.put(Direction.DOWN, uvData);
        return this;
    }

    public CubeUV north(float u1, float v1, float u2, float v2){
        return north(new UVData(u1, v1, u2, v2));
    }

    public CubeUV north(UVData uvData){
        uv.put(Direction.NORTH, uvData);
        return this;
    }

    public CubeUV east(float u1, float v1, float u2, float v2){
        return east(new UVData(u1, v1, u2, v2));
    }

    public CubeUV east(UVData uvData){
        uv.put(Direction.EAST, uvData);
        return this;
    }

    public CubeUV south(float u1, float v1, float u2, float v2){
        return south(new UVData(u1, v1, u2, v2));
    }

    public CubeUV south(UVData uvData){
        uv.put(Direction.SOUTH, uvData);
        return this;
    }

    public CubeUV west(float u1, float v1, float u2, float v2){
        return west(new UVData(u1, v1, u2, v2));
    }

    public CubeUV west(UVData uvData){
        uv.put(Direction.WEST, uvData);
        return this;
    }
}