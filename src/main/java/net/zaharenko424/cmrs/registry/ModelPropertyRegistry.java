package net.zaharenko424.cmrs.registry;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.property.*;
import net.zaharenko424.cmrs.util.StreamCodecUtils;

import java.util.HashMap;
import java.util.Map;

public class ModelPropertyRegistry {

    public static final DeferredRegister<ModelPropertyType<?>> PROPERTIES = DeferredRegister.create(CMRS.resourceLoc("model_property"), CMRS.MODID);
    public static final Registry<ModelPropertyType<?>> PROPERTY_REGISTRY = PROPERTIES.makeRegistry(builder ->{});

    public static final StreamCodec<FriendlyByteBuf, ModelProperty> CODEC = StreamCodecUtils.RESOURCE_LOC.dispatch(
            property -> property.type().getId(),
            loc -> {
                ModelPropertyType<?> type = PROPERTY_REGISTRY.get(loc);

                if(type == null) throw new IllegalStateException("No ModelPropertyType is registered under " + loc);
                return type.codec();
            }
    );
    public static final StreamCodec<FriendlyByteBuf, Map<String, ModelProperty>> MAP = ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, CODEC);

    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<BooleanProperty>> BOOLEAN = PROPERTIES.register("boolean", () -> new ModelPropertyType<>(BooleanProperty.CODEC_, BooleanProperty.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<FloatProperty>> FLOAT = PROPERTIES.register("float", () -> new ModelPropertyType<>(FloatProperty.CODEC_, FloatProperty.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<IntProperty>> INT = PROPERTIES.register("int", () -> new ModelPropertyType<>(IntProperty.CODEC_, IntProperty.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<StringProperty>> STRING = PROPERTIES.register("string", () -> new ModelPropertyType<>(StringProperty.CODEC_, StringProperty.CODEC));
    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<UnitProperty>> UNIT = PROPERTIES.register("unit", () -> new ModelPropertyType<>(UnitProperty.CODEC_, UnitProperty.CODEC));

    public static final DeferredHolder<ModelPropertyType<?>, ModelPropertyType<FPArms>> FP_ARMS = PROPERTIES.register("fp_arms", () -> new ModelPropertyType<>(FPArms.CODEC_, FPArms.CODEC));
}