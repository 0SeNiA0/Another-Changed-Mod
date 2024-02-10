package net.zaharenko424.a_changed.registry;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class DNAType {

    private final Supplier<ItemLike> material;

    public DNAType(Supplier<ItemLike> material) {
        this.material = material;
    }

    public Ingredient getMaterial(){
        return Ingredient.of(material.get());
    }

    @Override
    public String toString() {
        return DNATypeRegistry.DNA_TYPE_REGISTRY.getKey(this).toString();
    }
}