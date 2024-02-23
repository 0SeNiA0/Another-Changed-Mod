package net.zaharenko424.a_changed.client.model.geom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class GroupBuilder {
    private final List<CubeDefinition> cubes = new ArrayList<>();
    private final boolean mirror = false;

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, ImmutableMap<Direction, UVData> uv){
        return addBox(x1, y1, z1, x2, y2, z2, inflate, mirror, new UV(uv));
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, UV uv){
        return addBox(x1, y1, z1, x2, y2, z2, new Vector3f(), mirror, uv);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, UV uv){
        return addBox(x1, y1, z1, x2, y2, z2, inflate, mirror, uv);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, boolean mirror, UV uv){
        cubes.add(new CubeDefinition(x1, y1, z1, x2, y2, z2, inflate, mirror, uv));
        return this;
    }

    public List<CubeDefinition> cubes(){
        return cubes;
    }

    @Contract(" -> new")
    public static @NotNull GroupBuilder create(){
        return new GroupBuilder();
    }
}