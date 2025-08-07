package net.zaharenko424.cmrs.client.material;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.api.Material;

public record MaterialType <M extends Material> (StreamCodec<FriendlyByteBuf, M> codec) {}
