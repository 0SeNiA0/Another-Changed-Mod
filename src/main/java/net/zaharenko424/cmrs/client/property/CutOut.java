package net.zaharenko424.cmrs.client.property;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.api.BufferSourceAccess;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.ModelLayer;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CutOut implements ModelLayer {

    public static final StreamCodec<FriendlyByteBuf, CutOut> CODEC = StreamCodec.of((buffer, textures) -> {
        Int2IntArrayMap map = textures.renderIdToTexture;
        buffer.writeVarInt(map.size());
        map.forEach((id, texture) -> {
            buffer.writeVarInt(id);
            buffer.writeVarInt(texture);
        });
    }, buffer -> {
        int size = buffer.readVarInt();
        Int2IntArrayMap map = new Int2IntArrayMap(size);
        for(int i = 0; i < size; i++) {
            map.put(buffer.readVarInt(), buffer.readVarInt());
        }
        return new CutOut(map);
    });

    private final Int2IntArrayMap renderIdToTexture;

    public CutOut(@NotNull Int2IntArrayMap renderIdToTexture){
        this.renderIdToTexture = renderIdToTexture;
    }

    @Override
    public IntSet renderIds() {
        return renderIdToTexture.keySet();
    }

    @Override
    public void verifyTextures(List<Texture> textures) {
        renderIdToTexture.values().forEach(i -> {
            if(textures.size() <= i) throw new IllegalStateException();
        });
    }

    @Override
    public boolean shouldRenderInFirstPerson() {
        return true;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access) {
        access.cmrs$startSubBatch();

        renderIdToTexture.forEach((renderId, textureId) -> {
            Texture texture = model.getTexture(textureId);
            if(renderId != Integer.MIN_VALUE) stack.getOrCreate(renderId).add()
                    .setUVRemapped(access.cmrs$getBuffer(stack.defRenderType(texture.getLocation(), RenderType.ENTITY_CUTOUT), 0), texture);
        });
    }
}