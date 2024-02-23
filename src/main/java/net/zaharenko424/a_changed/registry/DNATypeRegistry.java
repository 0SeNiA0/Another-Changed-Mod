package net.zaharenko424.a_changed.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.item.BloodSyringe;

import static net.zaharenko424.a_changed.AChanged.MODID;

public class DNATypeRegistry {

    public static final DeferredRegister<DNAType> DNA_TYPES = DeferredRegister.create(AChanged.resourceLoc("dna_registry"), MODID);
    public static final Registry<DNAType> DNA_TYPE_REGISTRY = DNA_TYPES.makeRegistry(builder -> {});

    public static final DeferredHolder<DNAType, DNAType> APPLE_DNA = DNA_TYPES.register("apple", ()-> new DNAType(Items.APPLE::getDefaultInstance));
    public static final DeferredHolder<DNAType, DNAType> WOLF_DNA = DNA_TYPES.register("wolf", ()-> new DNAType(()-> BloodSyringe.encodeEntityType(EntityType.WOLF, null)));
}