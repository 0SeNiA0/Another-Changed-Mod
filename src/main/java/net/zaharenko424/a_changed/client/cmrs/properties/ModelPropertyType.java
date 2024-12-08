package net.zaharenko424.a_changed.client.cmrs.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ModelPropertyType<T>(StreamCodec<FriendlyByteBuf, T> codec) {}