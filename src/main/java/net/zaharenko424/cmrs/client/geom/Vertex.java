package net.zaharenko424.cmrs.client.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Supplier;

public record Vertex(Vector3f pos, Supplier<VertexData> dataSupplier, float u, float v) {

    public Vertex(VertexData data) {
        this(data.pos(), () -> data, 0, 0);
    }

    public Vertex(VertexData data, float u, float v) {
        this(data.pos(), () -> data, u, v);
    }

    public VertexData data() {
        return dataSupplier.get();
    }

    @Contract("_, _ -> new")
    public @NotNull Vertex remap(float u, float v) {
        return new Vertex(pos, dataSupplier, u, v);
    }
}
