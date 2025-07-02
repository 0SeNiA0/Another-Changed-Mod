package net.zaharenko424.cmrs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.cmrs.client.RemappingVertexConsumer;
import net.zaharenko424.cmrs.client.geom.Mesh;
import net.zaharenko424.cmrs.client.geom.SimpleVertexMultiConsumer;
import net.zaharenko424.cmrs.util.Pool;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class RenderStack {

    protected final Int2ObjectOpenHashMap<ParameterList> map = new Int2ObjectOpenHashMap<>(4);
    protected static final Pool<ParameterList> paramListPool = new Pool<>() {
        @Override
        protected ParameterList newObject() {
            return new ParameterList();
        }
    };
    protected static final Pool<Parameters> paramPool = new Pool<>() {
        @Override
        protected Parameters newObject() {
            return new Parameters();
        }
    };
    protected boolean remap;
    protected Function<ResourceLocation, RenderType> func;

    public ParameterList getOrCreate(int renderId){
        return map.computeIfAbsent(renderId, k -> paramListPool.obtain().init(remap));
    }

    /**
     * Sets whether to remap absolute uv coordinates to relative.
     */
    public void setRemap(boolean remap){
        this.remap = remap;
    }

    /**
     * Sets the default Texture to RenderType function.
     */
    public void setRenderTypeFunc(Function<ResourceLocation, RenderType> func){
        this.func = func;
    }

    /**
     * Applies the default Texture to RenderType function if present or fallback.
     */
    public RenderType defRenderType(ResourceLocation texture, Function<ResourceLocation, RenderType> fallback){
        return func != null ? func.apply(texture) : fallback.apply(texture);
    }

    /**
     * Resets parameters for rendering.
     */
    public void reset(){
        map.values().forEach(paramListPool::free);
        map.clear();
    }

    public void renderMesh(Mesh mesh, PoseStack.Pose matrix, int light, int overlay, int color){
        ParameterList parameters = map.get(mesh.renderId);
        if(parameters == null || parameters.multiConsumer.isEmpty()) return;

        parameters.multiConsumer.forEach(consumer ->
                ((Parameters)consumer).setupIfNS(overlay, color).light(light));

        mesh.compile(matrix, parameters.multiConsumer);
    }

    public static class ParameterList implements Pool.Poolable {

        private final SimpleVertexMultiConsumer multiConsumer = new SimpleVertexMultiConsumer();
        private boolean remap;

        private ParameterList(){}

        ParameterList init(boolean remap){
            this.remap = remap;
            return this;
        }

        public Parameters add(VertexConsumer consumer){
            Parameters params = paramPool.obtain();
            params.wrap(consumer);
            multiConsumer.add(params);
            params.remap = remap;

            return params;
        }

        public void reset(){
            multiConsumer.forEach(consumer -> paramPool.free((Parameters) consumer));
            multiConsumer.clear();
        }
    }

    public static final class Parameters extends RemappingVertexConsumer {

        private boolean overlaySet;
        private boolean colorSet;
        private boolean remap;

        private Parameters(){}

        @Override
        public Parameters texture(int textureWidth, int textureHeight) {
            return remap ? (Parameters) super.texture(textureWidth, textureHeight) : this;
        }

        @Override
        public Parameters texture(Texture texture) {
            return remap ? (Parameters) super.texture(texture) : this;
        }

        public Parameters overlay(int overlay){
            super.overlay(overlay);
            overlaySet = true;
            return this;
        }

        public Parameters color(int color){
            super.color(color);
            colorSet = true;
            return this;
        }

        Parameters setupIfNS(int overlay, int color){
            if(!overlaySet) overlay(overlay);
            if(!colorSet) color(color);
            return this;
        }

        @Override
        public void reset() {
            super.reset();
            overlaySet = false;
            colorSet = false;
            textureWidth = 1;
            textureHeight = 1;
        }
    }
}