package net.zaharenko424.cmrs.registry;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.api.Material;
import net.zaharenko424.cmrs.client.material.*;
import net.zaharenko424.cmrs.util.StreamCodecUtils;

import java.util.List;

public class MaterialRegistry {

    public static final DeferredRegister<MaterialType<?>> MATERIALS = DeferredRegister.create(CMRS.resourceLoc("material"), CMRS.MODID);
    public static final Registry<MaterialType<?>> MATERIAL_REGISTRY = MATERIALS.makeRegistry(builder ->{});

    public static final StreamCodec<FriendlyByteBuf, Material> CODEC = StreamCodecUtils.RESOURCE_LOC.dispatch(
            mat -> mat.type().getId(),
            loc -> {
                MaterialType<?> type = MATERIAL_REGISTRY.get(loc);

                if(type == null) throw new IllegalStateException("No MaterialType is registered under " + loc);
                return type.codec();
            }
    );
    public static final StreamCodec<FriendlyByteBuf, List<Material>> LIST = CODEC.apply(ByteBufCodecs.list());

    public static final DeferredHolder<MaterialType<?>, MaterialType<CutOut>> CUT_OUT = MATERIALS.register("cut_out", () -> new MaterialType<>(CutOut.CODEC));
    public static final DeferredHolder<MaterialType<?>, MaterialType<OpaqueColor>> OPAQUE_COLOR = MATERIALS.register("opaque_color", () -> new MaterialType<>(OpaqueColor.CODEC));
    public static final DeferredHolder<MaterialType<?>, MaterialType<Glow>> GLOW = MATERIALS.register("glow", () -> new MaterialType<>(Glow.CODEC));
    public static final DeferredHolder<MaterialType<?>, MaterialType<VanillaTexArmor>> VANILLA_TEX_ARMOR = MATERIALS.register("vanilla_tex_armor", () -> new MaterialType<>(VanillaTexArmor.CODEC));
}
