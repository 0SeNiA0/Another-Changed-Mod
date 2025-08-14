package net.zaharenko424.cmrs.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.zaharenko424.cmrs.CMRS;

import java.io.IOException;
import java.util.function.Function;

@EventBusSubscriber(value = Dist.CLIENT)
public class ExtraRenderTypes {

    public static final VertexFormat POS_COLOR_LIGHT_NORMAL = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV2", VertexFormatElement.UV2)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();

    private static ShaderInstance ENTITY_OPAQUE_COLOR;
    private static ShaderInstance ENTITY_OPAQUE_GLOW_COLOR;

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(Minecraft.getInstance().getResourceManager(), CMRS.resourceLoc("rendertype_entity_opaque_color"), POS_COLOR_LIGHT_NORMAL),
                shader -> ENTITY_OPAQUE_COLOR = shader);

        event.registerShader(new ShaderInstance(Minecraft.getInstance().getResourceManager(), CMRS.resourceLoc("rendertype_entity_opaque_glow_color"), POS_COLOR_LIGHT_NORMAL),
                shader -> ENTITY_OPAQUE_GLOW_COLOR = shader);
    }

    public static ShaderInstance entityOpaqueColor(){
        return ENTITY_OPAQUE_COLOR;
    }

    public static ShaderInstance entityOpaqueGlowColor() {
        return ENTITY_OPAQUE_GLOW_COLOR;
    }

    public static final Function<ResourceLocation, RenderType> OPAQUE_GLOW = Util.memoize(tex -> RenderType.create(
            "glow",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            69621,//Only used by vanilla
            false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_EYES_SHADER)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTextureState(new RenderStateShard.TextureStateShard(tex, false, false))
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .createCompositeState(false)
            )
    );

    public static final RenderType OPAQUE_COLOR = RenderType.create("entity_opaque_color",
            POS_COLOR_LIGHT_NORMAL,
            VertexFormat.Mode.QUADS,
            69621,
            false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ENTITY_OPAQUE_COLOR))
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .createCompositeState(false)
    );

    public static final RenderType OPAQUE_COLOR_GLOW = RenderType.create("entity_opaque_color_glow",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            69621,
            false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ENTITY_OPAQUE_GLOW_COLOR))
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .createCompositeState(false)
    );
}
