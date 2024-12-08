package net.zaharenko424.a_changed.client.cmrs.model;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.client.cmrs.RemappingVertexConsumer;
import net.zaharenko424.a_changed.client.cmrs.properties.Texture;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.util.Pool;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class RenderStack {

    protected final Int2ObjectOpenHashMap<ParameterList> map = new Int2ObjectOpenHashMap<>(2);
    protected static final Pool<RenderParameters> paramPool = new Pool<>() {
        @Override
        protected RenderParameters newObject() {
            return new RenderParameters();
        }
    };
    protected static final Pool<RemappingVertexConsumer> remapperPool = new Pool<>() {
        @Override
        protected RemappingVertexConsumer newObject() {
            return new RemappingVertexConsumer(null, 0, 0);
        }
    };
    protected boolean remap;
    protected Function<ResourceLocation, RenderType> func;

    public ParameterList getOrCreate(int renderId){
        return map.computeIfAbsent(renderId, k -> new ParameterList());
    }

    public void reset(){
        map.values().forEach(ParameterList::clear);
    }

    public void setRemap(boolean remap){
        this.remap = remap;
    }

    public void setRenderTypeFunc(Function<ResourceLocation, RenderType> func){
        this.func = func;
    }

    public RenderType defRenderType(ResourceLocation texture, @NotNull Function<ResourceLocation, RenderType> fallback){
        return func != null ? func.apply(texture) : fallback.apply(texture);
    }

    public void clear(){
        map.clear();
    }

    public void renderMesh(@NotNull ModelPart.Mesh mesh, PoseStack.Pose pose, int light, int overlay, int color){
        ParameterList parameters = map.get(mesh.renderId);
        if(parameters == null || parameters.list.isEmpty()) return;

        for(RenderParameters param : parameters){
            mesh.compile(pose, param.consumer, light, param.overlay != 0 ? param.overlay : overlay, param.color != 0 ? param.color : color);
        }
    }

    public class ParameterList implements Iterable<RenderParameters> {

        private final List<RenderParameters> list = new ArrayList<>(2);

        public RenderParameters add(){
            RenderParameters param = paramPool.obtain();
            param.init(RenderStack.this);
            list.add(param);
            return param;
        }

        public void clear(){
            for(RenderParameters param : list) paramPool.free(param);
            list.clear();
        }

        @Override
        public @NotNull Iterator<RenderParameters> iterator() {
            return list.iterator();
        }
    }

    public static class RenderParameters implements Pool.Poolable {

        private VertexConsumer consumer;
        private int overlay;
        private int color;

        private boolean remap;

        void init(RenderStack stack){
            remap = stack.remap;
        }

        public RenderParameters set(@NotNull VertexConsumer consumer){
            this.consumer = consumer;
            return this;
        }

        @CanIgnoreReturnValue
        public RenderParameters setUVRemapped(@NotNull VertexConsumer consumer, int textureWidth, int textureHeight){
            this.consumer = remap ? remapperPool.obtain().wrap(consumer, textureWidth, textureHeight) : consumer;
            return this;
        }

        @CanIgnoreReturnValue
        public RenderParameters setUVRemapped(@NotNull VertexConsumer consumer, Texture texture){
            this.consumer = remap
                    ? remapperPool.obtain().wrap(consumer, (int) (texture.getWidth() / texture.getScale()), (int) (texture.getHeight() / texture.getScale()))
                    : consumer;
            return this;
        }

        public RenderParameters setOverlay(int overlay){
            this.overlay = overlay;
            return this;
        }

        public RenderParameters setColor(int color){
            this.color = color;
            return this;
        }

        @Override
        public void reset() {
            if(consumer instanceof RemappingVertexConsumer remapper) remapperPool.free(remapper);
            consumer = null;
            overlay = 0;
            color = 0;
            remap = false;
        }
    }
}