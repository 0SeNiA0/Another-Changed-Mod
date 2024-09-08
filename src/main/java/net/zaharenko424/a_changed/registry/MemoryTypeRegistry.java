package net.zaharenko424.a_changed.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;

import java.util.Optional;

public class MemoryTypeRegistry {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, AChanged.MODID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> GRAB_ATTEMPT_COOLDOWN = MEMORY_TYPES
            .register("grab_attempt_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> TRANSFUR_HOLDING = MEMORY_TYPES
            .register("transfur_holding", () -> new MemoryModuleType<>(Optional.empty()));
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Boolean>> TRYING_TO_TRANSFUR = MEMORY_TYPES
            .register("trying_to_transfur", () -> new MemoryModuleType<>(Optional.empty()));
}