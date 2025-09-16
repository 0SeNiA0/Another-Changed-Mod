package net.zaharenko424.cmrs.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.minecraft.client.renderer.RenderType;
import net.zaharenko424.cmrs.util.*;

import java.util.*;

public class MultiBufferSource {

    public static final int DEF_BUF_SIZE = 65536;//64KB
    public static final int MAX_POOL_BUF_SIZE = 262144;//256KB
    private static MultiBufferSource instance;

    public static MultiBufferSource getInstance(){
        if(instance == null) instance = new MultiBufferSource();

        return instance;
    }

    BufferPool bufferPool = new BufferPool();
    NonPoolablePool<Pair<BufferBuilder, ByteBuffer>> pairPool = new NonPoolablePool<>() {
        @Override
        protected Pair<BufferBuilder, ByteBuffer> newObject() {
            return ObjectObjectMutablePair.of(null, null);
        }

        @Override
        protected void reset(Pair<BufferBuilder, ByteBuffer> element) {
            element.first(null).second(null);
        }
    };
    EnumMap<TransparencyType, Map<RenderType, Pair<BufferBuilder, ByteBuffer>>> layers = new EnumMap<>(TransparencyType.class);

    private MultiBufferSource(){}

    public VertexConsumer getBuffer(RenderType type, TransparencyType transparency){
        Map<RenderType, Pair<BufferBuilder, ByteBuffer>> map = layers.computeIfAbsent(transparency, k -> new LinkedHashMap<>());

        Pair<BufferBuilder, ByteBuffer> pair = map.computeIfAbsent(type, k -> {
            ByteBuffer buffer = bufferPool.obtain();
            buffer.use();
            return pairPool.obtain().first(new BufferBuilder(buffer.getBuffer(), type.mode, type.format)).second(buffer);
        });

        if(pair.first() == null){
            pair.first(new BufferBuilder(pair.second().getBuffer(), type.mode, type.format));
            pair.second().use();
        }

        return pair.first();
    }

    public long allocated(){
        return bufferPool.totalCapacity();
    }

    public void endBatch(){
        for(TransparencyType type : layers.keySet()){
            endBatch(type);
        }

        bufferPool.deallocateUnused(clearTime());//If still unused, deallocate
    }

    public void endBatch(TransparencyType transparency){
        Map<RenderType, Pair<BufferBuilder, ByteBuffer>> map = layers.get(transparency);
        if(layers == null || layers.isEmpty()) return;

        int halfClear = clearTime() / 2;
        map.entrySet().removeIf(entry -> {
            Pair<BufferBuilder, ByteBuffer> pair = entry.getValue();
            pair.second().maybeShrink(halfClear);
            if(pair.first() != null) endBatch(entry.getKey(), pair.first(), pair.second());

            if(pair.second().isUnused(halfClear)){//Unbind buffer from renderType
                bufferPool.free(pair.second());
                pairPool.free(pair);
                return true;
            }

            pair.first(null);
            return false;
        });
    }

    static final long HALF_GB = 512 * 1024 * 1024;
    static final long QUARTER_GB = 256 * 1024 * 1024;

    int clearTime(){
        long total = bufferPool.totalCapacity();
        if(total > Utils.GB) return 500;
        if(total > HALF_GB) return 1000;
        if(total > QUARTER_GB) return 5000;
        return 10000;
    }

    private void endBatch(RenderType type, BufferBuilder builder, ByteBuffer buffer){
        MeshData meshdata = builder.build();
        if(meshdata == null) return;//Seems like this is unnecessary \/
        if(type.sortOnUpload()) meshdata.sortQuads(buffer.getBuffer(), RenderSystem.getVertexSorting());

        type.draw(meshdata);
    }

    private static class BufferPool extends Pool<ByteBuffer> {

        List<ByteBuffer> inUse = new ArrayList<>();

        @Override
        protected ByteBuffer newObject() {
            return new ByteBuffer(DEF_BUF_SIZE);
        }

        @Override
        public ByteBuffer obtain() {
            ByteBuffer buffer = super.obtain();
            inUse.add(buffer);
            return buffer;
        }

        @Override
        public void free(ByteBuffer element) {
            inUse.remove(element);

            if(element.capacity() > MAX_POOL_BUF_SIZE) {//Deallocate if too big
                element.deallocate();
                return;
            }

            super.free(element);
        }

        long totalCapacity(){
            long capacity = 0;

            for(ByteBuffer buf : pool){
                capacity += buf.capacity();
            }

            for(ByteBuffer buf : inUse){
                capacity += buf.capacity();
            }

            return capacity;
        }

        void deallocateUnused(int clearTimeMS){
            pool.removeIf(e -> e.deallocateUnused(clearTimeMS));
        }
    }
}