package net.zaharenko424.cmrs.client.layer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.api.RenderLayer;

public record RenderLayerType <RL extends RenderLayer> (StreamCodec<FriendlyByteBuf, RL> codec) {}
