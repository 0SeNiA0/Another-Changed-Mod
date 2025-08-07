package net.zaharenko424.cmrs.registry;

import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.client.layer.ItemOnHead;
import net.zaharenko424.cmrs.client.property.FPArms;
import net.zaharenko424.cmrs.client.property.ModelPropertyType;

public class ModelPropertyRegistry {

    public static final DeferredRegister<ModelPropertyType<?>> PROPERTIES = DeferredRegister.create(CMRS.resourceLoc("model_property"), CMRS.MODID);
    public static final Registry<ModelPropertyType<?>> PROPERTY_REGISTRY = PROPERTIES.makeRegistry(builder ->{});

    /**
     * Remaps absolute uv to uv relative to the size of the texture. //TODO create UV container for each vertex to easier differentiate absolute & relative?
     */
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<Unit>> REMAP_UV = PROPERTIES.register("remap_uv", () -> new ModelPropertyType<>(StreamCodec.unit(Unit.INSTANCE)));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<String>> HEAD = PROPERTIES.register("head", () -> new ModelPropertyType<>(ByteBufCodecs.STRING_UTF8.mapStream(friendly -> friendly)));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<ItemOnHead>> ITEM_ON_HEAD = PROPERTIES.register("item_on_head", () -> new ModelPropertyType<>(ItemOnHead.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<FPArms>> FP_ARMS = PROPERTIES.register("fp_arms", () -> new ModelPropertyType<>(FPArms.CODEC));
}