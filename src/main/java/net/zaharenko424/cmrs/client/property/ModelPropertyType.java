package net.zaharenko424.cmrs.client.property;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.api.ModelProperty;

public record ModelPropertyType <P extends ModelProperty> (StreamCodec<FriendlyByteBuf, P> codec) {}