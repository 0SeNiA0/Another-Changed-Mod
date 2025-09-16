package net.zaharenko424.cmrs.client.material;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.Material;
import net.zaharenko424.cmrs.client.model.RenderStack;
import net.zaharenko424.cmrs.client.model.Texture;
import net.zaharenko424.cmrs.property.IntProperty;
import net.zaharenko424.cmrs.client.renderer.ExtraRenderTypes;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import net.zaharenko424.cmrs.registry.MaterialRegistry;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import net.zaharenko424.cmrs.client.renderer.TransparencyType;

import javax.annotation.Nullable;
import java.util.List;

public final class OpaqueColor implements Material {

    public static final StreamCodec<FriendlyByteBuf, OpaqueColor> CODEC = StreamCodec.of(
            (buffer, cutout) -> {
                String propertyKey = cutout.propertyKey;
                StreamCodecUtils.writeOptionally(propertyKey, propertyKey != null && !propertyKey.isBlank(), buffer, ByteBufCodecs.STRING_UTF8);
                buffer.writeVarInt(cutout.fallbackColor);
                buffer.writeBoolean(cutout.glow);
            },
            buffer ->
                    new OpaqueColor(
                            StreamCodecUtils.readOptionally(buffer, ByteBufCodecs.STRING_UTF8),
                            buffer.readVarInt(),
                            buffer.readBoolean()
                    )
    );

    private final String propertyKey;
    private final int fallbackColor;
    private final boolean glow;

    public OpaqueColor(int color){
        this(null, color);
    }

    public OpaqueColor(@Nullable String propertyKey, int fallbackColor){
        this(propertyKey, fallbackColor, false);
    }

    public OpaqueColor(@Nullable String propertyKey, int fallbackColor, boolean glow){
        this.propertyKey = propertyKey != null && propertyKey.isBlank() ? null : propertyKey;
        this.fallbackColor = fallbackColor;
        this.glow = glow;
    }

    @Override
    public DeferredHolder<MaterialType<?>, MaterialType<OpaqueColor>> type() {
        return MaterialRegistry.OPAQUE_COLOR;
    }

    @Override
    public void verifyTextures(List<Texture> textures) {}

    @Override
    public boolean shouldRenderInFirstPerson() {
        return true;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack.ParameterList parameters, MultiBufferSource source) {
        RenderStack.Parameters param = parameters.add(source.getBuffer(glow ? ExtraRenderTypes.OPAQUE_COLOR_GLOW : ExtraRenderTypes.OPAQUE_COLOR, TransparencyType.OPAQUE));

        if(propertyKey != null){
            IntProperty color = model.getProperty(propertyKey, ModelPropertyRegistry.INT);

            if(color != null){
                param.color(color.value());
                return;
            }
        }

        param.color(fallbackColor == 0 ? -1 : fallbackColor);
    }
}