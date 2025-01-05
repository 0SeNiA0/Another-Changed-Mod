package net.zaharenko424.a_changed.client.cmrs.properties;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.client.cmrs.api.ModelPropertyRegistry;
import net.zaharenko424.a_changed.client.cmrs.api.ModelPropertyType;
import net.zaharenko424.a_changed.client.cmrs.api.Overridable;
import net.zaharenko424.a_changed.client.cmrs.api.PropertyOverrideMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PropertyOverrideMapImpl implements PropertyOverrideMap {

    private final Reference2ObjectOpenHashMap<ModelPropertyType.Overridable<?, ?>, Object> overrides;

    public PropertyOverrideMapImpl(){
        this(new Reference2ObjectOpenHashMap<>());
    }

    public PropertyOverrideMapImpl(Reference2ObjectOpenHashMap<ModelPropertyType.Overridable<?, ?>, Object> overrides){
        this.overrides = overrides;
    }

    @Override
    public <O> void addOverride(ModelPropertyType.@NotNull Overridable<? extends Overridable<O>, O> type, @NotNull O override) {
        overrides.put(type, override);
    }

    @Override
    public void removeOverride(ModelPropertyType.@NotNull Overridable<?, ?> type) {
        overrides.remove(type);
    }

    @Override
    public <O> @Nullable O getOverride(ModelPropertyType.@NotNull Overridable<? extends Overridable<O>, O> type) {
        return (O) overrides.get(type);
    }

    @Override
    public boolean isEmpty() {
        return overrides.isEmpty();
    }

    @Override
    public <P extends Overridable<O>, O> void write(@NotNull FriendlyByteBuf buffer) {
        Registry<ModelPropertyType<?>> registry = ModelPropertyRegistry.PROPERTY_REGISTRY;
        int size = overrides.size();
        buffer.writeVarInt(size);
        if(size == 0) return;
        ModelPropertyType.Overridable<P, O> type;
        ResourceLocation key;
        for(Map.Entry<ModelPropertyType.Overridable<?, ?>, Object> entry : overrides.entrySet()){
            type = (ModelPropertyType.Overridable<P, O>) entry.getKey();
            key = registry.getKey(type);
            if(key == null) throw new IllegalStateException("Model property type not registered!");
            buffer.writeResourceLocation(key);
            type.override.encode(buffer, (O) entry.getValue());
        }
    }
}