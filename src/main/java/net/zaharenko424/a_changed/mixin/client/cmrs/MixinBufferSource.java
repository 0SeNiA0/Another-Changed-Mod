package net.zaharenko424.a_changed.mixin.client.cmrs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.zaharenko424.a_changed.client.cmrs.api.BufferSourceAccess;
import net.zaharenko424.a_changed.util.NonPoolablePool;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultiBufferSource.BufferSource.class)
public abstract class MixinBufferSource implements BufferSourceAccess {

    @Unique
    private static final IntComparator cmrs$COMPARATOR = IntComparators.asIntComparator(Integer::compareTo);

    @Unique
    final NonPoolablePool<ByteBufferBuilder> cmrs$byteBufPool = new NonPoolablePool<>() {
        @Override
        protected ByteBufferBuilder newObject() {
            return new ByteBufferBuilder(77824);//76KB
        }

        @Override
        protected void reset(ByteBufferBuilder element) {
        }
    };
    @Unique
    final NonPoolablePool<Object2ObjectArrayMap<RenderType, Pair<BufferBuilder, ByteBufferBuilder>>> cmrs$mapPool = new NonPoolablePool<>() {

        @Override
        protected Object2ObjectArrayMap<RenderType, Pair<BufferBuilder, ByteBufferBuilder>> newObject() {
            return new Object2ObjectArrayMap<>();
        }

        @Override
        protected void reset(Object2ObjectArrayMap<RenderType, Pair<BufferBuilder, ByteBufferBuilder>> element) {
            element.clear();
        }
    };
    @Unique
    final NonPoolablePool<Pair<BufferBuilder, ByteBufferBuilder>> cmrs$pairPool = new NonPoolablePool<>() {
        @Override
        protected Pair<BufferBuilder, ByteBufferBuilder> newObject() {
            return ObjectObjectMutablePair.of(null, null);
        }

        @Override
        protected void reset(Pair<BufferBuilder, ByteBufferBuilder> element) {
            element.first(null).second(null);
        }
    };

    @Unique
    final Int2ObjectArrayMap<Object2ObjectArrayMap<RenderType, Pair<BufferBuilder, ByteBufferBuilder>>> cmrs$batches = new Int2ObjectArrayMap<>();
    @Unique
    final IntList cmrs$sortingList = new IntArrayList();
    @Unique
    int cmrs$marker;
    @Unique
    int cmrs$tmp;

    @Override
    public void cmrs$startSubBatch() {
        if(cmrs$batches.isEmpty()) return;
        cmrs$tmp++;
        cmrs$marker = cmrs$tmp;
    }

    @Override
    public VertexConsumer cmrs$getBuffer(@NotNull RenderType type, int subBatchIndex) {
        if(!type.canConsolidateConsecutiveGeometry()) throw new IllegalStateException("RenderType has to be able to consolidate geometry!");
        int index = cmrs$marker + subBatchIndex;
        if(subBatchIndex > cmrs$tmp) cmrs$tmp = subBatchIndex;

        Object2ObjectArrayMap<RenderType, Pair<BufferBuilder, ByteBufferBuilder>> map = cmrs$batches.computeIfAbsent(index, k -> cmrs$mapPool.obtain());

        Pair<BufferBuilder, ByteBufferBuilder> pair = map.computeIfAbsent(type, k -> {
            ByteBufferBuilder byteBuilder = cmrs$byteBufPool.obtain();
            return cmrs$pairPool.obtain().first(new BufferBuilder(byteBuilder, type.mode, type.format)).second(byteBuilder);
        });

        return pair.first();
    }

    @Override
    public void cmrs$finishBatched() {
        if(cmrs$batches.isEmpty()) return;
        cmrs$marker = 0;
        cmrs$tmp = 0;

        cmrs$sortingList.addAll(cmrs$batches.keySet());
        cmrs$sortingList.unstableSort(cmrs$COMPARATOR);//Sort, just in case

        Object2ObjectArrayMap<RenderType, Pair<BufferBuilder, ByteBufferBuilder>> m;
        RenderType type;
        Pair<BufferBuilder, ByteBufferBuilder> pair;
        for(int i : cmrs$sortingList){
            m = cmrs$batches.get(i);
            for(Object2ObjectMap.Entry<RenderType, Pair<BufferBuilder, ByteBufferBuilder>> entry1 : m.object2ObjectEntrySet()){
                type = entry1.getKey();
                pair = entry1.getValue();
                cmrs$endBatch(type, pair.first(), pair.second());
                cmrs$byteBufPool.free(pair.second());
                cmrs$pairPool.free(pair);
            }
            cmrs$mapPool.free(m);
        }
        cmrs$batches.clear();
        cmrs$sortingList.clear();
    }

    @Unique
    void cmrs$endBatch(RenderType renderType, BufferBuilder builder, ByteBufferBuilder byteBuilder){
        MeshData meshdata = builder.build();
        if(meshdata == null) return;
        if(renderType.sortOnUpload()) meshdata.sortQuads(byteBuilder, RenderSystem.getVertexSorting());

        renderType.draw(meshdata);
    }
}