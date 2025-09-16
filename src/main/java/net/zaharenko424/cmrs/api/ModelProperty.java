package net.zaharenko424.cmrs.api;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.property.ModelPropertyType;

public interface ModelProperty {

    DeferredHolder<ModelPropertyType<?>, ?> type();
}
