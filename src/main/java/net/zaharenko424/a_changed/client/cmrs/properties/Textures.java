package net.zaharenko424.a_changed.client.cmrs.properties;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.util.Int2ObjArrayMap;
import net.zaharenko424.a_changed.client.cmrs.model.RenderStack;
import net.zaharenko424.a_changed.client.cmrs.api.BufferSourceAccess;
import net.zaharenko424.a_changed.client.cmrs.api.CustomModel;
import net.zaharenko424.a_changed.client.cmrs.api.ModelLayer;
import org.jetbrains.annotations.NotNull;

public final class Textures implements ModelLayer {

    public static final StreamCodec<FriendlyByteBuf, Textures> CODEC = StreamCodec.of((buffer, textures) -> {
        Int2ObjectMap<Texture> map = textures.renderIdToTexture;
        buffer.writeVarInt(map.size());
        map.forEach((id, texture) -> {
            buffer.writeVarInt(id);
            Texture.CODEC.encode(buffer, texture);
        });
    }, buffer -> {
        int size = buffer.readVarInt();
        Int2ObjArrayMap<Texture> map = new Int2ObjArrayMap<>(size);
        for(int i = 0; i < size; i++) {
            map.put(buffer.readVarInt(), Texture.CODEC.decode(buffer));
        }
        return new Textures(map);
    });

    private final Int2ObjArrayMap<Texture> renderIdToTexture;

    public Textures(@NotNull Int2ObjArrayMap<Texture> renderIdToTexture){
        this.renderIdToTexture = renderIdToTexture;
    }

    @Override
    public IntSet renderIds() {
        return renderIdToTexture.keySet();
    }

    public ResourceLocation firstTexture(){
        return renderIdToTexture.firstValue().getLocation();
    }

    public Texture textureByIndex(int index){
        return renderIdToTexture.get(index);
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access) {
        access.cmrs$startSubBatch();

        renderIdToTexture.forEach((renderId, texture) -> {//forEach -> fastutil fastForEach
            if(renderId != Integer.MIN_VALUE) stack.getOrCreate(renderId).add()
                    .setUVRemapped(access.cmrs$getBuffer(stack.defRenderType(texture.getLocation(), RenderType.ENTITY_CUTOUT), 0), texture);
        });
    }
}