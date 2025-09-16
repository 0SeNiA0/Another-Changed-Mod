package net.zaharenko424.cmrs.client.material;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.Material;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.client.renderer.ExtraRenderTypes;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import net.zaharenko424.cmrs.registry.MaterialRegistry;
import net.zaharenko424.cmrs.client.renderer.TransparencyType;

import java.util.List;

public final class Glow implements Material {

    public static final StreamCodec<FriendlyByteBuf, Glow> CODEC = StreamCodec.of(
            (buffer, cutout) -> buffer.writeVarInt(cutout.textureId),
            buffer -> new Glow(buffer.readVarInt())
    );

    private final int textureId;

    public Glow(int textureId){
        if(textureId < 0) throw new IllegalArgumentException("Texture id must be >= 0");
        this.textureId = textureId;
    }

    @Override
    public DeferredHolder<MaterialType<?>, MaterialType<Glow>> type() {
        return MaterialRegistry.GLOW;
    }

    @Override
    public void verifyTextures(List<Texture> textures) {
        if(textures.size() <= textureId || textures.get(textureId) == null) throw new IllegalStateException("Texture with id " + textureId + " not found");
    }

    @Override
    public boolean shouldRenderInFirstPerson() {
        return true;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack.ParameterList parameters, MultiBufferSource source) {
        Texture texture = model.getTexture(textureId);
        parameters.add(source.getBuffer(ExtraRenderTypes.OPAQUE_GLOW.apply(texture.getLocation()), TransparencyType.OPAQUE))
                .texture(texture);
    }
}