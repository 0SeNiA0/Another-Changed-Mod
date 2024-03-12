package net.zaharenko424.a_changed.client.cmrs.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GeomUtil {

    @Contract(" -> new")
    public static @NotNull QuadBuilder quad(){
        return new QuadBuilder();
    }

    public static class QuadBuilder {

        private final ModelPart.Vertex[] vertices = new ModelPart.Vertex[4];
        private int i = 0;
        private UVData uv;

        private QuadBuilder(){}

        public QuadBuilder vertex(float x, float y, float z){
            vertices[i++] = new ModelPart.Vertex(x, y, z);
            return this;
        }

        public QuadBuilder uv(float u1, float v1, float u2, float v2){
            uv = new UVData(u1, v1, u2, v2);
            return this;
        }

        public ModelPart.Quad build(float textureWidth, float textureHeight){
            return new ModelPart.Quad(vertices, uv, textureWidth, textureHeight);
        }
    }
}