package net.zaharenko424.a_changed.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.item.BuildersWand;

import java.util.function.Supplier;

public class ComponentRegistry {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(AChanged.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Component>> BLOOD_OWNER_NAME = COMPONENTS
            .register("blood_owner_name", () -> DataComponentType.<Component>builder()
                    .persistent(ComponentSerialization.FLAT_CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> BLOOD_TYPE = COMPONENTS
            .register("blood_type", resourceLoc());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BuildersWand.Data>> BUILDERS_WAND_DATA = COMPONENTS
            .register("builders_wand_data", () -> DataComponentType.<BuildersWand.Data>builder()
                    .persistent(BuildersWand.Data.CODEC).networkSynchronized(BuildersWand.Data.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> DNA_TYPE = COMPONENTS
            .register("dna_type", resourceLoc());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> DEBUG_STICK_LATEX = COMPONENTS
            .register("debug_stick_latex", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> ENABLED = COMPONENTS
            .register("enabled", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC)
                    .networkSynchronized(ByteBufCodecs.fromCodec(Unit.CODEC)).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ITEM_ENERGY = COMPONENTS
            .register("item_energy", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ITEM_INVENTORY = COMPONENTS
            .register("item_inventory", () -> DataComponentType.<ItemContainerContents>builder()
                    .persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> TRANSFUR_TYPE = COMPONENTS
            .register("transfur_type", resourceLoc());

    private static Supplier<DataComponentType<ResourceLocation>> resourceLoc(){
        return () -> DataComponentType.<ResourceLocation>builder()
                .persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).build();
    }
}