package net.zaharenko424.cmrs.registry;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.api.RenderLayer;
import net.zaharenko424.cmrs.client.layer.*;
import net.zaharenko424.cmrs.util.StreamCodecUtils;

import java.util.List;

public class RenderLayerRegistry {

    public static final DeferredRegister<RenderLayerType<?>> LAYERS = DeferredRegister.create(CMRS.resourceLoc("layer"), CMRS.MODID);
    public static final Registry<RenderLayerType<?>> LAYER_REGISTRY = LAYERS.makeRegistry(builder ->{});

    public static final StreamCodec<FriendlyByteBuf, RenderLayer> CODEC = StreamCodecUtils.RESOURCE_LOC.dispatch(
            layer -> layer.type().getId(),
            loc -> {
                RenderLayerType<?> type = LAYER_REGISTRY.get(loc);

                if(type == null) throw new IllegalStateException("No RenderLayerType is registered under " + loc);
                return type.codec();
            }
    );
    public static final StreamCodec<FriendlyByteBuf, List<RenderLayer>> LIST = CODEC.apply(ByteBufCodecs.list());

    public static final DeferredHolder<RenderLayerType<?>, RenderLayerType<ItemInHandLayer>> ITEM_IN_HAND = LAYERS.register("item_in_hand", () -> new RenderLayerType<>(ItemInHandLayer.CODEC));
    public static final DeferredHolder<RenderLayerType<?>, RenderLayerType<ItemInMawLayer>> ITEM_IN_MAW = LAYERS.register("item_in_maw", () -> new RenderLayerType<>(ItemInMawLayer.CODEC));
    public static final DeferredHolder<RenderLayerType<?>, RenderLayerType<ItemOnHead>> ITEM_ON_HEAD = LAYERS.register("item_on_head", () -> new RenderLayerType<>(ItemOnHead.CODEC));
    public static final DeferredHolder<RenderLayerType<?>, RenderLayerType<VanillaElytra>> VANILLA_ELYTRA = LAYERS.register("vanilla_elytra", () -> new RenderLayerType<>(VanillaElytra.CODEC));
    public static final DeferredHolder<RenderLayerType<?>, RenderLayerType<TridentSpinEffect>> TRIDENT_SPIN_EFFECT = LAYERS.register("trident_spin", () -> new RenderLayerType<>(TridentSpinEffect.CODEC));
}
