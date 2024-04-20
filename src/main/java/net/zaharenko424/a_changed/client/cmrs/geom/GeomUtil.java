package net.zaharenko424.a_changed.client.cmrs.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public interface GeomUtil {

    @Contract(value = " -> new", pure = true)
    static @NotNull TriangleBuilder triangle(){
        return new TriangleBuilder();
    }

    @Contract(" -> new")
    static @NotNull QuadBuilder quad(){
        return new QuadBuilder();
    }

    class TriangleBuilder {

        private final ModelPart.Vertex[] vertices = new ModelPart.Vertex[3];
        private int i = 0;
        private UVData uv;

        private TriangleBuilder(){}

        public TriangleBuilder vertex(float x, float y, float z){
            if(i < 3) vertices[i++] = new ModelPart.Vertex(x, y, z);
            return this;
        }

        public TriangleBuilder uv(float u1, float v1, float u2, float v2){
            uv = new UVData(u1, v1, u2, v2);
            return this;
        }

        public ModelPart.Triangle build(float textureWidth, float textureHeight){
            if(i < 2) throw new IllegalStateException("Not enough vertices specified! " + (i + 1) + "/3");
            return new ModelPart.Triangle(vertices, uv, textureWidth, textureHeight);
        }
    }

    class QuadBuilder {

        private final ModelPart.Vertex[] vertices = new ModelPart.Vertex[4];
        private int i = 0;
        private UVData uv;

        private QuadBuilder(){}

        public QuadBuilder vertex(float x, float y, float z){
            return vertex(new Vector3f(x, y, z));
        }

        public QuadBuilder vertex(Vector3f pos){
            if(i < 4) vertices[i++] = new ModelPart.Vertex(pos);
            return this;
        }

        public QuadBuilder uv(float u1, float v1, float u2, float v2){
            uv = new UVData(u1, v1, u2, v2);
            return this;
        }

        public ModelPart.Quad build(float textureWidth, float textureHeight){
            if(i < 4) throw new IllegalStateException("Not enough vertices specified! " + (i + 1) + "/4");
            return new ModelPart.Quad(vertices, uv, textureWidth, textureHeight);
        }
    }
}