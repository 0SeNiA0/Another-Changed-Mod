package net.zaharenko424.a_changed.mixin.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.zaharenko424.a_changed.client.cmrs.BufferSourceAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(MultiBufferSource.BufferSource.class)
public abstract class MixinBufferSource implements BufferSourceAccess {

    @Shadow
    protected abstract void endBatch(RenderType renderType, BufferBuilder builder);

    @Unique
    protected final SequencedMap<RenderType, ByteBufferBuilder> a_changed$bufferMap = new Object2ObjectLinkedOpenHashMap<>();
    @Unique
    protected final List<ByteBufferBuilder> a_changed$pool = new ArrayList<>();
    @Unique
    protected final Map<RenderType, BufferBuilder> a_changed$startedBuilders = new HashMap<>();

    @Override
    public VertexConsumer a_changed$getPooledBuffer(RenderType renderType) {
        BufferBuilder bufferbuilder = a_changed$startedBuilders.get(renderType);
        if (bufferbuilder != null) {
            if(renderType.canConsolidateConsecutiveGeometry()) return bufferbuilder;
            endBatch(renderType, bufferbuilder);
        }

        ByteBufferBuilder byteBuilder = a_changed$bufferMap.get(renderType);
        if (byteBuilder == null) {
            byteBuilder = a_changed$pollBuffer();
            a_changed$bufferMap.put(renderType, byteBuilder);
        }
        bufferbuilder = new BufferBuilder(byteBuilder, renderType.mode(), renderType.format());

        a_changed$startedBuilders.put(renderType, bufferbuilder);
        return bufferbuilder;
    }

    public void a_changed$endPooledBatch(){
        RenderType renderType;
        BufferBuilder builder;
        for (Map.Entry<RenderType, ByteBufferBuilder> entry : a_changed$bufferMap.entrySet()){
            renderType = entry.getKey();
            builder = a_changed$startedBuilders.remove(renderType);
            if(builder != null) endBatch(renderType, builder);
            a_changed$pool.add(entry.getValue());
        }
        a_changed$bufferMap.clear();
    }

    @Unique
    private ByteBufferBuilder a_changed$pollBuffer(){
        if(!a_changed$pool.isEmpty()) return a_changed$pool.removeLast();
        return new ByteBufferBuilder(77824);//76KB
    }

    @Inject(at = @At("RETURN"), method = "endBatch()V")
    private void onEndBatch(CallbackInfo ci){
        a_changed$endPooledBatch();
    }

    @Inject(at = @At("HEAD"), method = "endBatch(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/BufferBuilder;)V", cancellable = true)
    private void onEndBatch(RenderType renderType, BufferBuilder builder, CallbackInfo ci){
        if(!a_changed$bufferMap.containsKey(renderType) || !a_changed$startedBuilders.containsValue(builder)) return;
        ci.cancel();
        MeshData meshdata = builder.build();
        if(meshdata == null) return;
        if (renderType.sortOnUpload()) {
            ByteBufferBuilder bytebufferbuilder = a_changed$bufferMap.get(renderType);
            meshdata.sortQuads(bytebufferbuilder, RenderSystem.getVertexSorting());
        }

        renderType.draw(meshdata);
    }
}