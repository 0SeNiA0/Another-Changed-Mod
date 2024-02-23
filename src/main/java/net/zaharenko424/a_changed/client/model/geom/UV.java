package net.zaharenko424.a_changed.client.model.geom;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class UV {
    public final Object2ObjectArrayMap<Direction, UVData> uv = new Object2ObjectArrayMap<>();

    public UV(){}

    public UV(@NotNull ImmutableMap<Direction, UVData> data){
        uv.putAll(data);
    }

    public UV up(float u1,float v1, float u2, float v2){
        return up(new UVData(u1, v1, u2, v2));
    }

    public UV up(UVData uvData){
        uv.put(Direction.UP, uvData);
        return this;
    }

    public UV down(float u1,float v1, float u2, float v2){
        return down(new UVData(u1, v1, u2, v2));
    }

    public UV down(UVData uvData){
        uv.put(Direction.DOWN, uvData);
        return this;
    }

    public UV north(float u1,float v1, float u2, float v2){
        return north(new UVData(u1, v1, u2, v2));
    }

    public UV north(UVData uvData){
        uv.put(Direction.NORTH, uvData);
        return this;
    }

    public UV east(float u1,float v1, float u2, float v2){
        return east(new UVData(u1, v1, u2, v2));
    }

    public UV east(UVData uvData){
        uv.put(Direction.EAST, uvData);
        return this;
    }

    public UV south(float u1,float v1, float u2, float v2){
        return south(new UVData(u1, v1, u2, v2));
    }

    public UV south(UVData uvData){
        uv.put(Direction.SOUTH, uvData);
        return this;
    }

    public UV west(float u1,float v1, float u2, float v2){
        return west(new UVData(u1, v1, u2, v2));
    }

    public UV west(UVData uvData){
        uv.put(Direction.WEST, uvData);
        return this;
    }
}