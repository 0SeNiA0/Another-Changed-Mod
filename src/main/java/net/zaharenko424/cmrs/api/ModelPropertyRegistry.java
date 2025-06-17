package net.zaharenko424.cmrs.api;

import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.client.property.*;

public class ModelPropertyRegistry {

    public static final DeferredRegister<ModelPropertyType<?>> PROPERTIES = DeferredRegister.create(CMRS.resourceLoc("model_properties"), CMRS.MODID);
    public static final Registry<ModelPropertyType<?>> PROPERTY_REGISTRY = PROPERTIES.makeRegistry(builder ->{});

    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<Textures>> TEXTURES = PROPERTIES.register("textures", () -> new ModelPropertyType<>(Textures.CODEC));
    /**
     * Remaps absolute uv to uv relative to the size of the texture. //TODO create UV container for each vertex to easier differentiate absolute & relative?
     */
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<Unit>> REMAP_UV = PROPERTIES.register("remap_uv", () -> new ModelPropertyType<>(StreamCodec.unit(Unit.INSTANCE)));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<Armor>> ARMOR = PROPERTIES.register("armor", () -> new ModelPropertyType<>(Armor.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<Glow>> GLOW = PROPERTIES.register("glow", () -> new ModelPropertyType<>(Glow.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<String>> HEAD = PROPERTIES.register("head", () -> new ModelPropertyType<>(ByteBufCodecs.STRING_UTF8.mapStream(friendly -> friendly)));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<ItemOnHead>> ITEM_ON_HEAD = PROPERTIES.register("item_on_head", () -> new ModelPropertyType<>(ItemOnHead.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<ItemInHandLayer>> ITEM_IN_HAND = PROPERTIES.register("item_in_hand", () -> new ModelPropertyType<>(ItemInHandLayer.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<ItemInMawLayer>> ITEM_IN_MAW = PROPERTIES.register("item_in_maw", () -> new ModelPropertyType<>(ItemInMawLayer.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<VanillaElytra>> VANILLA_ELYTRA = PROPERTIES.register("vanilla_elytra", () -> new ModelPropertyType<>(VanillaElytra.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<TridentSpinEffect>> TRIDENT_SPIN_EFFECT = PROPERTIES.register("trident_spin_effect", () -> new ModelPropertyType<>(TridentSpinEffect.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<FPArms>> FP_ARMS = PROPERTIES.register("fp_arms", () -> new ModelPropertyType<>(FPArms.CODEC));
}