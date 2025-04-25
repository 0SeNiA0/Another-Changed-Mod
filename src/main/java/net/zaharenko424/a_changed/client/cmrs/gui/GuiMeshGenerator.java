package net.zaharenko424.a_changed.client.cmrs.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.geom.Reusable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class GuiMeshGenerator {

    public static void genArcMesh(ImmutableList.Builder<ModelPart.VertexData> builder, List<ModelPart.Quad> quads, float radius, float thickness, float radSize, float radQuadStep){
        genArcMesh(builder, quads, radius, thickness, 0, radSize, radQuadStep);
    }

    public static void genArcMesh(ImmutableList.Builder<ModelPart.VertexData> builder, List<ModelPart.Quad> quads, float radius, float thickness, float radOffset, float radSize, float radQuadStep){
        genArcMesh(builder, quads, Reusable.VEC3F.get().set(0), radius, thickness, radOffset, radSize, radQuadStep);
    }

    public static void genArcMesh(ImmutableList.Builder<ModelPart.VertexData> builder, List<ModelPart.Quad> quads, Vector3f offset, float radius, float thickness, float radOffset, float radSize, float radQuadStep){
        float x, y, x1, y1;
        Vector3f vec;
        boolean started = false, finish = false;
        ModelPart.Vertex[] arr = null;
        ModelPart.VertexData last1, last2;
        float radMax = radSize + radOffset;
        for(float rad = radOffset;; rad += radQuadStep){
            if(rad > radMax) {
                rad = radMax;
                finish = true;
            }

            x = Mth.cos(rad) * radius;
            y = (float) (Math.sin(rad) * radius);
            vec = new Vector3f(x * 16, y * 16, 0);
            offset.mulAdd(16, vec, vec);
            last1 = new ModelPart.VertexData(vec, new ModelPart.Quad[0]);
            builder.add(last1);

            x1 = Mth.cos(rad) * (radius + thickness);
            y1 = (float) (Math.sin(rad) * (radius + thickness));
            vec = new Vector3f(x1 * 16, y1 * 16, 0);
            offset.mulAdd(16, vec, vec);
            last2 = new ModelPart.VertexData(vec, new ModelPart.Quad[0]);
            builder.add(last2);

            if(!started){
                arr = new ModelPart.Vertex[4];
                arr[0] = new ModelPart.Vertex(last2);
                arr[1] = new ModelPart.Vertex(last1);
                started = true;
            } else {
                arr[2] = new ModelPart.Vertex(last1);
                arr[3] = new ModelPart.Vertex(last2);
                quads.add(new ModelPart.Quad(arr));
                if(finish) break;

                arr = new ModelPart.Vertex[4];
                arr[0] = new ModelPart.Vertex(last2);
                arr[1] = new ModelPart.Vertex(last1);
            }
        }
    }

    public static void genRay(ImmutableList.Builder<ModelPart.VertexData> builder, List<ModelPart.Quad> quads, float rayRad, float offset, float length, float outlineThickness, boolean flip){
        ModelPart.VertexData[] arr = new ModelPart.VertexData[4];

        float x, y, o = flip ? -1 : 1;

        x = Mth.cos(rayRad) * (length - outlineThickness);//Outer left
        y = Mth.sin(rayRad) * (length - outlineThickness);
        arr[0] = new ModelPart.VertexData(new Vector3f(x * 16, y * 16, 0), new ModelPart.Quad[0]);

        x = Mth.cos(rayRad + Mth.HALF_PI)* o * outlineThickness + x;//Outer right
        y = Mth.sin(rayRad + Mth.HALF_PI)* o * outlineThickness + y;
        arr[1] = new ModelPart.VertexData(new Vector3f(x * 16, y * 16, 0), new ModelPart.Quad[0]);

        x = Mth.cos(rayRad) * (offset + outlineThickness * .9f);//Inner left
        y = Mth.sin(rayRad) * (offset + outlineThickness * .9f);
        arr[3] = new ModelPart.VertexData(new Vector3f(x * 16, y * 16, 0), new ModelPart.Quad[0]);

        x = Mth.cos(rayRad + Mth.HALF_PI) * o * outlineThickness  * .9f + x;//Inner right
        y = Mth.sin(rayRad + Mth.HALF_PI) * o * outlineThickness  * .9f + y;
        arr[2] = new ModelPart.VertexData(new Vector3f(x * 16, y * 16, 0), new ModelPart.Quad[0]);

        builder.add(arr);
        if(!flip) {
            quads.add(new ModelPart.Quad(new ModelPart.Vertex[]{new ModelPart.Vertex(arr[3]), new ModelPart.Vertex(arr[2]), new ModelPart.Vertex(arr[1]), new ModelPart.Vertex(arr[0])}));
        } else quads.add(new ModelPart.Quad(new ModelPart.Vertex[]{new ModelPart.Vertex(arr[0]), new ModelPart.Vertex(arr[1]), new ModelPart.Vertex(arr[2]), new ModelPart.Vertex(arr[3])}));
    }

    public static void genQuad(ImmutableList.Builder<ModelPart.VertexData> builder, List<ModelPart.Quad> quads, Vector3f vert0, Vector3f vert1, Vector3f vert2, Vector3f vert3){
        ModelPart.VertexData[] arr = new ModelPart.VertexData[4];
        arr[0] = new ModelPart.VertexData(vert0.mul(16), new ModelPart.Quad[0]);
        arr[1] = new ModelPart.VertexData(vert1.mul(16), new ModelPart.Quad[0]);
        arr[2] = new ModelPart.VertexData(vert2.mul(16), new ModelPart.Quad[0]);
        arr[3] = new ModelPart.VertexData(vert3.mul(16), new ModelPart.Quad[0]);

        builder.add(arr);
        quads.add(new ModelPart.Quad(new ModelPart.Vertex[]{new ModelPart.Vertex(arr[3]), new ModelPart.Vertex(arr[2]),new ModelPart.Vertex(arr[1]),new ModelPart.Vertex(arr[0])}));
    }
}
